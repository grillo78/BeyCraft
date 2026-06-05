package grillo78.beycraft.client.render;

import grillo78.beycraft.Beycraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Carga modelos OBJ (y otros formatos soportados por Assimp) directamente
 * desde el sistema de recursos de Minecraft, incluyendo resource packs.
 *
 * Los archivos deben colocarse en:
 *   assets/<modid>/models/<nombre>.obj
 *   assets/<modid>/models/<nombre>.mtl  (opcional, Assimp lo pedirá automáticamente)
 */
public class AssimpResourceLoader {

    /**
     * Carga un modelo 3D desde los assets de Minecraft usando Assimp.
     *
     * @param rm   ResourceManager de Minecraft (obtenido en el reload listener)
     * @param loc  ResourceLocation apuntando al archivo, p.ej:
     *             new ResourceLocation("modid", "beyblade.obj")
     *             Se buscará en assets/modid/models/beyblade.obj
     * @param flags Flags de post-procesado de Assimp, p.ej:
     *              Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenNormals
     * @return AIScene con la geometría cargada. Llamar a Assimp.aiReleaseImport()
     *         cuando ya no se necesite para liberar memoria nativa.
     */
    public static AIScene loadFromResources(ResourceManager rm, ResourceLocation loc, int flags) {

        // Ruta interna dentro de assets: assets/<namespace>/models/<path>
        ResourceLocation modelLoc = ResourceLocation.fromNamespaceAndPath(
                loc.getNamespace(),
                "models/" + loc.getPath()
        );

        // Mapa: dirección del AIFile nativo → ByteBuffer con el contenido del archivo
        Map<Long, ByteBuffer> openFiles = new HashMap<>();
        // Mapa: dirección del AIFile nativo → cursor de lectura actual (en bytes)
        Map<Long, int[]> fileCursors = new HashMap<>();

        // ── Callback: Assimp quiere abrir un archivo ──────────────────────────
        AIFileOpenProcI openProc = (pFileIO, pFileName, pOpenMode) -> {

            String requestedName = MemoryUtil.memUTF8(pFileName);
            ResourceLocation fileLoc = resolveRelative(modelLoc, requestedName);

            Beycraft.LOGGER.debug("Assimp solicita archivo: {}", fileLoc);

            // Leer el recurso en un ByteBuffer nativo
            ByteBuffer data;
            try {
                Resource resource = rm.getResource(fileLoc)
                        .orElseThrow(() -> new FileNotFoundException(fileLoc.toString()));
                byte[] raw = resource.open().readAllBytes();
                data = MemoryUtil.memAlloc(raw.length); // heap nativo, hay que liberar manualmente
                data.put(raw).flip();
            } catch (IOException e) {
                Beycraft.LOGGER.error("Assimp: no se pudo abrir el recurso '{}'", fileLoc, e);
                return 0L; // null → Assimp reportará error de carga
            }

            // Crear la estructura AIFile y registrar sus callbacks
            AIFile aiFile = AIFile.create();
            long filePtr = aiFile.address();

            openFiles.put(filePtr, data);
            fileCursors.put(filePtr, new int[]{0});

            // Lectura
            aiFile.ReadProc((pFile, pBuffer, size, count) -> {
                ByteBuffer buf = openFiles.get(pFile);
                int[] cursor = fileCursors.get(pFile);
                long toRead    = size * count;
                long available = buf.limit() - cursor[0];
                long actual    = Math.min(toRead, available);

                if (actual <= 0) return 0L;

                MemoryUtil.memCopy(
                        MemoryUtil.memAddress(buf) + cursor[0],
                        pBuffer,
                        actual
                );
                cursor[0] += (int) actual;
                return actual / size;
            });

            // Escritura (no necesaria para carga, devuelve 0)
            aiFile.WriteProc((pFile, pBuffer, size, count) -> 0L);

            // Posición actual del cursor
            aiFile.TellProc(pFile -> fileCursors.get(pFile)[0]);

            // Tamaño total del archivo
            aiFile.FileSizeProc(pFile -> openFiles.get(pFile).limit());

            // Seek
            aiFile.SeekProc((pFile, offset, origin) -> {
                ByteBuffer buf = openFiles.get(pFile);
                int[] cursor = fileCursors.get(pFile);

                int newPos = switch (origin) {
                    case Assimp.aiOrigin_SET -> (int) offset;
                    case Assimp.aiOrigin_CUR -> cursor[0] + (int) offset;
                    case Assimp.aiOrigin_END -> buf.limit() + (int) offset;
                    default -> cursor[0];
                };

                // Clamp para evitar out-of-bounds
                cursor[0] = Math.max(0, Math.min(newPos, buf.limit()));
                return Assimp.aiReturn_SUCCESS;
            });

            // Flush (no-op para lectura)
            aiFile.FlushProc(pFile -> {});

            return filePtr;
        };

        // ── Callback: Assimp quiere cerrar un archivo ─────────────────────────
        AIFileCloseProcI closeProc = (pFileIO, pFile) -> {
            ByteBuffer buf = openFiles.remove(pFile);
            if (buf != null) {
                MemoryUtil.memFree(buf); // liberar memoria nativa
            }
            fileCursors.remove(pFile);
        };

        // ── Montar AIFileIO y lanzar la carga ─────────────────────────────────
        try (AIFileIO fileIO = AIFileIO.create()) {
            fileIO.OpenProc(openProc);
            fileIO.CloseProc(closeProc);

            // El primer argumento es solo un identificador; el contenido real
            // lo proporciona openProc a través del ResourceManager.
            AIScene scene = Assimp.aiImportFileEx(
                    modelLoc.getPath(),
                    flags,
                    fileIO
            );

            if (scene == null) {
                // Liberar cualquier buffer que haya quedado abierto en caso de error
                openFiles.values().forEach(MemoryUtil::memFree);
                throw new RuntimeException(
                        "Assimp no pudo cargar '" + modelLoc + "': " + Assimp.aiGetErrorString()
                );
            }

            return scene;
            // Recuerda: llamar a Assimp.aiReleaseImport(scene) cuando termines de usar la escena.
        }
    }

    /**
     * Resuelve la ruta de un archivo que Assimp solicita (p.ej. el .mtl)
     * de forma relativa al archivo base ya conocido.
     *
     * Ejemplo:
     *   base     = "modid:models/beyblade.obj"
     *   requested = "beyblade.mtl"
     *   resultado = "modid:models/beyblade.mtl"
     */
    private static ResourceLocation resolveRelative(ResourceLocation base, String requested) {
        String basePath = base.getPath(); // "models/beyblade.obj"
        String dir = basePath.contains("/")
                ? basePath.substring(0, basePath.lastIndexOf('/') + 1)
                : "";

        // Tomar solo el nombre de archivo de la ruta solicitada
        String fileName = requested.contains("/")
                ? requested.substring(requested.lastIndexOf('/') + 1)
                : requested;

        return ResourceLocation.fromNamespaceAndPath(base.getNamespace(), dir + fileName);
    }
}
