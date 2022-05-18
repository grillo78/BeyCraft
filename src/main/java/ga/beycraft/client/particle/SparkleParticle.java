package ga.beycraft.client.particle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ga.beycraft.client.util.CustomRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SparkleParticle extends SpriteTexturedParticle {

    public SparkleParticle(ClientWorld worldIn, double posXIn, double posYIn, double posZIn, double speedX,
                           double speedY, double speedZ) {
        super(worldIn, posXIn, posYIn, posZIn, speedX, speedY, speedZ);
        this.xd = speedX + (Math.random() * 2.0D - 1.0D) * (double)0.1F;
        this.yd = speedY + (Math.random() * 2.0D - 1.0D) * (double)0.2F;
        this.zd = speedZ + (Math.random() * 2.0D - 1.0D) * (double)0.1F;
        lifetime/=10;
    }

    @Override
    public void render(IVertexBuilder p_225606_1_, ActiveRenderInfo activeRenderInfo, float p_225606_3_) {

        IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        IVertexBuilder buffer = buffers.getBuffer(CustomRenderType.THINLINE);
        MatrixStack matrixStack = new MatrixStack();
        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-view.x(), -view.y(), -view.z());

        float f = (float)MathHelper.lerp(p_225606_3_, this.xo, this.x);
        float f1 = (float)MathHelper.lerp(p_225606_3_, this.yo, this.y);
        float f2 = (float)MathHelper.lerp(p_225606_3_, this.zo, this.z);
        matrixStack.translate(f,f1,f2);
        matrixStack.scale(0.5F,0.5F,0.5F);
        Matrix4f matrix4f = matrixStack.last().pose();
        buffer.vertex(matrix4f, 0, 0, 0).color(255, 170, 59, 255).endVertex();
        buffer.vertex(matrix4f, (float) xd/3, (float) yd/3, (float) zd/3).color(255, 170, 59, 255).endVertex();
        buffer.vertex(matrix4f, (float) xd/3, (float) yd/3, (float) zd/3).color(255, 170, 59, 255).endVertex();
        buffer.vertex(matrix4f, (float) xd/2, (float) yd/2, (float) zd/2).color(255, 217, 0, 255).endVertex();
        buffers.endBatch();


        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.alphaFunc(516, 0.003921569F);
        RenderSystem.enableDepthTest();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            SparkleParticle sparkleParticle = new SparkleParticle((ClientWorld) worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            sparkleParticle.pickSprite(this.spriteSet);
            return sparkleParticle;
        }
    }
}
