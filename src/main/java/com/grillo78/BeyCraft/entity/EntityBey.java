package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.SoundHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

	public EntityBey(World worldIn) {
		this(worldIn, new ItemStack(BeyRegistry.ACHILLESA4), new ItemStack(BeyRegistry.ELEVENDISK),
				new ItemStack(BeyRegistry.XTENDDRIVER));
	}

	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.height = 0.253F;
		this.rotationSpeed = 100;
		layer = layerIn;
		disk = diskIn;
		driver = driverIn;
	}

	@Override
	public void onUpdate() {
		if (rotationSpeed != 0) {
			rotationSpeed -= 0.25;
			this.rotationYaw-=rotationSpeed;
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
	}

	@Override
	public void onDeath(DamageSource cause) {
		dropItems();
		super.onDeath(cause);
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
			setHealth(0);
		}
//		if (entityIn instanceof EntityBey) {
//			((EntityBey) entityIn).setHealth(((EntityBey) entityIn).getHealth()-10);
//		}
		super.collideWithEntity(entityIn);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAIWander(this, 1.0D));
		super.initEntityAI();
	}

	public float getEyeHeight() {
		return this.height - 0.1F;
	}

	private void dropItems() {
		EntityItem itemLayer = new EntityItem(world, posX, posY, posZ, layer);
		world.spawnEntity(itemLayer);
		EntityItem itemDisk = new EntityItem(world, posX, posY, posZ, disk);
		world.spawnEntity(itemDisk);
		EntityItem itemDriver = new EntityItem(world, posX, posY, posZ, driver);
		world.spawnEntity(itemDriver);
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
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setFloat("RotationSpeed", rotationSpeed);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setFloat("RotationSpeed", rotationSpeed);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
