package grillo78.beycraft.entities;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.blocks.StadiumBlock;
import grillo78.beycraft.items.MainBeyPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class Beyblade extends LivingEntity {
    public static final float MAX_ROTATION_SPEED = 500F;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
    private static final EntityDataAccessor<ItemStack> BEYBLADE_ITEM = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> ROTATION_ANGLE = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FLOWER_PATTERN = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.BOOLEAN);

    private Vec3 petalOffset = null;
    private double maxFlowerDistanceToCenter = -1;
    private int flowerPatternAngleTick = 0;

    public Beyblade(EntityType<Beyblade> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEYBLADE_ITEM, ItemStack.EMPTY);
        builder.define(ROTATION_ANGLE, 0F);
        builder.define(FLOWER_PATTERN, false);
        builder.define(ROTATION_SPEED, MAX_ROTATION_SPEED);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.STEP_HEIGHT, 1.5 / 16F).add(Attributes.KNOCKBACK_RESISTANCE, 0.01F);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return inventory;
    }

    public ItemStack getBeybladeItem() {
        return entityData.get(BEYBLADE_ITEM);
    }

    public void setBeybladeItem(ItemStack beybladeItem) {
        entityData.set(BEYBLADE_ITEM, beybladeItem);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    public float getRotationAngle() {
        return entityData.get(ROTATION_ANGLE);
    }

    public void setRotationAngle(float rotationAngle) {
        entityData.set(ROTATION_ANGLE, rotationAngle);
    }

    public float getRotationSpeed() {
        return entityData.get(ROTATION_SPEED);
    }

    public void setRotationSpeed(float rotationSpeed) {
        entityData.set(ROTATION_SPEED, rotationSpeed);
    }

    public boolean isFlowerPattern() {
        return entityData.get(FLOWER_PATTERN) && getRotationSpeed() > 30;
    }

    public void setFlowerPattern(boolean flowerPattern) {
        entityData.set(FLOWER_PATTERN, flowerPattern);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return (getBeybladeItem().getItem() instanceof MainBeyPart) && !((MainBeyPart) getBeybladeItem().getItem()).canBurst(getBeybladeItem());
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        if (getBeybladeItem().getItem() instanceof MainBeyPart) {
            if (((MainBeyPart) getBeybladeItem().getItem()).canBurst(getBeybladeItem())) {
                ((MainBeyPart) getBeybladeItem().getItem()).die(getBeybladeItem(), level, position());
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level().isClientSide) {
            level().addFreshEntity(new ItemEntity(level(), player.position().x, player.position().y, player.position().z, getBeybladeItem()));
            remove(RemovalReason.DISCARDED);
        }
        return super.interact(player, hand);
    }

    @Override
    public @Nullable Collection<ItemEntity> captureDrops() {
        return super.captureDrops();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (getBeybladeItem().getItem() instanceof MainBeyPart) {
                setRotationAngle((getRotationSpeed() > 30 ? 30 : getRotationSpeed()) + getRotationAngle());
                if (getRotationSpeed() > 0) {
                    float friction = (((MainBeyPart) getBeybladeItem().getItem()).getFriction(getBeybladeItem()));
                    float weight = ((MainBeyPart) getBeybladeItem().getItem()).getWeight(getBeybladeItem());
                    float radiusReduction = ((MainBeyPart) getBeybladeItem().getItem()).getRadiusReduction(getBeybladeItem()) * 0.015F + 0.01F;
                    float speed = ((MainBeyPart) getBeybladeItem().getItem()).getSpeed(getBeybladeItem());
                    setRotationSpeed(getRotationSpeed() - (friction / 2 - weight / 50) / 10);
                    BlockState state = level().getBlockState(blockPosition());
                    if (onGround() && state.getBlock() instanceof StadiumBlock) {
                        if (isFlowerPattern())
                            flowerPatterLaunchMove(friction, radiusReduction, speed, weight);
                        else
                            basicLaunchMove(friction, radiusReduction, speed, weight);
                    }
                } else if (getRotationSpeed() < 0)
                    setRotationSpeed(0);
            }
        }
    }

    private void basicLaunchMove(float friction, float radiusReduction, float speed, float weight) {
        Vec3 stadiumCenter = getStadiumCenter();
        Vec3 offsetToCenter = position().subtract(stadiumCenter);
        double desiredDistanceToCenter = 1.2 * getRotationSpeed() / MAX_ROTATION_SPEED;
        desiredDistanceToCenter = Math.clamp(desiredDistanceToCenter, 0.1F, 1.2F);
        double distanceToCenter = (desiredDistanceToCenter < offsetToCenter.length() ? radiusReduction : -radiusReduction);
        distanceToCenter = offsetToCenter.length() - distanceToCenter;
        distanceToCenter = Math.clamp(distanceToCenter, 0.1F, desiredDistanceToCenter);
        if (offsetToCenter.normalize().horizontalDistance() == 0)
            offsetToCenter = offsetToCenter.add(0, 0, 0.01);
        Vec3 desiredPosition = stadiumCenter.add(offsetToCenter.normalize().yRot((float) 0.5 * speed * weight * 0.1F * friction).multiply(distanceToCenter, 1, distanceToCenter));

        move(MoverType.SELF, desiredPosition.subtract(position()));
    }

    private void flowerPatterLaunchMove(float friction, float radiusReduction, float speed, float weight) {
        float angle = 60 + (random.nextFloat()*20)-10;
        if (petalOffset == null) {
            maxFlowerDistanceToCenter = position().distanceTo(getStadiumCenter());
            petalOffset = position().subtract(getStadiumCenter()).yRot((float) Math.toRadians(-angle));
        } else if (getStadiumCenter().distanceTo(position()) > maxFlowerDistanceToCenter) {
            petalOffset = position().subtract(getStadiumCenter()).yRot((float) Math.toRadians(-angle));
            flowerPatternAngleTick = 0;
        }

        Vec3 localPosition = position().subtract(getStadiumCenter());
        Vec3 petalRadius = petalOffset.subtract(localPosition);
        Vec3 rotatedPetalRadius = petalRadius.yRot((float) Math.toRadians(speed*20));
        Vec3 movement = petalRadius.subtract(rotatedPetalRadius);

        move(MoverType.SELF, movement);
        flowerPatternAngleTick += speed * 2;
    }

    @Override
    protected void playHurtSound(DamageSource source) {

    }

    @Override
    public void push(Entity entity) {
        if (!(entity instanceof Beyblade) || getRotationSpeed() <= 0)
            super.push(entity);
        else {
            double scale = random.nextDouble() < 0.25 ? (random.nextDouble() < 0.05 ? 0.25 : 0.15) : 0.25 * (1.3 - getStadiumCenter().subtract(position()).length());
            Vec3 force = entity.position().subtract(position()).normalize().multiply(random.nextDouble() * 0.5 + 0.5, 0, random.nextDouble() * 0.5 + 0.5).scale(scale * 5).add(0, onGround() ? 0.1 : 0, 0);
            if (force.horizontalDistance()<0.001F)
                force.add(0,0,0.001);
            entity.move(MoverType.SELF, force);
            FX fx = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(Beycraft.MOD_ID,"sparking"));

            EntityEffectExecutor effectExecutor = new EntityEffectExecutor(fx, level(), entity, EntityEffectExecutor.AutoRotate.NONE);
            effectExecutor.setOffset(position().subtract(entity.position()).toVector3f());
            effectExecutor.setAllowMulti(true);
            effectExecutor.start();
            move(MoverType.SELF, force.multiply(-1, 1, -1));
            boolean canEnemyBurst = (((MainBeyPart) ((Beyblade) entity).getBeybladeItem().getItem()).canBurst(((Beyblade) entity).getBeybladeItem()));
            boolean canBurst = (((MainBeyPart) getBeybladeItem().getItem()).canBurst(getBeybladeItem()));
            boolean canEnemyAbsorb = (((MainBeyPart) ((Beyblade) entity).getBeybladeItem().getItem()).canAbsorb(((Beyblade) entity).getBeybladeItem()));
            boolean canAbsorb = (((MainBeyPart) getBeybladeItem().getItem()).canAbsorb(getBeybladeItem()));
            float attack = (((MainBeyPart) getBeybladeItem().getItem()).getAttack(getBeybladeItem()));
            float enemyAttack = (((MainBeyPart) ((Beyblade) entity).getBeybladeItem().getItem()).getAttack(((Beyblade) entity).getBeybladeItem()));
            float defense = (((MainBeyPart) ((Beyblade) entity).getBeybladeItem().getItem()).getDefense(((Beyblade) entity).getBeybladeItem()));
            float enemyDefense = (((MainBeyPart) getBeybladeItem().getItem()).getDefense(getBeybladeItem()));
            float damageScale = 10;
            if (canEnemyAbsorb)
                ((Beyblade) entity).setRotationSpeed(((Beyblade) entity).getRotationSpeed() + attack * damageScale);
            else {
                float damage = (attack - defense) * damageScale;
                if (damage <= 0)
                    damage = 1;
                ((Beyblade) entity).setRotationSpeed(((Beyblade) entity).getRotationSpeed() - damage);
            }
            if (random.nextDouble() < 0.5) {
                if (canAbsorb)
                    setRotationSpeed(((Beyblade) entity).getRotationSpeed() + enemyAttack * damageScale);
                else {
                    float damage = (enemyAttack - enemyDefense) * damageScale;
                    if (damage <= 0)
                        damage = 1;
                    ((Beyblade) entity).setRotationSpeed(((Beyblade) entity).getRotationSpeed() - damage);
                    ((Beyblade) entity).playSound(getHurtSound(damageSources().generic()));
                }
            }
            if (canEnemyBurst) {
                float burtsResistance = (((MainBeyPart) ((Beyblade) entity).getBeybladeItem().getItem()).getBurstResistance(((Beyblade) entity).getBeybladeItem()));
                entity.hurt(damageSources().generic(), (attack - defense) * (1 - burtsResistance));
            }
            if (canBurst) {
                float burtsResistance = (((MainBeyPart) getBeybladeItem().getItem()).getBurstResistance(getBeybladeItem()));
                entity.hurt(damageSources().generic(), (attack - defense) * (1 - burtsResistance));
            }
        }
    }

    public Vec3 getStadiumCenter() {
        BlockState state = level().getBlockState(getOnPos());
        BlockPos pos = getOnPos();
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.15313, pos.getZ() + 0.5);

        if (state.getBlock() instanceof StadiumBlock)
            switch (state.getValue(StadiumBlock.PART)) {
                case TOPLEFT:
                    center = center.add(-1, 0, 1);
                    break;
                case TOPCENTER:
                    center = center.add(-1, 0, 0);
                    break;
                case TOPRIGHT:
                    center = center.add(-1, 0, -1);
                    break;
                case MIDDLELEFT:
                    center = center.add(0, 0, 1);
                    break;
                case MIDDLERIGHT:
                    center = center.add(0, 0, -1);
                    break;
                case BOTTOMLEFT:
                    center = center.add(1, 0, 1);
                    break;
                case BOTTOMCENTER:
                    center = center.add(1, 0, 0);
                    break;
                case BOTTOMRIGHT:
                    center = center.add(1, 0, -1);
                    break;
            }
        return center;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("beybladeItem")) {
            setBeybladeItem(ItemStack.parse(registryAccess(), tag.get("beybladeItem")).orElse(ItemStack.EMPTY));
        }
        if (tag.contains("rotationAngle")) {
            setRotationAngle(tag.getFloat("rotationAngle"));
        }
        if (tag.contains("rotationSpeed")) {
            setRotationAngle(tag.getFloat("rotationSpeed"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("beybladeItem", getBeybladeItem().save(registryAccess()));
        compound.putFloat("rotationAngle", getRotationAngle());
        compound.putFloat("rotationSpeed", getRotationSpeed());
    }
}
