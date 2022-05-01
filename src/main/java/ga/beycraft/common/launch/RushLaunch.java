package ga.beycraft.common.launch;

import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.util.math.vector.Vector3d;

public class RushLaunch extends Launch {

    private float speed = 15;

    public RushLaunch() {
        super(LaunchTypes.RUSH_LAUNCH_TYPE);
    }

    @Override
    public void moveBeyblade(BeybladeEntity beyblade) {
        if (beyblade.getEnergy() < 25)
            super.moveBeyblade(beyblade);
        else {

            Vector3d stadiumCenter = beyblade.findStadiumCenter();
            Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
            double distance = distanceToCenter.length();
            if (distance>1.2) {
                beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
            }

            beyblade.yRot -= 2 * ((LayerItem)beyblade.getStack().getItem()).getRotationDirection(beyblade.getStack()).getValue();

            beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(beyblade.getLookAngle().multiply(0.0125*speed, 0, 0.0125*speed).add(distanceToCenter.multiply(0.025,0,0.025))));
        }
    }
}