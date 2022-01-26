package ga.beycraft.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class ResonanceParticle extends DeceleratingParticle {

    private EntityBey bey;
    private float xOffset = 0;
    private boolean xDirection = random.nextBoolean();
    private float yOffset = 0;
    private float zOffset = 0;
    private boolean zDirection = random.nextBoolean();
    private float fixedRoll = random.nextInt(360);
    private static int lastColor = 0;
    private boolean isForPlayer;

    private static final IParticleRenderType RENDER_TYPE = new IParticleRenderType() {
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            textureManager.bind(AtlasTexture.LOCATION_PARTICLES);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(516, 0.003921569F);
            bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE);
        }

        public void end(Tessellator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "CUSTOM_PARTICLE_SHEET_TRANSLUCENT";
        }
    };

    protected ResonanceParticle(ClientWorld worldIn, double posXIn, double posYIn, double posZIn, double speedX,
                                double speedY, double speedZ, boolean isForPlayer, EntityBey bey) {
        super(worldIn, posXIn, posYIn, posZIn, speedX, speedY, speedZ);
        this.bey = bey;
        this.isForPlayer = isForPlayer;
        quadSize = 0.075F;
        lifetime = 20;
        ItemBeyLayer.Color color = null;
        alpha = 0.2F;
        lastColor++;
        if (lastColor == 3)
            lastColor = 0;
        switch (lastColor) {
            case 0:
                color = ((ItemBeyLayer) bey.getLayer().getItem()).getResonanceColor(bey.getLayer());
                break;
            case 1:
                color = ((ItemBeyLayer) bey.getLayer().getItem()).getSecondResonanceColor(bey.getLayer());
                break;
            case 2:
                color = ((ItemBeyLayer) bey.getLayer().getItem()).getThirdResonanceColor(bey.getLayer());
                break;
        }
        this.setColor(color.getRed(), color.getGreen(), color.getBlue());
        updatePosition();
    }

    @Override
    protected int getLightColor(float p_189214_1_) {
        return 11534560;
    }

    @Override
    public void move(double p_187110_1_, double p_187110_3_, double p_187110_5_) {
        updatePosition();
    }

    public void updatePosition() {
        Vector3d position = isForPlayer && PlayerUtils.getPlayerByName(bey.getPlayerName(), level) != null ? PlayerUtils.getPlayerByName(bey.getPlayerName(), level).getPosition(Minecraft.getInstance().getFrameTime()).add(-0.5, 0, -0.5) : bey.getPosition(Minecraft.getInstance().getFrameTime()).add(bey.getDeltaMovement().scale(0.1));
        setPos(position.x + xOffset, position.y + yOffset, position.z + zOffset);
    }

    @Override
    public void tick() {
        super.tick();
        if (bey.isAlive()) {
            xOffset += (xDirection ? -1 : 1) * random.nextFloat() / 100;
            yOffset += 0.01F;
            zOffset += (zDirection ? -1 : 1F) * random.nextFloat() / 100;
            oRoll = roll;
            roll += 0.01;
            if (quadSize > 0)
                quadSize = quadSize + 0.005F;
        } else
            this.remove();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;
        private final Random random = new Random();

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EntityBey bey = null;
            for (EntityBey entity : worldIn.getEntitiesOfClass(EntityBey.class, new AxisAlignedBB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1))) {
                bey = entity;
            }
            final ResonanceParticle[] resonanceParticle = {null};
            PlayerEntity player = PlayerUtils.getPlayerByName(bey.getPlayerName(), worldIn);
            if (player != null) {
                float xOffset;
                float yOffset;
                float zOffset;
                if (typeIn == BeyCraftRegistry.RESONANCE_PLAYER) {
                    xOffset = 0.2F+random.nextFloat()/1.5F;
                    yOffset = random.nextFloat() * 2;
                    zOffset = 0.2F+random.nextFloat()/1.5F;
                    x = player.getX();
                    y = player.getY();
                    z = player.getZ();
                }else {

                    xOffset = 0.19F/2-random.nextFloat()/8;
                    yOffset = 0.19F/2-random.nextFloat()/5;
                    zOffset = 0.19F/2-random.nextFloat()/8;
                }
                double finalX = x;
                double finalY = y;
                double finalZ = z;
                float finalXOffset = xOffset;
                float finalYOffset = yOffset;
                float finalZOffset = zOffset;
                EntityBey finalBey = bey;
                player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(bladerLevel -> {
                    if(bladerLevel.isInResonance()){
                        resonanceParticle[0] = new ResonanceParticle((ClientWorld) worldIn, finalX + finalXOffset, finalY + finalYOffset, finalZ + finalZOffset, xSpeed, ySpeed, zSpeed, typeIn != BeyCraftRegistry.RESONANCE_BEY, finalBey);
                        resonanceParticle[0].pickSprite(this.spriteSet);
                        if (typeIn == BeyCraftRegistry.RESONANCE_PLAYER)
                            resonanceParticle[0].quadSize = 0.1F;
                        resonanceParticle[0].xOffset = finalXOffset;
                        resonanceParticle[0].yOffset = finalYOffset;
                        resonanceParticle[0].zOffset = finalZOffset;
                    }
                });
            }
            return resonanceParticle[0];
        }
    }
}
