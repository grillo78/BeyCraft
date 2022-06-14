package ga.beycraft.common.launch;

import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import ga.beycraft.common.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlashLaunch extends Launch {

    private boolean hasAttacked = false;
    private boolean normalMovement = false;
    private int counter = 0;
    private float speed = 0.2F;

    public FlashLaunch() {
        super(LaunchTypes.FLASH_LAUNCH_TYPE);
    }

    @Override
    public void moveBeyblade(BeybladeEntity beyblade) {
        counter++;
        if (beyblade.getEnergy() < 25) {
            normalMovement = true;
            super.moveBeyblade(beyblade);
        } else {
            LayerItem layer = (LayerItem) beyblade.getStack().getItem();
            int dir = layer.getRotationDirection(beyblade.getStack()).getValue();
            Vector3d stadiumCenter = beyblade.findStadiumCenter();
            Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
            beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
            double distance = distanceToCenter.length();
            double speed = layer.getSpeed(beyblade.getStack());
            if (hasAttacked) {
                if (distance < 1.3) {
                    this.speed += 0.00001 / distance;
                    super.moveBeyblade(beyblade);
                } else {
                    this.speed = 0.2F;
                    hasAttacked = false;
                }
            } else if (distance < 0.2)
                hasAttacked = true;
            else
                beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(distanceToCenter
                        .multiply(0.5 * layer.getRadiusReduction(beyblade.getStack()), 0, 0.5 * layer.getRadiusReduction(beyblade.getStack())))
                        .add(distanceToCenter
                                .multiply(0.08 * speed / 15, 0, 0.08 * speed / 15).yRot((float) (Math.toRadians(-90) * dir))));
        }
    }

    @Override
    public void onAttack(BeybladeEntity attacker, BeybladeEntity entity) {
        Vector3d stadiumCenter = attacker.findStadiumCenter();
        Vector3d distanceToCenter = stadiumCenter.add(attacker.position().reverse());
        entity.setDeltaMovement(distanceToCenter.multiply(3 * distanceToCenter.length(), 0, 3 * distanceToCenter.length()));
        if (!hasAttacked) {
            this.hasAttacked = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderTick(BeybladeEntity beyblade) {
        super.renderTick(beyblade);
        if (beyblade.isOnGround() && !normalMovement) {
            Vector3d stadiumCenter = beyblade.findStadiumCenter();
            Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
            Vector3d movement = distanceToCenter.multiply(0.1, 0.1, 0.1).yRot(90);
            Vector3d position = beyblade.getPosition(Minecraft.getInstance().getFrameTime()).add(0, 0.01, 0);
            for (int i = 0; i < 25; i++) {
                beyblade.level.addParticle(ModParticles.SPARKLE, position.x, position.y, position.z, movement.x, movement.y, movement.z);
            }
        }
    }

    @Override
    public double getSpeed() {

        return hasAttacked || !normalMovement ? speed : super.getSpeed();
    }

    @Override
    public double getReduction() {
        return hasAttacked || !normalMovement ? 0.2 : super.getReduction();
    }
}
