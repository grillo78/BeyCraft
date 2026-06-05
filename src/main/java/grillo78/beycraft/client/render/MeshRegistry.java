package grillo78.beycraft.client.render;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.util.HashMap;
import java.util.Map;

/**
 * Registro central de meshes cargados en GPU.
 * <p>
 * Gestiona el ciclo de vida completo de los ObjMesh:
 * carga desde assets, almacenamiento por ResourceLocation, y limpieza de VRAM.
 * <p>
 * Uso típico:
 * - Registrar modelos en RegisterClientReloadListenersEvent
 * - Obtener meshes en el render loop con MeshRegistry.get()
 * - La limpieza se hace automáticamente al recargar recursos
 */
public class MeshRegistry {

    /**
     * Flags de Assimp aplicados a todos los modelos cargados.
     */
    private static final int ASSIMP_FLAGS =
            Assimp.aiProcess_Triangulate | // convertir polígonos a triángulos
                    Assimp.aiProcess_GenNormals | // generar normales si faltan
                    Assimp.aiProcess_FlipUVs | // ajustar UVs para OpenGL
                    Assimp.aiProcess_JoinIdenticalVertices; // optimizar vértices duplicados

    /**
     * Mapa principal: ResourceLocation → ObjMesh en GPU.
     */
    private static final Map<ResourceLocation, ObjMesh> meshes = new HashMap<>();

    // ── API pública ───────────────────────────────────────────────────────────

    /**
     * Carga un modelo desde los assets y lo registra en el mapa.
     * Si el modelo ya estaba registrado, no lo recarga (safe to call multiple times).
     *
     * @param rm  ResourceManager de Minecraft
     * @param loc ResourceLocation del modelo, p.ej:
     *            new ResourceLocation("modid", "beyblade_blade.obj")
     *            Se buscará en assets/modid/models/beyblade_blade.obj
     */
    public static void register(ResourceManager rm, ResourceLocation loc) {
        if (meshes.containsKey(loc)) {
            Beycraft.LOGGER.debug("MeshRegistry: '{}' ya estaba registrado, omitiendo.", loc);
            return;
        }

        Beycraft.LOGGER.info("MeshRegistry: cargando '{}'...", loc);

        try {
            AIScene scene = AssimpResourceLoader.loadFromResources(rm, loc, ASSIMP_FLAGS);
            ObjMesh mesh = ObjMesh.fromScene(scene, loc.getNamespace());
            Assimp.aiReleaseImport(scene); // liberar memoria nativa de Assimp

            meshes.put(loc, mesh);
            Beycraft.LOGGER.info("MeshRegistry: '{}' cargado ({} índices).", loc, mesh.totalIndexCount());

        } catch (Exception e) {
            Beycraft.LOGGER.error("MeshRegistry: fallo al cargar '{}'.", loc, e);
        }
    }

    /**
     * Devuelve el ObjMesh asociado a un ResourceLocation, o null si no existe.
     *
     * @param loc ResourceLocation usado al registrar el modelo
     * @return ObjMesh listo para renderizar, o null si no fue registrado/cargado
     */
    public static ObjMesh get(ResourceLocation loc) {
        ObjMesh mesh = meshes.get(loc);
        if (mesh == null) {
            Beycraft.LOGGER.warn("MeshRegistry: se solicitó '{}' pero no está registrado.", loc);
        }
        return mesh;
    }

    /**
     * Indica si un modelo está registrado y disponible.
     *
     * @param loc ResourceLocation del modelo
     * @return true si el modelo está cargado en GPU
     */
    public static boolean isLoaded(ResourceLocation loc) {
        return meshes.containsKey(loc);
    }

    /**
     * Libera todos los recursos de GPU y limpia el mapa.
     * Debe llamarse antes de recargar recursos (en el reload listener)
     * y al cerrar el cliente.
     */
    public static void cleanup() {
        int count = meshes.size();
        meshes.values().forEach(ObjMesh::cleanup);
        meshes.clear();
        Beycraft.LOGGER.info("MeshRegistry: {} mesh(es) liberados de GPU.", count);
    }

    // ── Prevenir instanciación ────────────────────────────────────────────────
    private MeshRegistry() {
    }

    public static void registerAll() {
        MeshRegistry.cleanup();
        BeypartsReloadListener.BEYPARTS.forEach(((resourceLocation, beypart) -> {
            MeshRegistry.register(Minecraft.getInstance().getResourceManager(), beypart.getModel());
        }));
    }
}
