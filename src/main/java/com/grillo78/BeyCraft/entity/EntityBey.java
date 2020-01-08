package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author grillo78
 *
 */
public class EntityBey extends CreatureEntity implements IEntityAdditionalSpawnData {

	private ItemStackHandler inventory = new ItemStackHandler(3);
	private int rotationDirection = 1;
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
		this(type, world,new ItemStack(BeyRegistry.STRIKEVALTRYEKV3),new ItemStack(BeyRegistry.SIXVDISK),new ItemStack(BeyRegistry.REBOOTDRIVER));
	}

	public EntityBey(EntityType<? extends EntityBey> type, World world,ItemStack layer,ItemStack disk,ItemStack driver) {
		super(type,world);
		this.inventory = new ItemStackHandler(3);
		this.inventory.setStackInSlot(0, layer);
		this.inventory.setStackInSlot(1, disk);
		this.inventory.setStackInSlot(2, driver);
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected void registerData() {
		this.dataManager.register(ROTATIONSPEED, 100F);
		this.dataManager.register(RADIUS, 1.5F);
		super.registerData();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
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
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		for (int i = 0; i < 3; i++) {
			this.inventory.setStackInSlot(i, additionalData.readItemStack());
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("RotationDirection", rotationDirection);
		compound.putFloat("MaxRotationSpeed", maxRotationSpeed);
		compound.putFloat("RotationSpeed", getRotationSpeed());
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		rotationDirection = compound.getInt("RotationDirection");
		setRotationSpeed(compound.getFloat("RotationSpeed"));
		maxRotationSpeed = compound.getFloat("MaxRotationSpeed");
		inventory.deserializeNBT(compound.getCompound("inventory"));
		super.readAdditional(compound);
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
		if (!world.isRemote && hand == Hand.MAIN_HAND) {
			world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
					inventory.getStackInSlot(0)));
			world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
					inventory.getStackInSlot(1)));
			world.addEntity(new ItemEntity(world, getPosition().getX(), getPosition().getY(), getPosition().getZ(),
					inventory.getStackInSlot(2)));
			this.remove();
			return ActionResultType.SUCCESS;
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}

	@Override
	public void tick() {
		if(getRotationSpeed()>0) {
			setRotationSpeed(getRotationSpeed() - 0.1f);
		}
		super.tick();
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

}
