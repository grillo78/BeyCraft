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
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityBey extends EntityCreature implements IEntityAdditionalSpawnData {

	public ItemBeyLayer layer;
	public ItemBeyDisk disk;
	public ItemBeyDriver driver;
	private final float maxRotationSpeed;
	private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	public float angle;
	private boolean stoped = false;
	private int rotationDirection;
	private int bladerLevel;
	private String playerName;

	public EntityBey(World worldIn) {
		this(worldIn, new ItemStack(BeyRegistry.ACHILLESA4), new ItemStack(BeyRegistry.ELEVENDISK),
				new ItemStack(BeyRegistry.XTENDDRIVER), 1, 1, "");
	}

	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn, int bladerLevel,
			int rotationDirection, String playerName) {
		super(worldIn);
		this.setSize(0.253F, 0.253F);
		this.height = 0.253F;
		this.bladerLevel = bladerLevel;
		this.setRotationSpeed(-10 * this.bladerLevel);
		maxRotationSpeed = getRotationSpeed();
		this.rotationDirection = rotationDirection;
		this.playerName = playerName;
		setRadius(0.2F);
		angle = 10;
		layer = (ItemBeyLayer) layerIn.getItem();
		disk = (ItemBeyDisk) diskIn.getItem();
		driver = (ItemBeyDriver) driverIn.getItem();
		BeyCraft.logger.info("Bey Launched");
	}

	public final float getRadius() {
		return ((Float) this.dataManager.get(RADIUS)).floatValue();
	}

	public void setRadius(float radius) {
		this.dataManager.set(RADIUS, Float.valueOf(radius));
	}

	public int getBladerLevel() {
		return bladerLevel;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	public final float getRotationSpeed() {
		return ((Float) this.dataManager.get(ROTATIONSPEED)).floatValue();
	}

	@Override
	public void onUpdate() {
		if (this.getRotationSpeed() < 0 && (world.getBlockState(this.getPosition().down())
				.getBlock() instanceof StadiumBlock
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.AIR
				|| world.getBlockState(this.getPosition().down()).getBlock() == Blocks.STONE
				|| (world.getBlockState(getPosition()).getBlock() instanceof StadiumBlock && world
						.getBlockState(
								new BlockPos(getPositionVector().x, getPositionVector().y - 0.1, getPositionVector().z))
						.getBlock() instanceof StadiumBlock))) {

			setRotationSpeed(getRotationSpeed() + 0.005F * driver.friction);
			rotationYaw -= getRotationSpeed() * rotationDirection / (-maxRotationSpeed * 0.1);
			angle += getRotationSpeed() * 30 * rotationDirection;

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
				setRotationSpeed(0);
			}

		}

		if (getRadius() > 0) {
			setRadius(getRadius() - 0.001f * driver.radiusReduction * getRotationSpeed() / (maxRotationSpeed));
		} else {
			setRadius(0);;
		}

		super.onUpdate();

	}

	@Override
	protected void entityInit() {
		this.dataManager.register(ROTATIONSPEED, Float.valueOf(1.0F));
		this.dataManager.register(RADIUS, Float.valueOf(1.0F));
		super.entityInit();
	}

	/**
	 * @return the stoped
	 */
	public boolean isStoped() {
		return stoped;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.005);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1024);
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
		this.tasks.addTask(0, new EntityAIRotate(this));
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);
		if (onGround) {
			if (entityIn instanceof EntityBey) {
				EntityBey entity = (EntityBey) entityIn;
				if (this.getRotationSpeed() != 0) {
					if (entity.getRotationSpeed() < 0) {
						float damage = new Random().nextInt(5);
						int burstDamage = new Random().nextInt(4);
						if (layer.canAbsorb(this)) {
							setRotationSpeed(
									getRotationSpeed() - damage * entity.layer.getAttack() / getLayer().getDefense());
						}
						if (entity.layer.getDefense() != 0) {

							entity.setRotationSpeed(
									entity.getRotationSpeed() + damage * layer.getAttack() / entity.layer.getDefense());
						} else {

							entity.setRotationSpeed(entity.getRotationSpeed() + damage * layer.getAttack());
						}
						if (layer.getDefense() != 0) {
							damageEntity(DamageSource.GENERIC,
									(burstDamage + layer.getBurst()) * entity.layer.getAttack() / layer.getDefense());
						} else {
							damageEntity(DamageSource.GENERIC,
									burstDamage * layer.getBurst() * entity.layer.getAttack());
						}
						playSound(getHurtSound(DamageSource.GENERIC), 1, 1);
					}
					if(getRadius()!=0) {
						this.rotationYaw -= 90;
					}
					if(new Random().nextInt(10)==1) {
						this.rotationYaw -= 90;
						move(MoverType.SELF, getLookVec().x * getRadius() * 1.5, 0, getLookVec().z * getRadius() * 1.5);
						setRadius(0.2F);
						entity.setRadius(0.2F);
					}
				}
			}
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

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		layer = (ItemBeyLayer) new ItemStack(compound.getCompoundTag("Layer")).getItem();
		disk = (ItemBeyDisk) new ItemStack(compound.getCompoundTag("Disk")).getItem();
		driver = (ItemBeyDriver) new ItemStack(compound.getCompoundTag("Driver")).getItem();
		playerName = compound.getString("Player");
		setRotationSpeed(compound.getFloat("RotationSpeed"));
		rotationDirection = compound.getInteger("RotationDirection");
		setRadius(compound.getFloat("Radius"));
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
		compound.setFloat("RotationSpeed", getRotationSpeed());
		compound.setFloat("Radius", getRadius());
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
		compound.setFloat("RotationSpeed", getRotationSpeed());
		compound.setInteger("RotationDirection", rotationDirection);
		compound.setFloat("Radius", getRadius());
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
			setRotationSpeed(compound.getFloat("RotationSpeed"));
			rotationDirection = compound.getInteger("RotationDirection");
			setRadius(compound.getFloat("Radius"));
			angle = compound.getFloat("Angle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setRotationSpeed(float rotationSpeed) {

		this.dataManager.set(ROTATIONSPEED, Float.valueOf(rotationSpeed));
	}

	public int getRotationDirection() {
		return rotationDirection;
	}
}
