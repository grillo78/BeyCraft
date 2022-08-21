package ga.beycraft.common.entity;

import ga.beycraft.common.block.ModBlocks;
import ga.beycraft.common.block.StadiumBlock;
import ga.beycraft.common.item.BeyPartItem;
import ga.beycraft.common.item.LayerItem;
import ga.beycraft.common.launch.Launch;
import ga.beycraft.common.launch.LaunchType;
import ga.beycraft.common.particle.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.heroesunited.heroesunited.common.abilities.AbilityHelper;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BeybladeEntity extends CreatureEntity implements IEntityAdditionalSpawnData {

    private ItemStack beyblade = ItemStack.EMPTY;
    private boolean dropped = false;
    private float spinAngle = 0;
    private float spinAngleO = 0;
    private static final DataParameter<Float> ENERGY = EntityDataManager.defineId(BeybladeEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> STOPPED = EntityDataManager.defineId(BeybladeEntity.class, DataSerializers.BOOLEAN);
    private LayerItem layer;
    private Launch launch;
    private Vector3d[] points = new Vector3d[10];

    public BeybladeEntity(World level, ItemStack beyblade, Launch launch) {
        this(ModEntities.BEYBLADE, level);
        this.beyblade = beyblade;
        layer = ((LayerItem) beyblade.getItem());
        AbilityHelper.setAttribute(this, "defense", Attributes.MAX_HEALTH, UUID.randomUUID(), (layer.getDefense(beyblade) + 1) * 200 - (layer.getBurst(beyblade) + 1) * 20, AttributeModifier.Operation.ADDITION);
        AbilityHelper.setAttribute(this, "attack", Attributes.ATTACK_DAMAGE, UUID.randomUUID(), (layer.getAttack(beyblade) + 1) * 15, AttributeModifier.Operation.ADDITION);
        setHealth(getMaxHealth());
        this.launch = launch;
    }

    BeybladeEntity(EntityType<? extends CreatureEntity> type, World level) {
        super(type, level);
        this.maxUpStep = 0.1F;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeItemStack(beyblade, true);
        CompoundNBT launchCompound = new CompoundNBT();
        launchCompound.putString("launchType", launch.getLaunchType().getRegistryName().toString());
        launchCompound.put("launchNBT", launch.serializeNBT());
        buffer.writeNbt(launchCompound);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        beyblade = additionalData.readItem();
        layer = ((LayerItem) beyblade.getItem());

        CompoundNBT launchCompound = additionalData.readAnySizeNbt();
        launch = LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(launchCompound.getString("launchType"))).generateLaunch();
        launch.deserializeNBT(launchCompound.getCompound("launchNBT"));
    }

    @Override
    public ActionResultType interactAt(PlayerEntity p_184199_1_, Vector3d p_184199_2_, Hand p_184199_3_) {
        if (!dropped) {
            dropped = true;
            ItemEntity entity = new ItemEntity(level, position().x, position().y, position().z, beyblade);
            level.addFreshEntity(entity);
            this.remove();
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        if (layer != null) {
            if (!level.isClientSide) {
                BlockPos pos = new BlockPos(position().add(0D, -0.001D, 0D));
                if ((level.getBlockState(pos).getBlock() != ModBlocks.STADIUM && level.getBlockState(pos).getBlock() != Blocks.AIR) || getEnergy() < 0)
                    setStopped(true);
                if (!isStopped() && onGround) {
                    setEnergy((float) (getEnergy() - 0.01 * layer.getFriction(beyblade)));
                    launch.moveBeyblade(this);
                }
            } else
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    this.clientTick();
                });
        }
    }

    private void setStopped(boolean stopped) {
        this.entityData.set(STOPPED, stopped);
    }

    public boolean isStopped() {
        return this.entityData.get(STOPPED);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ENERGY, 100f);
        this.entityData.define(STOPPED, false);
        super.defineSynchedData();
    }

    @Override
    protected void doPush(Entity entity) {
        if (!level.isClientSide && entity instanceof BeybladeEntity) {
            setEnergy((float) (getEnergy() - ((BeybladeEntity) entity).getAttributeValue(Attributes.ATTACK_DAMAGE) / 30));
            launch.onAttack(this, (BeybladeEntity) entity);
        }
        super.doPush(entity);
    }

    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
        double multiplier = 0;
        if (getEnergy() > 0) {
            multiplier = 5 + random.nextInt(5) + random.nextDouble();
        }
        super.push(p_70024_1_ * multiplier, p_70024_3_ * multiplier, p_70024_5_ * multiplier);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public void setEnergy(float energy) {
        this.entityData.set(ENERGY, Float.valueOf(energy));
    }

    public Float getEnergy() {
        return entityData.get(ENERGY);
    }

    public Vector3d findStadiumCenter() {
        AtomicReference<Vector3d> center = new AtomicReference<>(new Vector3d(0.5, 0, 0.5));

        BlockPos.betweenClosedStream(blockPosition().north().east(), blockPosition().south().west()).forEach(blockPos -> {
            BlockState state = level.getBlockState(blockPos);
            if (state.getBlock() == ModBlocks.STADIUM && state.getValue(StadiumBlock.PART) == StadiumBlock.EnumPartType.MIDDLECENTER) {
                center.set(center.get().add(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }
        });

        return center.get();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientTick() {
        this.spinAngleO = this.spinAngle;
        if (!isStopped()) {
            this.spinAngle += ((LayerItem) beyblade.getItem()).getRotationDirection(beyblade).getValue() * 30;
        }

        List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.1));
        for (Entity entity : list) {
            if (entity instanceof BeybladeEntity) {
                Vector3d distanceToEnemy = entity.position().add(position().reverse());
                Vector3d movement = distanceToEnemy.multiply(0.1, 0.1, 0.1).yRot(90);
                Vector3d position = getPosition(Minecraft.getInstance().getFrameTime()).add(distanceToEnemy.multiply(0.5, 0.5, 0.5)).add(0, 0.09, 0);
                for (int i = 0; i < 25; i++) {
                    level.addParticle(ModParticles.SPARKLE, position.x, position.y, position.z, movement.x, movement.y, movement.z);
                }
            }
        }
        Vector3d position = getPosition(Minecraft.getInstance().getFrameTime()).add(0,0.05,0);
        for (int i = 0; i < 25; i++) {
            level.addParticle(ModParticles.RESONANCE, position.x, position.y, position.z, 0, random.nextDouble()*0.02, 0);
            level.addParticle(ModParticles.RESONANCE, position.x, position.y, position.z, 0, random.nextDouble()*0.02, 0);
        }
        if (onGround)
            updatePoints(this);
    }

    public Launch getLaunch() {
        return launch;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d[] getPoints() {
        return points;
    }

    @OnlyIn(Dist.CLIENT)
    public void updatePoints(BeybladeEntity entity) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null) {
                if (i != points.length - 1) {
                    points[i] = points[i + 1];
                } else {
                    points[i] = entity.getPosition(Minecraft.getInstance().getFrameTime());
                }
            } else {
                points[i] = entity.position();
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 10000;
    }

    public float getSpinAngleO() {
        return spinAngleO;
    }

    public float getSpinAngle() {
        return spinAngle;
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        super.die(p_70645_1_);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            for (int i = 0; i < cap.getSlots(); i++) {
                ItemStack stack = cap.getStackInSlot(i);
                if (((BeyPartItem) stack.getItem()).canDropOnBurst()) {
                    ItemEntity entity = new ItemEntity(level, position().x, position().y, position().z, stack.copy());
                    stack.shrink(1);
                    level.addFreshEntity(entity);
                }
            }
        });
        ItemEntity entity = new ItemEntity(level, position().x, position().y, position().z, beyblade);
        level.addFreshEntity(entity);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        beyblade = ItemStack.of(compound.getCompound("beyblade"));
        layer = ((LayerItem) beyblade.getItem());
        CompoundNBT launchCompound = compound.getCompound("launch");
        launch = LaunchType.LAUNCH_TYPES.getValue(new ResourceLocation(launchCompound.getString("launchType"))).generateLaunch();
        launch.deserializeNBT(launchCompound.getCompound("launchNBT"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.put("beyblade", beyblade.save(new CompoundNBT()));
        CompoundNBT launchCompound = new CompoundNBT();
        launchCompound.putString("launchType", launch.getLaunchType().getRegistryName().toString());
        launchCompound.put("launchNBT", launch.serializeNBT());
        compound.put("launch", launchCompound);
    }

    public static AttributeModifierMap.MutableAttribute registerMonsterAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 1).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MAX_HEALTH, 1).add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
    }

    public ItemStack getStack() {
        return beyblade;
    }
}
