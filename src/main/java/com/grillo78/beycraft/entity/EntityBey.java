package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author grillo78
 */
public class EntityBey extends CreatureEntity implements IEntityAdditionalSpawnData {

    private int updatePointCount = 0;
    private ItemStackHandler inventory = new ItemStackHandler(3);
    private int rotationDirection;
    private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.createKey(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityBey.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Boolean> HORIZONTALCOLLISION = EntityDataManager.createKey(EntityBey.class,
            DataSerializers.BOOLEAN);
    public float angle = 0;
    private boolean increaseRadius = false;
    private boolean stoped = false;
    private float maxRotationSpeed = 5;
    private float maxRadius;

    private Vec3d[] points = new Vec3d[5];

    /**
     * @param type
     * @param world
     */
    public EntityBey(EntityType<? extends EntityBey> type, World world) {
        this(type, world, new ItemStack(BeyRegistry.LAYERICON), 1);
    }

    public EntityBey(EntityType<? extends EntityBey> type, World world, ItemStack layer, int rotationDirection) {
        super(type, world);
        this.rotationDirection = rotationDirection;
        this.inventory = new ItemStackHandler(3);
        this.inventory.setStackInSlot(0, layer);
        layer.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            this.inventory.setStackInSlot(1, h.getStackInSlot(0).copy());
            this.inventory.setStackInSlot(2, h.getStackInSlot(1).copy());
        });
        this.setRotationSpeed(maxRotationSpeed);
        maxRadius = 1.6F * maxRotationSpeed / 10;
        this.setRadius(maxRadius);
        stepHeight = 0;
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return BeyRegistry.HITSOUND;
    }

    @Override
    protected void playHurtSound(DamageSource p_184581_1_) {
        super.playHurtSound(p_184581_1_);
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {}

    public float getMaxRadius() {
        return maxRadius;
    }

    private void dropItems() {
        inventory.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            h.getStackInSlot(0).shrink(1);
            ;
            h.getStackInSlot(1).shrink(1);
            ;
        });
        world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
                inventory.getStackInSlot(0)));
        world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
                inventory.getStackInSlot(1)));
        world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
                inventory.getStackInSlot(2)));
    }

    private void dropItem(PlayerEntity player) {
        world.addEntity(new ItemEntity(world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(),
                inventory.getStackInSlot(0)));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(ROTATIONSPEED, maxRotationSpeed);
        this.dataManager.register(HORIZONTALCOLLISION, false);
        this.dataManager.register(RADIUS, 1.6F);
        super.registerData();
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(74.0D);
    }

    @Override
    public void onDeath(DamageSource cause) {
        dropItems();
        remove();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        rotationDirection = nbt.getInt("RotationDirection");
        setRotationSpeed(nbt.getFloat("RotationSpeed"));
        maxRotationSpeed = nbt.getFloat("MaxRotationSpeed");
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        maxRadius = nbt.getFloat("MaxRadius");
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("RotationDirection", rotationDirection);
        compound.putFloat("MaxRotationSpeed", maxRotationSpeed);
        compound.putFloat("RotationSpeed", getRotationSpeed());
        compound.putFloat("MaxRadius", maxRadius);
        return compound;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        for (int i = 0; i < 3; i++) {
            buffer.writeItemStack(inventory.getStackInSlot(i));
        }
        buffer.writeInt(rotationDirection);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        for (int i = 0; i < 3; i++) {
            this.inventory.setStackInSlot(i, additionalData.readItemStack());
        }
        rotationDirection = additionalData.readInt();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.put("inventory", inventory.serializeNBT());
        compound.putInt("RotationDirection", rotationDirection);
        compound.putFloat("MaxRotationSpeed", maxRotationSpeed);
        compound.putFloat("RotationSpeed", getRotationSpeed());
        compound.putFloat("Radius", getRadius());
        compound.putFloat("MaxRadius", maxRadius);
        super.writeAdditional(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        rotationDirection = compound.getInt("RotationDirection");
        setRotationSpeed(compound.getFloat("RotationSpeed"));
        maxRotationSpeed = compound.getFloat("MaxRotationSpeed");
        inventory.deserializeNBT(compound.getCompound("inventory"));
        setRadius(compound.getFloat("Radius"));
        maxRadius = compound.getFloat("MaxRadius");
        super.readAdditional(compound);
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        if (!world.isRemote && !player.isSpectator()) {
            if (hand == Hand.MAIN_HAND) {
                dropItem(player);
                this.remove();
                return ActionResultType.SUCCESS;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EntityGoalRotate(this));
        super.registerGoals();
    }

    public void updatePoints(EntityBey entity) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null) {
                if (i != points.length - 1) {
                    points[i] = points[i + 1];
                } else {
                    points[i] = entity.getPositionVec();
                }
            } else {
                points[i] = entity.getPositionVec();
            }
        }
    }

    public Vec3d[] getPoints() {
        return points;
    }

    @Override
    public void tick() {
       if(inventory.getStackInSlot(0).getItem() instanceof  ItemBeyLayer && inventory.getStackInSlot(1).getItem() instanceof ItemBeyDisc && inventory.getStackInSlot(2).getItem() instanceof  ItemBeyDriver){
           if (this.getRotationSpeed() > 0 && (world.getBlockState(this.getPosition().down())
                   .getBlock() instanceof StadiumBlock
                   || world.getBlockState(this.getPosition().down()).getBlock() == Blocks.AIR
                   || (world.getBlockState(getPosition()).getBlock() instanceof StadiumBlock && world
                   .getBlockState(
                           new BlockPos(getPositionVector().x, getPositionVector().y - 0.1, getPositionVector().z))
                   .getBlock() instanceof StadiumBlock))) {
               setRotationSpeed(
                       getRotationSpeed() - 0.005F * ((ItemBeyDriver) inventory.getStackInSlot(2).getItem()).getFriction());

               angle += getRotationSpeed() * 30 * rotationDirection;
           } else {
               if (!stoped) {
                   stoped = true;
                   BeyCraft.logger.info("Bey Stopped");
                   setRotationSpeed(0);
               }
           }
           if (isHorizontalCollision() && !isStoped()) {
               for (int i = 0; i < 10; i++) {
                   world.addParticle(BeyRegistry.SPARKLE, getPosX(), getPosY() + 0.5, getPosZ(), rand.nextInt(5), rand.nextInt(5), rand.nextInt(5));
               }
           }
       }
        updatePoints(this);
        super.tick();
    }

    @Override
    public void travel(Vec3d p_213352_1_) {
        super.travel(p_213352_1_);
        if (collidedHorizontally) {
            rotationYaw += 90 * rotationDirection;
            BeyCraft.logger.info("rotation changed by collision");
        }
        setHorizontalCollision(collidedHorizontally);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        BeyCraft.logger.info(entityIn.getType().getRegistryName());
        if (!stoped && entityIn instanceof EntityBey) {
            playHurtSound(DamageSource.GENERIC);
            getLayer().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h->{
                damageEntity(DamageSource.GENERIC, ((ItemBeyLayer)((EntityBey)entityIn).getLayer().getItem()).getAttack()+((ItemBeyLayer)getLayer().getItem()).getBurst()-((ItemBeyLayer)getLayer().getItem()).getDefense());
            });
            double x = (getPosX() - entityIn.getPosX()) / 2;
            double y = (getPosY() - entityIn.getPosY()) / 2;
            double z =  (getPosZ() - entityIn.getPosZ()) / 2;
            ((ServerWorld) world).spawnParticle(BeyRegistry.SPARKLE, getPosX(), getPosY(), getPosZ(), 10, x, y, z, 10);
            if(new Random().nextInt(10) == 1){
                this.move(MoverType.SELF, new Vec3d(this.getPositionVec().inverse().x,this.getPositionVec().y+0.1,this.getPositionVec().inverse().z));
                increaseRadius = true;
            }
        }
        super.collideWithEntity(entityIn);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {

        super.applyEntityCollision(entityIn);
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos) {
        super.move(typeIn, pos);
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
     * @return the maxRotationSpeed
     */
    public float getMaxRotationSpeed() {
        return maxRotationSpeed;
    }

    /**
     * @return the inventory
     */
    public ItemStackHandler getInventory() {
        return inventory;
    }


    @Override
    protected void updateMovementGoalFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof MobEntity);
        boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
        this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
    }

    public ItemStack getLayer() {
        return inventory.getStackInSlot(0);
    }

    public void setLayer(ItemStack layer) {
        inventory.setStackInSlot(0, layer);
    }

    public ItemStack getDisk() {
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
        return ((Boolean) this.dataManager.get(HORIZONTALCOLLISION)).booleanValue();
    }

    public void setHorizontalCollision(boolean horizontalCollision) {
        this.dataManager.set(HORIZONTALCOLLISION, Boolean.valueOf(horizontalCollision));
    }

    public final float getRadius() {
        return ((Float) this.dataManager.get(RADIUS)).floatValue();
    }

    public void setRadius(float radius) {
        this.dataManager.set(RADIUS, Float.valueOf(radius));
    }

    public void setRotationSpeed(float speed) {
        this.dataManager.set(ROTATIONSPEED, Float.valueOf(speed));
    }

    public final float getRotationSpeed() {
        return ((Float) this.dataManager.get(ROTATIONSPEED)).floatValue();
    }

    public boolean isStoped() {
        return stoped;
    }
}
