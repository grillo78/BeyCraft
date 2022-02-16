package ga.beycraft.entity;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Absorb;
import ga.beycraft.abilities.SpringAttack;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.items.ItemBeyDisc;
import ga.beycraft.items.ItemBeyDriver;
import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageSyncBladerLevel;
import ga.beycraft.util.ConfigManager;
import ga.beycraft.util.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author grillo78
 */
public class EntityBey extends CreatureEntity implements IEntityAdditionalSpawnData {

    private ItemStackHandler inventory = new ItemStackHandler(3);
    private int rotationDirection;
    private int rotationStartedCount = 0;
    private boolean rotationStarted = false;
    private boolean pickedUp = false;
    private static final DataParameter<Float> BLADERLEVEL = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> MAXROTATIONSPEED = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> RADIUS0 = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> RADIUS = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Boolean> HORIZONTALCOLLISION = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.BOOLEAN);
    private String playerName;
    private static final DataParameter<Float> ANGLE = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> ANGLE0 = EntityDataManager.defineId(EntityBey.class,
            DataSerializers.FLOAT);
    private boolean increaseRadius = false;
    private boolean stopped = false;
    private float maxRadius;
    private int tickCount = 0;
    private Lightning[] lightnings;

    private Vector3d[] points = new Vector3d[5];

    /**
     * @param type
     * @param world
     */
    public EntityBey(EntityType<? extends EntityBey> type, World world) {
        this(type, world, ItemStack.EMPTY, 1, null, 0);
    }

    public EntityBey(EntityType<? extends EntityBey> type, World world, ItemStack layer, int rotationDirection,
                     String playerName, int bladerLevel) {
        super(type, world);
        this.rotationDirection = rotationDirection;
        if (layer.hasTag()) {
            this.inventory.setStackInSlot(0, layer);
            this.inventory.setStackInSlot(1, ItemStack.of(layer.getTag().getCompound("disc")));
            this.inventory.setStackInSlot(2, ItemStack.of(layer.getTag().getCompound("driver")));
        }
        this.playerName = playerName;
        setBladerlevel(bladerLevel);
        if (!world.isClientSide) {
            this.setMaxRotationSpeed(7);
            this.setRotationSpeed(getMaxRotationSpeed());
        }
        maxRadius = 1.6F * getMaxRotationSpeed() / 15;
        this.setRadius(maxRadius);
        maxUpStep = 0.1F;

    }

    public Lightning[] getLightnings() {
        return lightnings;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 74.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        return p_70112_1_ < 4000;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return BeyCraftRegistry.HITSOUND;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
    }

    public float getMaxRadius() {
        return maxRadius;
    }

    private void dropItems() {
        BlockPos pos = null;
        for (PlayerEntity player : level.players()) {
            if (playerName.equals(player)) {
                pos = new BlockPos(player.getX(), player.getY(), player.getZ());
            }
        }
        inventory.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            h.getStackInSlot(0).shrink(1);

            h.getStackInSlot(1).shrink(1);

        });
        inventory.getStackInSlot(0).getTag().put("disc", ItemStack.EMPTY.save(new CompoundNBT()));
        inventory.getStackInSlot(0).getTag().put("driver", ItemStack.EMPTY.save(new CompoundNBT()));
        if (pos == null) {
            spawnAtLocation(inventory.getStackInSlot(0));
            spawnAtLocation(inventory.getStackInSlot(1));
            spawnAtLocation(inventory.getStackInSlot(2));
        } else {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(0)));
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(1)));
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(2)));
        }
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    private void dropItem(PlayerEntity player) {
        pickedUp = true;
        inventory.getStackInSlot(0).getTag().putBoolean("isEntity", false);
        level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
                inventory.getStackInSlot(0)));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ROTATIONSPEED, 1f);
        this.entityData.define(MAXROTATIONSPEED, 1f);
        this.entityData.define(BLADERLEVEL, 1f);
        this.entityData.define(HORIZONTALCOLLISION, false);
        this.entityData.define(RADIUS0, 1.6F);
        this.entityData.define(RADIUS, 1.6F);
        this.entityData.define(ANGLE, 0F);
        this.entityData.define(ANGLE0, 0F);
        super.defineSynchedData();
    }

    @Override
    public void die(DamageSource cause) {
        dropItems();
        remove();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        rotationDirection = nbt.getInt("RotationDirection");
        setRotationSpeed(nbt.getFloat("RotationSpeed"));
        setMaxRotationSpeed(nbt.getFloat("MaxRotationSpeed"));
        setBladerlevel(nbt.getFloat("BladerLevel"));
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        maxRadius = nbt.getFloat("MaxRadius");
        playerName = nbt.getString("PlayerName");
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("RotationDirection", rotationDirection);
        compound.putFloat("MaxRotationSpeed", getMaxRotationSpeed());
        compound.putFloat("RotationSpeed", getRotationSpeed());
        compound.putFloat("MaxRadius", maxRadius);
        compound.putString("PlayerName", playerName);
        compound.putFloat("BladerLevel", getBladerLevel());
        return compound;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        for (int i = 0; i < 3; i++) {
            buffer.writeItem(inventory.getStackInSlot(i));
        }
        buffer.writeInt(rotationDirection);
        buffer.writeUtf(playerName);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        for (int i = 0; i < 3; i++) {
            this.inventory.setStackInSlot(i, additionalData.readItem());
        }
        rotationDirection = additionalData.readInt();
        playerName = additionalData.readUtf();
        ItemBeyLayer layerItem = ((ItemBeyLayer) getLayer().getItem());
        this.lightnings = new Lightning[]{new Lightning(layerItem.getResonanceColor(getLayer())), new Lightning(layerItem.getSecondResonanceColor(getLayer())), new Lightning(layerItem.getThirdResonanceColor(getLayer())), new Lightning(layerItem.getResonanceColor(getLayer())), new Lightning(layerItem.getSecondResonanceColor(getLayer())), new Lightning(layerItem.getThirdResonanceColor(getLayer()))};
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("RotationDirection", rotationDirection);
        compound.putFloat("MaxRotationSpeed", getMaxRotationSpeed());
        compound.putFloat("RotationSpeed", getRotationSpeed());
        compound.putFloat("Radius", getRadius());
        compound.putFloat("MaxRadius", maxRadius);
        compound.putString("PlayerName", playerName);
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        rotationDirection = compound.getInt("RotationDirection");
        setRotationSpeed(compound.getFloat("RotationSpeed"));
        setMaxRotationSpeed(compound.getFloat("MaxRotationSpeed"));
        inventory.deserializeNBT(compound.getCompound("inventory"));
        setRadius(compound.getFloat("Radius"));
        maxRadius = compound.getFloat("MaxRadius");
        playerName = compound.getString("PlayerName");
        super.readAdditionalSaveData(compound);
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        if (!level.isClientSide && !player.isSpectator() && isAlive()) {
            if (hand == Hand.MAIN_HAND) {
                dropItem(player);
                this.remove();
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactAt(player, vec, hand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EntityGoalRotate(this));
        super.registerGoals();
    }

    @OnlyIn(Dist.CLIENT)
    public void updatePoints(EntityBey entity) {
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

    public Vector3d[] getPoints() {
        return points;
    }


    public void setRotationStarted(boolean rotationStarted) {
        this.rotationStarted = rotationStarted;
    }

    public boolean isRotationStarted() {
        return rotationStarted;
    }

    @Override
    public void tick() {
        if (!onGround || (getLayer().getItem() instanceof ItemBeyLayer
                && getDisc().getItem() instanceof ItemBeyDisc
                && getDriver().getItem() instanceof ItemBeyDriver)) {
            if (this.getRotationSpeed() > 0
                    && (level.getBlockState(this.getOnPos()).getBlock() == Blocks.AIR
                    || (level.getBlockState(new BlockPos(position().x,
                    position().y - 0.1, position().z)).getBlock() == BeyCraftRegistry.STADIUM
                    || (!ConfigManager.isOnlyStadium() && (!ConfigManager.getBlockBlackList()
                    .contains(level
                            .getBlockState(new BlockPos(position().x,
                                    position().y - 0.1, position().z))
                            .getBlock())))))) {

                if (!rotationStarted) {
                    rotationStartedCount++;
                    if (rotationStartedCount == 4) {
                        rotationStarted = true;
                    }
                } else {
                    setRotationSpeed(getRotationSpeed()
                            - 0.005F * ((ItemBeyDriver) getDriver().getItem()).getFriction(getDriver())
                            / (10 * (1 + ((ItemBeyDisc) getDisc().getItem()).getWeight() + ((ItemBeyLayer) getLayer().getItem()).getWeight(getLayer()))));
                    if (!level.isClientSide) {
                        float angleIncrementation = (getRotationSpeed() * 30 * -rotationDirection);
                        setAngle(getAngle() + angleIncrementation);
                        if (((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() != null)
                            ((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility().executeAbility(this);
                        if (((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() != null)
                            ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility().executeAbility(this);

                        if (((ItemBeyDisc) getDisc().getItem()).getPrimaryAbility() != null)
                            ((ItemBeyDisc) getDisc().getItem()).getPrimaryAbility().executeAbility(this);
                        if (((ItemBeyDisc) getDisc().getItem()).getSecundaryAbility() != null)
                            ((ItemBeyDisc) getDisc().getItem()).getSecundaryAbility().executeAbility(this);

                        if (((ItemBeyDriver) getDriver().getItem()).getPrimaryAbility() != null)
                            ((ItemBeyDriver) getDriver().getItem()).getPrimaryAbility().executeAbility(this);
                        if (((ItemBeyDriver) getDriver().getItem()).getSecundaryAbility() != null)
                            ((ItemBeyDriver) getDriver().getItem()).getSecundaryAbility().executeAbility(this);
                    }
                }
            } else {
                if (!stopped) {
                    stopped = true;
                    setRotationSpeed(0);
                }
            }
        }
        if (isHorizontalCollision() && !isStopped()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(BeyCraftRegistry.SPARKLE, getX() + getDimensions(Pose.STANDING).width / 2, getY() + 0.5, getZ() + getDimensions(Pose.STANDING).width / 2, random.nextInt(5),
                        random.nextInt(5), random.nextInt(5));
            }
        }
        tickCount++;
        if (level.isClientSide) {
            updatePoints(this);
            PlayerEntity player = PlayerUtils.getPlayerByName(playerName, level);
            if (player != null && getRotationSpeed() != 0 && tickCount % 5 == 0)
                player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                    if (h.isInResonance()) {
                        if (lightnings != null) {
                            for (int i = 0; i < lightnings.length; i++) {
                                if (lightnings[i] != null)
                                    lightnings[i].tick();
                            }
                        }
                        for (int i = 0; i < 100; i++)
                            level.addParticle(BeyCraftRegistry.RESONANCE_BEY, getX(), getY(), getZ(), 0,
                                    0, 0);

                        if (!player.isSpectator())
                            for (int i = 0; i < 100; i++)
                                level.addParticle(BeyCraftRegistry.RESONANCE_PLAYER, getX(), getY(), getZ(), 0,
                                        0, 0);
                    }
                });
        } else {
            PlayerEntity player = PlayerUtils.getPlayerByName(playerName, level);
            if (player != null && !isStopped())
                player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
                    boolean wasInResonance = h.isInResonance();
                    h.increaseResonanceCount();
                    if (wasInResonance && !h.isInResonance())
                        PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience(), h.isInResonance(), false, player.getId()),
                                ((ServerPlayerEntity) PlayerUtils.getPlayerByName(playerName, level)).connection.getConnection(),
                                NetworkDirection.PLAY_TO_CLIENT);
                });

        }
        super.tick();
    }

    public int getTickCount() {
        return tickCount;
    }

    @Override
    public void travel(Vector3d p_213352_1_) {
        super.travel(p_213352_1_);
        if (horizontalCollision) {
            yRot += -90 * rotationDirection;
        }
        setHorizontalCollision(horizontalCollision);
    }

    @Override
    protected void doPush(Entity entityIn) {
        if (!level.isClientSide) {
            if (!stopped && entityIn instanceof EntityBey) {
                EntityBey bey = (EntityBey) entityIn;
                playHurtSound(DamageSource.GENERIC);
                double x = (getX() - entityIn.getX()) / 2;
                double y = (getY() - entityIn.getY()) / 2;
                double z = (getZ() - entityIn.getZ()) / 2;
                ((ServerWorld) level).sendParticles(BeyCraftRegistry.SPARKLE, getX(), getY(), getZ(), 10, x, y, z,
                        10);
                ((ServerWorld) level).sendParticles(ParticleTypes.SWEEP_ATTACK, getX(), getY(), getZ(), 1, x, y, z,
                        1);
                if (!((EntityBey) entityIn).isStopped()) {
                    if (((((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() instanceof Absorb
                            || ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() instanceof Absorb
                            || ((ItemBeyDisc) getDisc().getItem()).getPrimaryAbility() instanceof Absorb
                            || ((ItemBeyDisc) getDisc().getItem()).getSecundaryAbility() instanceof Absorb) &&
                            !(((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getPrimaryAbility() instanceof Absorb
                                    || ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getSecundaryAbility() instanceof Absorb
                                    || ((ItemBeyDisc) ((EntityBey) entityIn).getDisc().getItem()).getPrimaryAbility() instanceof Absorb
                                    || ((ItemBeyDisc) ((EntityBey) entityIn).getDisc().getItem()).getSecundaryAbility() instanceof Absorb))) {
                        setRotationSpeed(getRotationSpeed()
                                + (((EntityBey) entityIn).getPartsAttack()
                                + getPartsDefense()) / 100);
                        bey.setRotationSpeed(bey.getRotationSpeed()
                                - (bey.getPartsAttack()
                                + getPartsDefense()
                                + bey.getBladerLevel()) / getBladerLevel());
                        if (getRotationSpeed() > getMaxRotationSpeed()) {
                            setRotationSpeed(getMaxRotationSpeed());
                        } else {
                            if (bey.getRotationSpeed() > getRotationSpeed() && random.nextInt(5) == 1) {
                                float damage = bey.getHealth();
                                bey.hurt(DamageSource.mobAttack(this), damage);
                            }
                        }
                        if (random.nextInt(3) == 1 || ((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() instanceof SpringAttack
                                || ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() instanceof SpringAttack) {
                            entityIn.hurt(DamageSource.mobAttack(this),
                                    (int) (getPartsAttack()
                                            + ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getBurst(((EntityBey) entityIn).getLayer())
                                            - ((EntityBey) entityIn).getPartsDefense()
                                            + getBladerLevel() - ((EntityBey) entityIn).getBladerLevel()));
                        }
                    } else {
                        setRotationSpeed(getRotationSpeed()
                                - (((EntityBey) entityIn).getPartsAttack()
                                + getPartsDefense()
                                + ((EntityBey) entityIn).getBladerLevel()) / getBladerLevel());
                        if (random.nextInt(3) == 1 || ((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() instanceof SpringAttack
                                || ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() instanceof SpringAttack) {
                            entityIn.hurt(DamageSource.mobAttack(this),
                                    (int) (getPartsAttack()
                                            + ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getBurst(((EntityBey) entityIn).getLayer())
                                            - ((EntityBey) entityIn).getPartsDefense()
                                            + getBladerLevel() - ((EntityBey) entityIn).getBladerLevel()));
                        }
                    }
                    if (getRadius() == 0 && random.nextInt(5) == 1) {
                        increaseRadius = true;
                        if (random.nextInt(50) == 1 || ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getPrimaryAbility() instanceof SpringAttack
                                || ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getSecundaryAbility() instanceof SpringAttack) {
                            entityIn.hurt(DamageSource.mobAttack(this),
                                    (getPartsAttack()
                                            + ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getBurst(((EntityBey) entityIn).getLayer())
                                            - ((EntityBey) entityIn).getPartsDefense()
                                            + getBladerLevel() - ((EntityBey) entityIn).getBladerLevel()));
                        } else {
                            entityIn.hurt(DamageSource.GENERIC,
                                    (getPartsAttack()
                                            + ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getBurst(((EntityBey) entityIn).getLayer())
                                            - ((EntityBey) entityIn).getPartsDefense()
                                            + getBladerLevel() - ((EntityBey) entityIn).getBladerLevel()));
                        }
                    } else {
                        entityIn.hurt(DamageSource.GENERIC,
                                (getPartsAttack()
                                        + ((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getBurst(((EntityBey) entityIn).getLayer()) * 4
                                        - ((EntityBey) entityIn).getPartsDefense()
                                        + ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
                    }
                }

            }
        }
        super.doPush(entityIn);
    }

    public float getResonanceMultiplier() {
        AtomicReference<Float> resonanceMultiplier = new AtomicReference<>((float) 1);
        PlayerEntity player = PlayerUtils.getPlayerByName(playerName, level);
        if (player != null)
            player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(bladerLevel -> {
                if (bladerLevel.isInResonance())
                    resonanceMultiplier.set((float) (bladerLevel.getBladerLevel()) / 10F);
            });
        return resonanceMultiplier.get();
    }

    public float getPartsAttack() {
        return (((ItemBeyLayer) getLayer().getItem()).getAttack(getLayer()) + ((ItemBeyDisc) getDisc().getItem()).getAttack(getDisc())) * getResonanceMultiplier();
    }

    public float getPartsDefense() {
        return (((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer()) + ((ItemBeyDisc) getDisc().getItem()).getDefense(getDisc())) * getResonanceMultiplier();
    }

    /**
     * @return the increaseRadius
     */
    public boolean isIncreaseRadius() {
        return increaseRadius;
    }

    /**
     * @param increaseRadius the increaseRadius to set
     */
    public void setIncreaseRadius(boolean increaseRadius) {
        this.increaseRadius = increaseRadius;
    }

    /**
     * @return the inventory
     */
    public ItemStackHandler getInventory() {
        return inventory;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof MobEntity);
        boolean flag1 = !(this.getVehicle() instanceof BoatEntity);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
    }

    public ItemStack getLayer() {
        return inventory.getStackInSlot(0);
    }

    public void setLayer(ItemStack layer) {
        inventory.setStackInSlot(0, layer);
    }

    public ItemStack getDisc() {
        return inventory.getStackInSlot(1);
    }

    public void setDisk(ItemStack disk) {
        inventory.setStackInSlot(1, disk);
    }

    public ItemStack getDriver() {
        return inventory.getStackInSlot(2);
    }

    public void setDriver(ItemStack driver) {
        inventory.setStackInSlot(2, driver);
    }

    public int getRotationDirection() {
        return rotationDirection;
    }

    public void setRotationDirection(int rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    public final boolean isHorizontalCollision() {
        return (Boolean) this.entityData.get(HORIZONTALCOLLISION);
    }

    public void setHorizontalCollision(boolean horizontalCollision) {
        this.entityData.set(HORIZONTALCOLLISION, horizontalCollision);
    }

    public final float getRadius() {
        return (Float) this.entityData.get(RADIUS);
    }

    public final float getRadius0() {
        return (Float) this.entityData.get(RADIUS0);
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS0, getRadius());
        this.entityData.set(RADIUS, radius);
    }

    public final float getAngle() {
        return (Float) this.entityData.get(ANGLE);
    }

    public final float getAngle0() {
        return (Float) this.entityData.get(ANGLE0);
    }

    public void setAngle(float angle) {
        this.entityData.set(ANGLE0, getAngle());
        this.entityData.set(ANGLE, angle);
    }

    public final float getBladerLevel() {
        return (Float) this.entityData.get(BLADERLEVEL);
    }

    public void setBladerlevel(float bladerLevel) {
        this.entityData.set(BLADERLEVEL, bladerLevel);
    }

    public void setRotationSpeed(float speed) {
        this.entityData.set(ROTATIONSPEED, Float.valueOf(speed));
    }

    public final float getRotationSpeed() {
        return (Float) this.entityData.get(ROTATIONSPEED);
    }

    public void setMaxRotationSpeed(float speed) {
        this.entityData.set(MAXROTATIONSPEED, Float.valueOf(speed));
    }

    public final float getMaxRotationSpeed() {
        return (Float) this.entityData.get(MAXROTATIONSPEED);
    }

    public boolean isStopped() {
        return stopped;
    }

    public static class Lightning {
        private int seed = new Random().nextInt(100);
        private float red;
        private float green;
        private float blue;

        public Lightning(ItemBeyLayer.Color color) {
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
        }

        public int getSeed() {
            return seed;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }

        public void tick() {
            seed++;
        }
    }
}
