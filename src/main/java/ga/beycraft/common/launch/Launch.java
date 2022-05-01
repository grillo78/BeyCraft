package ga.beycraft.common.launch;

import ga.beycraft.Beycraft;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.item.LayerItem;
import ga.beycraft.common.stats.CustomStats;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
        player.awardStat(CustomStats.LAUNCH);
        LayerItem layer = (LayerItem) beyblade.getItem();
        BeybladeEntity beybladeEntity = new BeybladeEntity(level, beyblade.copy(), this);

        Vector3d position = new Vector3d(player.position().x,
                player.position().y,
                player.position().z);
        position = position.add(getOffset(player));

        beybladeEntity.moveTo(position.x, position.y, position.z, (float) (player.yRot + layer.getRotationDirection(beyblade).getValue() * Math.toRadians(-45)), 0);
        level.addFreshEntity(beybladeEntity);
        beybladeEntity.setDeltaMovement(getLaunchImpulse(player));
        beyblade.shrink(1);
        HUPlayer.getCap(player).setAnimation(getAnimation(getHandSide(hand,player)), ANIMATIONS_FILE, false);
        return beybladeEntity;
    }

    private Vector3d getOffset(PlayerEntity player) {
        Vector3d offset = new Vector3d(0,0,1).yRot((float) Math.toRadians(player.yRot));
        return new Vector3d(0,1.5,0).add(-offset.x, offset.y, offset.z);
    }

    public void moveBeyblade(BeybladeEntity beyblade){
        LayerItem layer = (LayerItem) beyblade.getStack().getItem();
        int dir = layer.getRotationDirection(beyblade.getStack()).getValue();
        Vector3d stadiumCenter = beyblade.findStadiumCenter();
        beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
        Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
        double speed = layer.getSpeed(beyblade.getStack());
        beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(distanceToCenter
                .multiply(getReduction() * layer.getRadiusReduction(beyblade.getStack()), 0, getReduction() * layer.getRadiusReduction(beyblade.getStack())))
                .add(distanceToCenter
                        .multiply(getSpeed()*speed/15, 0, getSpeed()*speed/15).yRot((float) (Math.toRadians(-90) * dir))));
    }

    protected double getSpeed(){
        return 0.08;
    }

    protected double getReduction(){
        return 0.025;
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

    public void onAttack(BeybladeEntity attacker, BeybladeEntity entity){
        if (attacker.getRandom().nextInt(10) == 0) {
            double x = (attacker.getX() - entity.getX()) / 2;
            double y = (attacker.getY() - entity.getY()) / 2;
            double z = (attacker.getZ() - entity.getZ()) / 2;
            ((ServerWorld) attacker.level).sendParticles(ParticleTypes.SWEEP_ATTACK, attacker.getX(), attacker.getY(), attacker.getZ(), 1, x, y, z,
                    1);
            entity.hurt(DamageSource.mobAttack(attacker), (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE));

            if (attacker.getRandom().nextInt(20) == 0)
                attacker.hurt(DamageSource.mobAttack((BeybladeEntity) entity), (float) ((BeybladeEntity) entity).getAttributeValue(Attributes.ATTACK_DAMAGE));
        }}
}
