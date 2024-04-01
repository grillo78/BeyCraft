package com.beycraft.client.particle;

import com.beycraft.common.entity.BeybladeEntity;
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

public class ResonanceParticle extends SpriteTexturedParticle {

    private static final float MAX_QUADSIZE = 0.03F;
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
        List<Entity> list = this.level.getEntities(null, new AxisAlignedBB(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1));
        this.yd = 0.015;
        if (!list.isEmpty()) {
            quadSize = MAX_QUADSIZE;
            this.owner = list.get(0);

            setAlpha(0.3F);
            offset = owner.getPosition(Minecraft.getInstance().getFrameTime()).add(new Vector3d(this.x, this.y, this.z).reverse());
//            if (p_i232448_1_.random.nextBoolean()) {
                setColor((float) speedX, (float) speedY, (float) speedZ);
//            } else {
//                setColor(1,1,1);
//                setAlpha(0.01F);
//                quadSize = 0.02F;
//            }
        } else {
            remove();
        }
    }

    @Override
    public void render(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float partialTicks) {
        if (owner != null && owner.isAlive()) {
            Vector3d position = owner.getPosition(partialTicks).add(offset);
            if(owner instanceof BeybladeEntity)
                position = position.add(((BeybladeEntity)owner).findStadiumCenter().subtract(owner.getPosition(partialTicks)).normalize().multiply(0.01,0,0.01));
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
        this.yo = this.y;
        this.move(0, this.yd, 0);
        if(offset != null)this.yd *= (0.04 - Math.pow(new Vector3d(offset.x,0, offset.z).length(),2))*20;
        if (quadSize > 0) {
            quadSize -= 0.002F;
        } else
            remove();
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
            ResonanceParticle resonanceParticle = new ResonanceParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            resonanceParticle.pickSprite(this.spriteSet);
            return resonanceParticle;
        }
    }
}
