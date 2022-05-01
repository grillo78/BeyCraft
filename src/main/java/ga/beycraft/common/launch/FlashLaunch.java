package ga.beycraft.common.launch;

import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.util.math.vector.Vector3d;

public class FlashLaunch extends Launch {

    private boolean hasAttacked = false;
    private boolean normalMovement = false;
    private int counter = 0;

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
            if (counter < 40)
                super.moveBeyblade(beyblade);
            else {
                LayerItem layer = (LayerItem) beyblade.getStack().getItem();
                int dir = layer.getRotationDirection(beyblade.getStack()).getValue();
                Vector3d stadiumCenter = beyblade.findStadiumCenter();
                Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
                beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
                double distance = distanceToCenter.length();
                double speed = layer.getSpeed(beyblade.getStack());
                if (hasAttacked) {
                    if (distance < 1.3) {
                        super.moveBeyblade(beyblade);
                    } else {
                        hasAttacked = false;
                    }
                } else
                    beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(distanceToCenter
                            .multiply(0.5 * layer.getRadiusReduction(beyblade.getStack()), 0, 0.5 * layer.getRadiusReduction(beyblade.getStack())))
                            .add(distanceToCenter
                                    .multiply(0.08 * speed / 15, 0, 0.08 * speed / 15).yRot((float) (Math.toRadians(-90) * dir))));
            }
        }
    }

    @Override
    public void onAttack(BeybladeEntity attacker, BeybladeEntity entity) {
        Vector3d stadiumCenter = attacker.findStadiumCenter();
        Vector3d distanceToCenter = stadiumCenter.add(attacker.position().reverse());
        entity.setDeltaMovement(distanceToCenter.multiply(3*distanceToCenter.length(), 0, 3*distanceToCenter.length()));
        if (!hasAttacked) {
            this.hasAttacked = true;
        }
    }

    @Override
    public double getSpeed() {
        return !normalMovement ? 0.2 : super.getSpeed();
    }

    @Override
    public double getReduction() {
        return !normalMovement ? 0.2 : super.getReduction();
    }
}
