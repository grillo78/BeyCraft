package com.beycraft.common.launch;

import com.beycraft.common.capability.entity.Blader;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.beycraft.common.entity.BeybladeEntity;
import com.beycraft.common.item.LayerItem;
import com.beycraft.common.stats.CustomStats;
import com.beycraft.network.PacketHandler;
import com.beycraft.network.message.MessageApplyAnimation;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class    Launch {

    protected LaunchType launchType;
    protected boolean activated = false;
    private static final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public Launch(LaunchType launchType) {
        this.launchType = launchType;
    }

    public Launch() {
        this.launchType = LaunchTypes.BASIC_LAUNCH_TYPE;
    }

    public void activate() {
        activated = true;
    }

    public BeybladeEntity launchBeyblade(ItemStack beyblade, World level, ServerPlayerEntity player, Hand hand) {
        Blader blader = player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);
        AtomicReference<BeybladeEntity> beybladeEntity = new AtomicReference<>();
        if (!blader.isLaunching()) {
            if(launchType != LaunchTypes.HAND_LAUNCH_TYPE)
                PacketHandler.INSTANCE.sendTo(new MessageApplyAnimation(player.getUUID(), hand), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);

            blader.setLaunching(true);
            blader.getBladerLevel().increaseExperience(0.01F);
            blader.syncToAll();
            threadPool.schedule(() -> {
                player.awardStat(CustomStats.LAUNCH);
                LayerItem layer = (LayerItem) beyblade.getItem();
                beybladeEntity.set(new BeybladeEntity(level, beyblade.copy(), this, player.getUUID()));
                Vector3d position = new Vector3d(player.position().x,
                        player.position().y,
                        player.position().z);
                position = position.add(getOffset(player));

                beybladeEntity.get().setEnergy((float) (99+Math.pow(2,blader.getBladerLevel().getLevel()/1000F)));

                beybladeEntity.get().moveTo(position.x, position.y, position.z, (float) (player.yRot + layer.getRotationDirection(beyblade).getValue() * Math.toRadians(-45)), 0);
                level.addFreshEntity(beybladeEntity.get());
                beybladeEntity.get().setDeltaMovement(getLaunchImpulse(player));
                beyblade.shrink(1);
                blader.setLaunching(false);
                blader.syncToAll();
            }, 3250, TimeUnit.MILLISECONDS);
        }
        return beybladeEntity.get();
    }

    protected Vector3d getOffset(PlayerEntity player) {
        Vector3d offset = new Vector3d(0, 0, 0.75).yRot((float) Math.toRadians(player.yRot));
        return new Vector3d(0, 1.25, 0).add(-offset.x, offset.y, offset.z);
    }

    public void moveBeyblade(BeybladeEntity beyblade) {
        LayerItem layer = (LayerItem) beyblade.getStack().getItem();
        int dir = layer.getRotationDirection(beyblade.getStack()).getValue();
        Vector3d stadiumCenter = beyblade.findStadiumCenter();
        beyblade.lookAt(EntityAnchorArgument.Type.EYES, stadiumCenter);
        Vector3d distanceToCenter = stadiumCenter.add(beyblade.position().reverse());
        double speed = layer.getSpeed(beyblade.getStack());
        beyblade.setDeltaMovement(beyblade.getDeltaMovement().add(distanceToCenter
                .multiply(getReduction() * layer.getRadiusReduction(beyblade.getStack()), 0, getReduction() * layer.getRadiusReduction(beyblade.getStack())))
                .add(distanceToCenter
                        .multiply(getSpeed() * speed / 15, 0, getSpeed() * speed / 15).yRot((float) (Math.toRadians(-90) * dir))));
        if (activated && distanceToCenter.length() < 0.15) {
            activated = false;
        }
    }

    protected double getSpeed() {
        return 0.08;
    }

    protected double getReduction() {
        return activated ? 0.1 : 0.025;
    }

    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    private Vector3d getLaunchImpulse(PlayerEntity player) {
        return Vector3d.ZERO;
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void deserializeNBT(CompoundNBT launchNBT) {

    }

    public void onAttack(BeybladeEntity attacker, BeybladeEntity entity) {
        if (attacker.getRandom().nextInt(attacker.getLaunch().activated?1:5) == 0) {
            double x = (attacker.getX() - entity.getX()) / 2;
            double y = (attacker.getY() - entity.getY()) / 2;
            double z = (attacker.getZ() - entity.getZ()) / 2;
            ((ServerWorld) attacker.level).sendParticles(ParticleTypes.SWEEP_ATTACK, attacker.getX(), attacker.getY(), attacker.getZ(), 1, x, y, z,
                    1);
            entity.hurt(DamageSource.mobAttack(attacker), (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE));
            entity.setDeltaMovement(attacker.position().subtract(entity.position()).normalize().multiply(0.45,0,0.45).add(0,0.3,0));

            if (attacker.getRandom().nextBoolean()) {
                attacker.hurt(DamageSource.mobAttack(entity), (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE));
                attacker.setDeltaMovement(entity.position().subtract(attacker.position()).normalize().multiply(0.45,0,0.45).add(0,0.3,0));
            }
        }
        if (shouldDeactivate())
            activated = false;
    }

    protected boolean shouldDeactivate() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderTick(BeybladeEntity beyblade) {
    }
}
