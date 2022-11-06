package com.beycraft.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

public class ResonanceParticle extends SpriteTexturedParticle {

    private static IParticleRenderType RENDER_TYPE = new IParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            startRender();
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE);
        }

        @Override
        public void end(Tessellator tessellator) {
            tessellator.end();
            endRender();
        }
    };
    private Entity owner;
    private Vector3d offset;

    protected ResonanceParticle(ClientWorld p_i232448_1_, double x, double y, double z, double speedX, double speedY, double speedZ) {
        super(p_i232448_1_, x, y, z, 0, 0, 0);
        this.xd = speedX;
        this.yd = speedY;
        this.zd = speedZ;
        List<Entity> list = this.level.getEntities(null, new AxisAlignedBB(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1));
        if (!list.isEmpty()) {
            quadSize = 0.09F;
            this.owner = list.get(0);
            offset = owner.getPosition(Minecraft.getInstance().getFrameTime()).add(new Vector3d(this.x, this.y, this.z).reverse());
            if (p_i232448_1_.random.nextBoolean()) {
                setColor(0, 0, 1);
            } else {
                float value = new Random().nextFloat() / 2;
                setColor(0.5F + value, 0.5F + value, 0);
                setAlpha(0.1F);
                quadSize = 0.08F;
            }
        } else {
            remove();
        }
    }

    @Override
    public void render(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float partialTicks) {
        if (owner != null && owner.isAlive() && quadSize<0.091){
            Vector3d position = owner.getPosition(partialTicks).add(offset);
            xo = x = position.x;
            zo = z = position.z;
            super.render(p_225606_1_, p_225606_2_, partialTicks);
        } else {
            remove();
        }
    }

    private static void startRender() {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003F);
        RenderSystem.disableLighting();
        Minecraft.getInstance().textureManager.bind(AtlasTexture.LOCATION_PARTICLES);
    }

    private static void endRender() {
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }

    @Override
    public void tick() {
        super.tick();
        if (quadSize > 0) {
            quadSize -= 0.005F;
        } else
            remove();
        if(quadSize>0.09){
            remove();
            System.out.println("quadSize = " + quadSize);
        }
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
            ResonanceParticle resonanceParticle = new ResonanceParticle((ClientWorld) worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            resonanceParticle.pickSprite(this.spriteSet);
            return resonanceParticle;
        }
    }
}
