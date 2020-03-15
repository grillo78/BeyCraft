package com.grillo78.BeyCraft.particles;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.Reference;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSpriteStitcher;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

public class SparkleParticle extends SpriteTexturedParticle {

    public SparkleParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        particleGravity = 1;
        setColor(Color.orange.getRed()/255F,Color.orange.getGreen()/255F,Color.orange.getBlue()/255F);
        particleScale=new Random().nextInt(25)/100;
    }

    public SparkleParticle(World worldIn, double posXIn, double posYIn, double posZIn, double speedX, double speedY, double speedZ) {
        super(worldIn, posXIn, posYIn, posZIn, speedX, speedY, speedZ);
        particleGravity = 0;
        setColor(Color.orange.getRed()/255F,Color.orange.getGreen()/255F,Color.orange.getBlue()/255F);
        particleScale=new Random().nextFloat()/50;
    }

    @Override
    public void tick() {
        super.tick();
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

        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkleParticle sparkleParticle;
            if(xSpeed==0 && ySpeed==0&&zSpeed==0){
                sparkleParticle = new SparkleParticle(worldIn, x, y, z);
            } else{
                sparkleParticle = new SparkleParticle(worldIn, x, y, z, 0, 0, 0);
            }
            sparkleParticle.selectSpriteRandomly(this.spriteSet);
            return sparkleParticle;
        }
    }
}
