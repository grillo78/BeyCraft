package grillo78.beycraft.client.render;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Representa un segmento del EBO correspondiente a un único material.
 *
 * Todos los submeshes de un ObjMesh comparten el mismo VAO/VBO/EBO;
 * cada uno apunta a un rango distinto de índices dentro del EBO
 * y tiene su propia textura diffuse.
 *
 * @param indexOffset Posición (en índices, no en bytes) donde empieza
 *                    este submesh dentro del EBO compartido.
 * @param indexCount  Número de índices que pertenecen a este submesh.
 * @param texture     ResourceLocation de la textura diffuse, lista para
 *                    pasarse a RenderSystem.setShaderTexture().
 *                    Null si el material no tiene textura diffuse asignada.
 */
public record SubMesh(int indexOffset, int indexCount, ResourceLocation texture, Vec3 center) {

    /**
     * Offset en bytes para glDrawElementsInstanced.
     * OpenGL espera el offset del EBO en bytes, no en índices.
     */
    public long indexOffsetBytes() {
        return (long) indexOffset * Integer.BYTES;
    }

    /**
     * Indica si este submesh tiene textura diffuse asignada.
     */
    public boolean hasTexture() {
        return texture != null;
    }
}
