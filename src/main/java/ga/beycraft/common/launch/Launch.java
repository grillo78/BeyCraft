package ga.beycraft.common.launch;

import ga.beycraft.Beycraft;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import xyz.heroesunited.heroesunited.common.capabilities.HUPlayer;

public class Launch {

    private static final ResourceLocation ANIMATIONS_FILE = new ResourceLocation(Beycraft.MOD_ID,"animations/launches.animation.json");

    protected LaunchType launchType;

    public Launch(LaunchType launchType) {
        this.launchType = launchType;
    }

    private Launch() {
        this.launchType = LaunchTypes.BASIC_LAUNCH_TYPE;
    }

    public static Launch getLaunch(){
        return new Launch();
    }

    public BeybladeEntity launchBeyblade(ItemStack beyblade, World level, PlayerEntity player, Hand hand) {
        LayerItem layer = (LayerItem) beyblade.getItem();
        BeybladeEntity beybladeEntity = new BeybladeEntity(level, beyblade.copy(), this);
        beybladeEntity.moveTo(player.position().x + player.getLookAngle().x / 2,
                player.position().y + 1 + player.getLookAngle().y / 2,
                player.position().z + player.getLookAngle().z / 2, (float) (player.yRot + layer.getRotationDirection(beyblade).getValue() * Math.toRadians(-45)), 0);
        level.addFreshEntity(beybladeEntity);
        beybladeEntity.setDeltaMovement(getLaunchImpulse(player));
        beyblade.shrink(1);
        HUPlayer.getCap(player).setAnimation(getAnimation(getHandSide(hand,player)), ANIMATIONS_FILE, false);
        return beybladeEntity;
    }

    public void moveBeyblade(BeybladeEntity beyblade){
        LayerItem layer = (LayerItem) beyblade.getStack().getItem();
        int dir = layer.getRotationDirection(beyblade.getStack()).getValue();
        Vector3d stadiumCenter = beyblade.findStadiumCenter();
        if(beyblade.getEnergy()<=1)
            beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
        else
            beyblade.setYHeadRot(beyblade.getYHeadRot()-0.01F);
        Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().add(beyblade.getBbWidth() / 2, 0, beyblade.getBbWidth() / 2).reverse());
        beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(distanceToCenter
                .multiply(0.025 * layer.getRadiusReduction(beyblade.getStack()), 0, 0.025 * layer.getRadiusReduction(beyblade.getStack())))
                .add(distanceToCenter
                        .multiply(0.08, 0, 0.08).yRot((float) (Math.toRadians(-90) * dir))));
    }

    public CompoundNBT serializeNBT(){
        return new CompoundNBT();
    }

    private Vector3d getLaunchImpulse(PlayerEntity player) {
        return Vector3d.ZERO;
    }

    private HandSide getHandSide(Hand hand, PlayerEntity player) {
        return hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
    }

    private String getAnimation(HandSide hand) {
        return hand != HandSide.RIGHT ? "basic_launch" : "basic_launch_mirror";
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void deserializeNBT(CompoundNBT launchNBT) {

    }
}
