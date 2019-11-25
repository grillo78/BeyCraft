package com.grillo78.BeyCraft.entity;

import java.util.Random;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;
import com.grillo78.BeyCraft.util.SoundHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityBey extends EntityCreature implements IEntityAdditionalSpawnData {

	public ItemStack layer;
	public ItemStack disk;
	public ItemStack driver;
	public float rotationSpeed;
	public float angle;
	public float radius = 0.2F;
	private boolean droppedItems = false;
	private int rotationDirection;

	public EntityBey(World worldIn) {
		this(worldIn, new ItemStack(BeyRegistry.ACHILLESA4), new ItemStack(BeyRegistry.ELEVENDISK),
				new ItemStack(BeyRegistry.XTENDDRIVER), 1, 1);
	}

	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn, int bladerLevel,
			int rotationDirection) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.height = 0.253F;
		this.rotationSpeed = -9 + bladerLevel;
		this.rotationDirection = rotationDirection;
		angle = 10;
		layer = layerIn.copy();
		disk = diskIn.copy();
		driver = driverIn.copy();
	}

	public boolean getDroppedItem() {
		return droppedItems;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (world.getBlockState(this.getPosition().down()).getBlock() != Blocks.GRASS && onGround) {
			rotationSpeed = 0;
		}
		if (this.rotationSpeed < 0) {
			rotationSpeed += 0.005;
			rotationYaw -= rotationSpeed * rotationDirection * ((ItemBeyDriver) driver.getItem()).friction;
			angle += rotationSpeed * 10 * rotationDirection;

		} else {
			rotationSpeed = 0;
		}
		if (radius > 0) {
			radius -= 0.0001f * ((ItemBeyDriver) driver.getItem()).radiusReducion * -9 / rotationSpeed;
		} else {
			radius = 0;
		}
		super.onUpdate();
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
	}

	@Override
	public void onDeath(DamageSource cause) {
		world.removeEntity(this);
	}

	@Override
	public void onRemovedFromWorld() {
		dropItems();
		super.onRemovedFromWorld();
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
			world.removeEntity(this);
		}
		if (entityIn instanceof EntityBey) {
			if (((EntityBey) entityIn).rotationSpeed < 0) {
				float damage = new Random().nextFloat();
				if (((ItemBeyLayer) layer.getItem()).canAbsorb(this)) {
					this.rotationSpeed -= damage;
				}
				((EntityBey) entityIn).rotationSpeed += damage;
				((EntityBey) entityIn).radius = 0.2f;
				((EntityBey) entityIn).damageEntity(DamageSource.FALL, new Random().nextInt(5));
				this.move(MoverType.SELF, entityIn.getLookVec().x, entityIn.getLookVec().y, entityIn.getLookVec().z);
			} else {
				((EntityBey) entityIn).rotationSpeed = 0;
				if (this.rotationSpeed != 0) {
					this.move(MoverType.SELF, entityIn.getLookVec().x, entityIn.getLookVec().y,
							entityIn.getLookVec().z);
				}
			}
		}
		super.collideWithEntity(entityIn);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAIRotate(this));
		super.initEntityAI();
	}

	public float getEyeHeight() {
		return this.height - 0.1F;
	}

	private void dropItems() {
		if (!world.isRemote) {
			EntityItem itemLayer = new EntityItem(world, posX, posY, posZ, layer);
			world.spawnEntity(itemLayer);
			EntityItem itemDisk = new EntityItem(world, posX, posY, posZ, disk);
			world.spawnEntity(itemDisk);
			EntityItem itemDriver = new EntityItem(world, posX, posY, posZ, driver);
			world.spawnEntity(itemDriver);
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundHandler.BEY_HIT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundHandler.BEY_HIT;
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		layer = new ItemStack(compound.getCompoundTag("Layer"));
		disk = new ItemStack(compound.getCompoundTag("Disk"));
		driver = new ItemStack(compound.getCompoundTag("Driver"));
		rotationSpeed = compound.getFloat("RotationSpeed");
		radius = compound.getFloat("Radius");
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setFloat("RotationSpeed", rotationSpeed);
		compound.setFloat("Radius", radius);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setFloat("RotationSpeed", rotationSpeed);
		compound.setInteger("RotationDirection", rotationDirection);
		compound.setFloat("Radius", radius);
		ByteBufUtils.writeTag(buffer, compound);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		NBTTagCompound compound;
		try {
			compound = ByteBufUtils.readTag(additionalData);
			layer = new ItemStack(compound.getCompoundTag("Layer"));
			disk = new ItemStack(compound.getCompoundTag("Disk"));
			driver = new ItemStack(compound.getCompoundTag("Driver"));
			rotationSpeed = compound.getFloat("RotationSpeed");
			rotationDirection = compound.getInteger("RotationDirection");
			radius = compound.getFloat("Radius");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getRotationDirection() {
		return rotationDirection;
	}
}
