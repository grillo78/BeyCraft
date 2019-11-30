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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
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
	private String playerName;

	public EntityBey(World worldIn) {
		this(worldIn, new ItemStack(BeyRegistry.ACHILLESA4), new ItemStack(BeyRegistry.ELEVENDISK),
				new ItemStack(BeyRegistry.XTENDDRIVER), 1, 1, "");
	}

	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn, float bladerLevel,
			int rotationDirection, String playerName) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.height = 0.253F;
		this.rotationSpeed = -5 * bladerLevel;
		this.rotationDirection = rotationDirection;
		this.playerName = playerName;
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
		if (this.rotationSpeed < 0 && ((world.getBlockState(this.getPosition().down()).getBlock() == BeyRegistry.STADIUM
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.STONE)
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.AIR)) {
			rotationSpeed += 0.005 * ((ItemBeyDriver) driver.getItem()).friction;
			rotationYaw -= rotationSpeed * rotationDirection* ((ItemBeyDriver) driver.getItem()).radiusReducion/4;
			angle += rotationSpeed * 100 * rotationDirection;

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
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		world.removeEntity(this);
		return EnumActionResult.SUCCESS;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIRotate(this));
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (!world.isRemote) {
			if (this.rotationSpeed != 0) {
				if (this.rotationSpeed < 0) {
					float damage = new Random().nextFloat();
					if (((ItemBeyLayer) this.layer.getItem()).canAbsorb(this)) {
						this.rotationSpeed -= damage;
					}
					this.rotationSpeed += damage;
					damageEntity(DamageSource.GENERIC, new Random().nextInt(10));
					this.move(MoverType.SELF, -this.getLookVec().x, this.getLookVec().y, -this.getLookVec().z);
					applyEntityCollision(entityIn);
				} else {
					this.rotationSpeed = 0;
					if (this.rotationSpeed != 0) {
						this.move(MoverType.SELF, this.getLookVec().x, this.getLookVec().y, this.getLookVec().z);
						applyEntityCollision(entityIn);
					}
				}
			}
		}
		super.collideWithEntity(entityIn);
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
		playerName = compound.getString("Player");
		rotationSpeed = compound.getFloat("RotationSpeed");
		rotationDirection = compound.getInteger("RotationDirection");
		radius = compound.getFloat("Radius");
		angle = compound.getFloat("Angle");
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setString("Player", playerName);
		compound.setInteger("RotationDirection", rotationDirection);
		compound.setFloat("RotationSpeed", rotationSpeed);
		compound.setFloat("Radius", radius);
		compound.setFloat("Angle", angle);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Layer", layer.writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", disk.writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", driver.writeToNBT(new NBTTagCompound()));
		compound.setString("Player", playerName);
		compound.setFloat("RotationSpeed", rotationSpeed);
		compound.setInteger("RotationDirection", rotationDirection);
		compound.setFloat("Radius", radius);
		compound.setFloat("Angle", angle);
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
			playerName = compound.getString("Player");
			rotationSpeed = compound.getFloat("RotationSpeed");
			rotationDirection = compound.getInteger("RotationDirection");
			radius = compound.getFloat("Radius");
			angle = compound.getFloat("Angle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getRotationDirection() {
		return rotationDirection;
	}
}
