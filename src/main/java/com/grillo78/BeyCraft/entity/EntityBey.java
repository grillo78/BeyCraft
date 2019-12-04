package com.grillo78.BeyCraft.entity;

import java.util.Random;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.blocks.StadiumBlock;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityBey extends EntityCreature implements IEntityAdditionalSpawnData {

	public ItemBeyLayer layer;
	public ItemBeyDisk disk;
	public ItemBeyDriver driver;
	private final float maxRotationSpeed;
	public float rotationSpeed;
	public float angle;
	private boolean movementStarted = false;
	private boolean stoped = false;
	public float radius;
	private int rotationDirection;
	private String playerName;

	public EntityBey(World worldIn) {
		this(worldIn, new ItemStack(BeyRegistry.ACHILLESA4), new ItemStack(BeyRegistry.ELEVENDISK),
				new ItemStack(BeyRegistry.XTENDDRIVER), 1, 1, "");
	}

	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn, float bladerLevel,
			int rotationDirection, String playerName) {
		super(worldIn);
		this.setSize(0.10F, 0.10F);
		this.height = 0.253F;
		this.rotationSpeed = -5;
		maxRotationSpeed = rotationSpeed;
		this.rotationDirection = rotationDirection;
		this.playerName = playerName;
		radius = 0.2F;
		angle = 10;
		layer = (ItemBeyLayer) layerIn.getItem();
		disk = (ItemBeyDisk) diskIn.getItem();
		driver = (ItemBeyDriver) driverIn.getItem();
		BeyCraft.logger.info("Bey Launched");
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.rotationSpeed < 0 && (world.getBlockState(this.getPosition().down()).getBlock() instanceof StadiumBlock
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.AIR
				|| (world.getBlockState(getPosition()).getBlock() instanceof StadiumBlock && world
						.getBlockState(
								new BlockPos(getPositionVector().x, getPositionVector().y - 0.1, getPositionVector().z))
						.getBlock() instanceof StadiumBlock))) {
			// TODO method
			if (!world.isRemote && movementStarted) {
				if (world.getBlockState(
						new BlockPos(getPositionVector().x + 0.23, getPositionVector().y, getPositionVector().z))
						.getBlock() != BeyRegistry.STADIUM) {
					this.rotationYaw = 90;
				}
				if (world.getBlockState(
						new BlockPos(getPositionVector().x - 0.23, getPositionVector().y, getPositionVector().z))
						.getBlock() != BeyRegistry.STADIUM) {
					this.rotationYaw = -90;
				}
				if (world.getBlockState(
						new BlockPos(getPositionVector().x, getPositionVector().y, getPositionVector().z + 0.23))
						.getBlock() != BeyRegistry.STADIUM) {
					this.rotationYaw = 180;
				}
				if (world.getBlockState(
						new BlockPos(getPositionVector().x, getPositionVector().y, getPositionVector().z - 0.23))
						.getBlock() != BeyRegistry.STADIUM) {
					this.rotationYaw = 0;
				}
			}
			rotationSpeed += 0.005 * driver.friction;
			rotationYaw -= rotationSpeed * rotationDirection / (-maxRotationSpeed * 0.1);
			angle += rotationSpeed * 30 * rotationDirection;

		} else {
			if (world.getBlockState(getPosition()).getBlock() instanceof StadiumBlock && !(world
					.getBlockState(
							new BlockPos(getPositionVector().x, getPositionVector().y - 0.1, getPositionVector().z))
					.getBlock() instanceof StadiumBlock)) {
				if (world.getBlockState(getPosition().north()).getBlock() == Blocks.AIR) {
					move(MoverType.SELF, 0, 0, -0.1);
				}
				if (world.getBlockState(getPosition().south()).getBlock() == Blocks.AIR) {
					move(MoverType.SELF, 0, 0, 0.1);
				}
				if (world.getBlockState(getPosition().west()).getBlock() == Blocks.AIR) {
					move(MoverType.SELF, -0.1, 0, 0);
				}
				if (world.getBlockState(getPosition().east()).getBlock() == Blocks.AIR) {
					move(MoverType.SELF, 0.1, 0, 0);
				}
			}
			if (!stoped) {
				stoped = true;
				BeyCraft.logger.info("Bey Stopped");
				rotationSpeed = 0;
			}

		}

		if (radius > 0) {
			radius -= 0.001f * driver.radiusReducion * rotationSpeed / (maxRotationSpeed);
		} else {
			radius = 0;
		}

		super.onUpdate();

	}

	/**
	 * @return the stoped
	 */
	public boolean isStoped() {
		return stoped;
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
		if (!player.isSpectator()) {
			world.removeEntity(this);
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIRotate(this));
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);
		if (entityIn instanceof EntityBey) {
			EntityBey entity = (EntityBey) entityIn;
			if (this.rotationSpeed != 0) {
				if (((EntityBey) entityIn).rotationSpeed < 0) {
					float damage = new Random().nextFloat();
					int burstDamage = new Random().nextInt(5);
					if (layer.canAbsorb(this)) {
						this.rotationSpeed -= damage * ((EntityBey) entityIn).layer.getAttack()
								/ getLayer().getDefense();
					}
					if (entity.layer.getDefense() != 0) {

						((EntityBey) entityIn).rotationSpeed += damage / ((EntityBey) entityIn).layer.getDefense();
					} else {

						((EntityBey) entityIn).rotationSpeed += damage * layer.getAttack();
					}
					if (layer.getDefense() != 0) {
						damageEntity(DamageSource.GENERIC, (burstDamage + layer.getBurst())
								* ((EntityBey) entityIn).layer.getAttack() / layer.getDefense());
					} else {
						damageEntity(DamageSource.GENERIC,
								burstDamage * layer.getBurst() * ((EntityBey) entityIn).layer.getAttack());
					}
				}
			}
			this.move(MoverType.SELF, -this.getLookVec().x, this.getLookVec().y, -this.getLookVec().z);
			BeyCraft.logger.info("Collision");
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	public float getEyeHeight() {
		return this.height - 0.1F;
	}

	public ItemBeyLayer getLayer() {
		return layer;
	}

	private void dropItems() {
		if (!world.isRemote) {
			EntityItem itemLayer = new EntityItem(world, posX, posY, posZ, new ItemStack(layer));
			world.spawnEntity(itemLayer);
			EntityItem itemDisk = new EntityItem(world, posX, posY, posZ, new ItemStack(disk));
			world.spawnEntity(itemDisk);
			EntityItem itemDriver = new EntityItem(world, posX, posY, posZ, new ItemStack(driver));
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
		layer = (ItemBeyLayer) new ItemStack(compound.getCompoundTag("Layer")).getItem();
		disk = (ItemBeyDisk) new ItemStack(compound.getCompoundTag("Disk")).getItem();
		driver = (ItemBeyDriver) new ItemStack(compound.getCompoundTag("Driver")).getItem();
		playerName = compound.getString("Player");
		rotationSpeed = compound.getFloat("RotationSpeed");
		rotationDirection = compound.getInteger("RotationDirection");
		radius = compound.getFloat("Radius");
		angle = compound.getFloat("Angle");
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("Layer", new ItemStack(layer).writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", new ItemStack(disk).writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", new ItemStack(driver).writeToNBT(new NBTTagCompound()));
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
		compound.setTag("Layer", new ItemStack(layer).writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", new ItemStack(disk).writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", new ItemStack(driver).writeToNBT(new NBTTagCompound()));
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
			layer = (ItemBeyLayer) new ItemStack(compound.getCompoundTag("Layer")).getItem();
			disk = (ItemBeyDisk) new ItemStack(compound.getCompoundTag("Disk")).getItem();
			driver = (ItemBeyDriver) new ItemStack(compound.getCompoundTag("Driver")).getItem();
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

	public boolean isMovementStarted() {
		return movementStarted;
	}

	public void setMovementStarted(boolean movementStarted) {
		this.movementStarted = movementStarted;
	}
}
