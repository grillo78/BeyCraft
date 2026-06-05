package grillo78.beycraft.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import grillo78.beycraft.Beycraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.assimp.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Representa un modelo 3D cargado en GPU mediante VAO/VBO/EBO.
 * <p>
 * Layout del VBO (stride = 40 bytes = 10 floats):
 * attrib 0 → Position  vec3 float  offset  0  (12 bytes)
 * attrib 1 → Normal    vec3 float  offset 12  (12 bytes)
 * attrib 2 → UV0       vec2 float  offset 24  ( 8 bytes)
 * attrib 3 → UV2       vec2 float  offset 32  ( 8 bytes)
 * <p>
 * Todos los atributos se usan en el shader para evitar que el compilador
 * GLSL los elimine y asigne location -1, lo que causaría errores de VAO.
 */
public class ObjMesh {

    public static ShaderInstance OBJ_SHADER;

    public final int vao;
    public final int vbo;
    public final int ebo;

    private final List<SubMesh> subMeshes;

    private ObjMesh(int vao, int vbo, int ebo, List<SubMesh> subMeshes) {
        this.vao = vao;
        this.vbo = vbo;
        this.ebo = ebo;
        this.subMeshes = Collections.unmodifiableList(subMeshes);
    }

    public List<SubMesh> subMeshes() {
        return subMeshes;
    }

    public int totalIndexCount() {
        return subMeshes.stream().mapToInt(SubMesh::indexCount).sum();
    }

    public static ObjMesh fromScene(AIScene scene, String namespace) {
        int meshCount = scene.mNumMeshes();
        if (meshCount == 0) {
            throw new IllegalArgumentException("El AIScene no contiene ningún mesh.");
        }

        int totalVertices = 0;
        int totalIndices = 0;

        AIMesh[] aiMeshes = new AIMesh[meshCount];
        for (int m = 0; m < meshCount; m++) {
            aiMeshes[m] = AIMesh.create(scene.mMeshes().get(m));
            totalVertices += aiMeshes[m].mNumVertices();
            totalIndices += aiMeshes[m].mNumFaces() * 3;
        }

        // 10 floats por vértice: pos(3) + normal(3) + uv0(2) + uv2(2)
        FloatBuffer vertices = MemoryUtil.memAllocFloat(totalVertices * 10);
        IntBuffer indices = MemoryUtil.memAllocInt(totalIndices);

        List<SubMesh> subMeshes = new ArrayList<>();
        int indexOffset = 0;
        int vertexBase = 0;

        for (int m = 0; m < meshCount; m++) {
            AIMesh aiMesh = aiMeshes[m];

            AIVector3D.Buffer positions = aiMesh.mVertices();
            AIVector3D.Buffer normals = aiMesh.mNormals();
            AIVector3D.Buffer uvs = aiMesh.mTextureCoords(0);
            int vCount = aiMesh.mNumVertices();
            float centerX = 0f;
            float centerY = 0f;
            float centerZ = 0f;

            for (int v = 0; v < vCount; v++) {
                // Position (offset 0, 12 bytes)
                AIVector3D pos = positions.get(v);
                centerX += pos.x();
                centerY += pos.y();
                centerZ += pos.z();

                vertices.put(pos.x()).put(pos.y()).put(pos.z());

                // Normal (offset 12, 12 bytes)
                if (normals != null) {
                    AIVector3D n = normals.get(v);
                    vertices.put(n.x()).put(n.y()).put(n.z());
                } else {
                    vertices.put(0f).put(1f).put(0f);
                }

                // UV0 diffuse (offset 24, 8 bytes)
                if (uvs != null) {
                    AIVector3D uv = uvs.get(v);
                    vertices.put(uv.x()).put(uv.y());
                } else {
                    vertices.put(0f).put(0f);
                }

                // UV2 lightmap placeholder (offset 32, 8 bytes)
                vertices.put(0f).put(0f);
            }
            centerX /= vCount;
            centerY /= vCount;
            centerZ /= vCount;

            int faceCount = aiMesh.mNumFaces();
            int iCount = 0;
            AIFace.Buffer faces = aiMesh.mFaces();

            for (int f = 0; f < faceCount; f++) {
                AIFace face = faces.get(f);
                if (face.mNumIndices() != 3) continue;
                indices.put(vertexBase + face.mIndices().get(0));
                indices.put(vertexBase + face.mIndices().get(1));
                indices.put(vertexBase + face.mIndices().get(2));
                iCount += 3;
            }

            ResourceLocation texture = extractDiffuseTexture(scene, aiMesh, namespace);
            subMeshes.add(new SubMesh(indexOffset, iCount, texture, new Vec3(centerX, centerY, centerZ)));

            indexOffset += iCount;
            vertexBase += vCount;
        }

        vertices.flip();
        indices.flip();

        // ── Crear objetos OpenGL ──────────────────────────────────────────────
        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();
        int ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        final int stride = 10 * Float.BYTES; // 40 bytes

        // attrib 0: Position
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0L);
        GL20.glEnableVertexAttribArray(0);

        // attrib 1: Normal
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, 3L * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // attrib 2: UV0
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, 6L * Float.BYTES);
        GL20.glEnableVertexAttribArray(2);


        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        MemoryUtil.memFree(vertices);
        MemoryUtil.memFree(indices);

        return new ObjMesh(vao, vbo, ebo, subMeshes);
    }

    // ── Extracción de textura diffuse ─────────────────────────────────────────

    private static ResourceLocation extractDiffuseTexture(AIScene scene, AIMesh aiMesh,
                                                          String namespace) {
        int materialIndex = aiMesh.mMaterialIndex();
        if (scene.mNumMaterials() == 0 || materialIndex < 0) return null;

        AIMaterial material = AIMaterial.create(scene.mMaterials().get(materialIndex));

        AIString texPath = AIString.calloc();
        int result = Assimp.aiGetMaterialTexture(
                material,
                Assimp.aiTextureType_DIFFUSE,
                0,
                texPath,
                (IntBuffer) null, (IntBuffer) null, (FloatBuffer) null,
                (IntBuffer) null, (IntBuffer) null, (IntBuffer) null
        );

        if (result != Assimp.aiReturn_SUCCESS) {
            texPath.free();
            return null;
        }

        String rawName = texPath.dataString();
        texPath.free();

        if (rawName == null || rawName.isBlank()) return null;

        if (!rawName.endsWith(".png")) {
            rawName = rawName + ".png";
        }

        return ResourceLocation.fromNamespaceAndPath(namespace, "textures/" + rawName);
    }

    // ── Draw call interno ─────────────────────────────────────────────────────
    private void drawSubMesh(SubMesh sub, ShaderInstance shader, Matrix4f modelView,
                             float lightU, float lightV, int lightmapTexId) {
        int minecraftVAO = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        if (sub.indexCount() <= 0) return;

        // ── Uniforms ──────────────────────────────────────────────────────────
        if (shader.MODEL_VIEW_MATRIX != null) shader.MODEL_VIEW_MATRIX.set(modelView);
        if (shader.PROJECTION_MATRIX != null) shader.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
        if (shader.COLOR_MODULATOR   != null) shader.COLOR_MODULATOR.set(1f, 1f, 1f, 1f);

        var lightmapUniform = shader.getUniform("LightmapUV");
        if (lightmapUniform != null) lightmapUniform.set(lightU, lightV);

        // ── apply() ───────────────────────────────────────────────────────────
        RenderSystem.setShader(() -> shader);
        shader.apply();

        // ── Bindear VAO después de apply() ────────────────────────────────────
        GL30.glBindVertexArray(vao);

        // ── Forzar binding units de samplers DESPUÉS de apply() ───────────────
        // apply() puede mapear Sampler0→slot0 y Sampler2→slot1 internamente.
        // glUniform1i fuerza el mapeo correcto independientemente de lo que haga Minecraft.
        int programId = shader.getId();
        int locSampler0 = GL20.glGetUniformLocation(programId, "Sampler0");
        int locSampler2 = GL20.glGetUniformLocation(programId, "Sampler2");

        if (locSampler0 >= 0) GL20.glUniform1i(locSampler0, 0); // Sampler0 → slot GL_TEXTURE0
        if (locSampler2 >= 0) GL20.glUniform1i(locSampler2, 2); // Sampler2 → slot GL_TEXTURE2


        // ── Bindear texturas en los slots correctos ───────────────────────────
        if (sub.hasTexture()) {
            AbstractTexture diffuseTex = Minecraft.getInstance()
                    .getTextureManager()
                    .getTexture(sub.texture(), MissingTextureAtlasSprite.getTexture());
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTex.getId());
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, lightmapTexId);

        GL13.glActiveTexture(GL13.GL_TEXTURE0); // restaurar slot activo

        // ── Draw ──────────────────────────────────────────────────────────────
        GL11.glDrawElements(GL11.GL_TRIANGLES, sub.indexCount(), GL11.GL_UNSIGNED_INT, sub.indexOffsetBytes());

        shader.clear();
        GL30.glBindVertexArray(minecraftVAO);
    }

    public void render(PoseStack poseStack, int packedLight, float alpha) {
        if (OBJ_SHADER == null) return;
        if (subMeshes.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5,0.5,0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(180));

        Matrix4f modelView = new Matrix4f(RenderSystem.getModelViewMatrix())
                .mul(poseStack.last().pose());

        float lightU = ((packedLight & 0xFFFF)         + 8f) / 256f;
        float lightV = (((packedLight >> 16) & 0xFFFF) + 8f) / 256f;

        // Obtener ID del lightmap una sola vez
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        int lightmapTexId = Minecraft.getInstance()
                .getTextureManager()
                .getTexture(Minecraft.getInstance().gameRenderer.lightTexture().lightTextureLocation)
                .getId();

        // Ordenar submeshes back-to-front
        List<SubMesh> sorted = new ArrayList<>(subMeshes);
        sorted.sort(Comparator.comparingDouble(sub -> {
            Vec3 c = sub.center();
            float wx = (float)(modelView.m00()*c.x + modelView.m10()*c.y + modelView.m20()*c.z + modelView.m30());
            float wy = (float)(modelView.m01()*c.x + modelView.m11()*c.y + modelView.m21()*c.z + modelView.m31());
            float wz = (float)(modelView.m02()*c.x + modelView.m12()*c.y + modelView.m22()*c.z + modelView.m32());
            return -(wx*wx + wy*wy + wz*wz);
        }));

        var shader = OBJ_SHADER;
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        var alphaUniform = shader.getUniform("Alpha");
        if (alphaUniform != null) alphaUniform.set(alpha);

        for (SubMesh sub : sorted) {
            drawSubMesh(sub, shader, modelView, lightU, lightV, lightmapTexId);
        }

        // ── Restaurar estado ──────────────────────────────────────────────────
        GL11.glDepthMask(true);
        RenderSystem.disableBlend();
        Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
        poseStack.popPose();
    }

    // ── Limpieza ──────────────────────────────────────────────────────────────

    public void cleanup() {
//        subMeshes.forEach(subMesh -> {
//            if(subMesh.hasTexture() && Minecraft.getInstance().getTextureManager().getTexture(subMesh.texture(), MissingTextureAtlasSprite.getTexture()) != MissingTextureAtlasSprite.getTexture())
//                Minecraft.getInstance().getTextureManager().release(subMesh.texture());
//        });
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ebo);
    }
}