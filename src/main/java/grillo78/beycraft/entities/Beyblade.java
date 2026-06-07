package grillo78.beycraft.entities;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.items.MainBeyPart;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class Beyblade extends LivingEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
    private static final EntityDataAccessor<ItemStack> BEYBLADE_ITEM = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> ROTATION_ANGLE = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ROTATION_SPEED = SynchedEntityData.defineId(Beyblade.class, EntityDataSerializers.FLOAT);

    public Beyblade(EntityType<Beyblade> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEYBLADE_ITEM, ItemStack.EMPTY);
        builder.define(ROTATION_ANGLE, 0F);
        builder.define(ROTATION_SPEED, 500F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes();
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

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return  (getBeybladeItem().getItem() instanceof MainBeyPart) && !((MainBeyPart) getBeybladeItem().getItem()).canBurst();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        if (getBeybladeItem().getItem() instanceof MainBeyPart) {
            if (((MainBeyPart) getBeybladeItem().getItem()).canBurst()) {
                ((MainBeyPart) getBeybladeItem().getItem()).die(getBeybladeItem(), level, position());
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(!level().isClientSide){
            level().addFreshEntity(new ItemEntity(level(), position().x, position().y, position().z, getBeybladeItem()));
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
                setRotationAngle(getRotationSpeed() + getRotationAngle());
                if (getRotationSpeed() > 0)
                    setRotationSpeed(getRotationSpeed() - (((MainBeyPart) getBeybladeItem().getItem()).getFriction(getBeybladeItem()) -((MainBeyPart) getBeybladeItem().getItem()).getWeight(getBeybladeItem())/50));
                else if (getRotationSpeed() < 0)
                    setRotationSpeed(0);
            }
        }
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
