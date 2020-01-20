package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.blocks.StadiumBlock;
import com.grillo78.BeyCraft.items.ItemBeyDriver;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author grillo78
 *
 */
public class EntityBey extends CreatureEntity implements IEntityAdditionalSpawnData {

	private ItemStackHandler inventory = new ItemStackHandler(3);
	private int rotationDirection;
	private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	public float angle = 0;
	private boolean increaseRadius = false;
	private boolean stoped = false;
	private float maxRotationSpeed = 5;

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
		stepHeight = 0;
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

	private void dropItem() {
		world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
				inventory.getStackInSlot(0)));
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(ROTATIONSPEED, 10F);
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
		super.onDeath(cause);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		rotationDirection = nbt.getInt("RotationDirection");
		setRotationSpeed(nbt.getFloat("RotationSpeed"));
		maxRotationSpeed = nbt.getFloat("MaxRotationSpeed");
		inventory.deserializeNBT(nbt.getCompound("inventory"));
		super.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("RotationDirection", rotationDirection);
		compound.putFloat("MaxRotationSpeed", maxRotationSpeed);
		compound.putFloat("RotationSpeed", getRotationSpeed());
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
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		rotationDirection = compound.getInt("RotationDirection");
		setRotationSpeed(compound.getFloat("RotationSpeed"));
		setRadius(compound.getFloat("Radius"));
		maxRotationSpeed = compound.getFloat("MaxRotationSpeed");
		inventory.deserializeNBT(compound.getCompound("inventory"));
		super.readAdditional(compound);
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
		if (!world.isRemote) {
			if (hand == Hand.MAIN_HAND) {
				dropItem();
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

	@Override
	public void tick() {
		if (this.getRotationSpeed() > 0 && (world.getBlockState(this.getPosition().down())
				.getBlock() instanceof StadiumBlock
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.AIR
				|| (world.getBlockState(getPosition()).getBlock() instanceof StadiumBlock && world
						.getBlockState(
								new BlockPos(getPositionVector().x, getPositionVector().y - 0.1, getPositionVector().z))
						.getBlock() instanceof StadiumBlock))) {
			setRotationSpeed(
					getRotationSpeed() - 0.005F * ((ItemBeyDriver) inventory.getStackInSlot(2).getItem()).friction);

			angle += getRotationSpeed() * 30 * rotationDirection;
		} else {
			if (!stoped) {
				stoped = true;
				BeyCraft.logger.info("Bey Stopped");
				setRotationSpeed(0);
			}

		}
		super.tick();
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
	protected void func_213385_F() {
		boolean flag = !(this.getControllingPassenger() instanceof MobEntity);
		boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
		this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
		this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
//		super.func_213385_F();
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
