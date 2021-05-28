package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Absorb;
import com.grillo78.beycraft.items.*;
import com.grillo78.beycraft.util.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.util.math.vector.Vector3d;
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

	private ItemStackHandler inventory = new ItemStackHandler(3);
	private int rotationDirection;
	private int rotationStartedCount = 0;
	private boolean rotationStarted = false;
	private static final DataParameter<Float> BLADERLEVEL = EntityDataManager.defineId(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.defineId(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> MAXROTATIONSPEED = EntityDataManager.defineId(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> RADIUS = EntityDataManager.defineId(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Boolean> HORIZONTALCOLLISION = EntityDataManager.defineId(EntityBey.class,
			DataSerializers.BOOLEAN);
	private String playerName;
	public float angle = 0;
	private int changeAngleCount = 0;
	private boolean increaseRadius = false;
	private boolean stoped = false;
	private float maxRadius;
	private boolean beylogger = false;

	private Vector3d[] points = new Vector3d[5];

	/**
	 * @param type
	 * @param world
	 */
	public EntityBey(EntityType<? extends EntityBey> type, World world) {
		this(type, world, ItemStack.EMPTY, 1, null, 0, false);
	}

	public EntityBey(EntityType<? extends EntityBey> type, World world, ItemStack layer, int rotationDirection,
			String playerName, int bladerLevel, boolean beylogger) {
		super(type, world);
		this.beylogger = beylogger;
		this.rotationDirection = rotationDirection;
		this.inventory = new ItemStackHandler(3);
		if (layer.hasTag()) {
			this.inventory.setStackInSlot(0, layer);
			this.inventory.setStackInSlot(1, ItemStack.of(layer.getTag().getCompound("disc")));
			this.inventory.setStackInSlot(2, ItemStack.of(layer.getTag().getCompound("driver")));
		}
		this.playerName = playerName;
		setBladerlevel(bladerLevel);
		if (!world.isClientSide) {
			this.setMaxRotationSpeed(7);
			this.setRotationSpeed(getMaxRotationSpeed());
		}
		maxRadius = 1.6F * getMaxRotationSpeed() / 15;
		this.setRadius(maxRadius);
		maxUpStep = 0.1F;
	}

	public boolean hasBeylogger(){
		return beylogger;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 74.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return BeyRegistry.HITSOUND;
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
	}

	public float getMaxRadius() {
		return maxRadius;
	}

	public void setRotationStarted(boolean rotationStarted) {
		this.rotationStarted = rotationStarted;
	}

	public boolean isRotationStarted() {
		return rotationStarted;
	}

	private void dropItems() {
		BlockPos pos = null;
		for (PlayerEntity player : level.players()) {
			if (playerName.equals(player)) {
				pos = new BlockPos(player.getX(), player.getY(), player.getZ());
			}
		}
		inventory.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			h.getStackInSlot(0).shrink(1);

			h.getStackInSlot(1).shrink(1);

		});
		inventory.getStackInSlot(0).getTag().put("disc", ItemStack.EMPTY.save(new CompoundNBT()));
		inventory.getStackInSlot(0).getTag().put("driver", ItemStack.EMPTY.save(new CompoundNBT()));
		if (pos == null) {
			spawnAtLocation(inventory.getStackInSlot(0));
			spawnAtLocation(inventory.getStackInSlot(1));
			spawnAtLocation(inventory.getStackInSlot(2));
		} else {
			level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(0)));
			level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(1)));
			level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(2)));
		}
	}

	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	private void dropItem(PlayerEntity player) {
		inventory.getStackInSlot(0).getTag().putBoolean("isEntity", false);
		level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
				inventory.getStackInSlot(0)));
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ROTATIONSPEED, 1f);
		this.entityData.define(MAXROTATIONSPEED, 1f);
		this.entityData.define(BLADERLEVEL, 1f);
		this.entityData.define(HORIZONTALCOLLISION, false);
		this.entityData.define(RADIUS, 1.6F);
		super.defineSynchedData();
	}

	@Override
	public void die(DamageSource cause) {
		dropItems();
		remove();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		rotationDirection = nbt.getInt("RotationDirection");
		setRotationSpeed(nbt.getFloat("RotationSpeed"));
		setMaxRotationSpeed(nbt.getFloat("MaxRotationSpeed"));
		setBladerlevel(nbt.getFloat("BladerLevel"));
		inventory.deserializeNBT(nbt.getCompound("inventory"));
		maxRadius = nbt.getFloat("MaxRadius");
		playerName = nbt.getString("PlayerName");
		super.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("RotationDirection", rotationDirection);
		compound.putFloat("MaxRotationSpeed", getMaxRotationSpeed());
		compound.putFloat("RotationSpeed", getRotationSpeed());
		compound.putFloat("MaxRadius", maxRadius);
		compound.putString("PlayerName", playerName);
		compound.putFloat("BladerLevel", getBladerLevel());
		return compound;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		for (int i = 0; i < 3; i++) {
			buffer.writeItem(inventory.getStackInSlot(i));
		}
		buffer.writeInt(rotationDirection);
		buffer.writeUtf(playerName);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		for (int i = 0; i < 3; i++) {
			this.inventory.setStackInSlot(i, additionalData.readItem());
		}
		rotationDirection = additionalData.readInt();
		playerName = additionalData.readUtf();
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("RotationDirection", rotationDirection);
		compound.putFloat("MaxRotationSpeed", getMaxRotationSpeed());
		compound.putFloat("RotationSpeed", getRotationSpeed());
		compound.putFloat("Radius", getRadius());
		compound.putFloat("MaxRadius", maxRadius);
		compound.putString("PlayerName", playerName);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		rotationDirection = compound.getInt("RotationDirection");
		setRotationSpeed(compound.getFloat("RotationSpeed"));
		setMaxRotationSpeed(compound.getFloat("MaxRotationSpeed"));
		inventory.deserializeNBT(compound.getCompound("inventory"));
		setRadius(compound.getFloat("Radius"));
		maxRadius = compound.getFloat("MaxRadius");
		playerName = compound.getString("PlayerName");
		super.readAdditionalSaveData(compound);
	}

	@Override
	public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
		if (!level.isClientSide && !player.isSpectator()) {
			if (hand == Hand.MAIN_HAND) {
				dropItem(player);
				this.remove();
				return ActionResultType.SUCCESS;
			}
		}
		return super.interactAt(player, vec, hand);
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
					points[i] = entity.position();
				}
			} else {
				points[i] = entity.position();
			}
		}
	}

	public Vector3d[] getPoints() {
		return points;
	}

	@Override
	public void tick() {
		if (!onGround || inventory.getStackInSlot(0).getItem() instanceof ItemBeyLayer
				&& inventory.getStackInSlot(1).getItem() instanceof ItemBeyDisc
				&& inventory.getStackInSlot(2).getItem() instanceof ItemBeyDriver) {
			if (this.getRotationSpeed() > 0
					&& (level.getBlockState(this.getOnPos().below()).getBlock() == BeyRegistry.STADIUM
							|| level.getBlockState(this.getOnPos().below()).getBlock() == Blocks.AIR
							|| level.getBlockState(this.getOnPos()).getBlock() == Blocks.AIR
							|| (level.getBlockState(new BlockPos(position().x,
					position().y - 0.1, position().z)).getBlock() == BeyRegistry.STADIUM
									|| (!ConfigManager.isOnlyStadium() && (!ConfigManager.getBlockBlackList()
											.contains(level
													.getBlockState(new BlockPos(position().x,
															position().y - 0.1, position().z))
													.getBlock())))))) {

				if (!rotationStarted) {
					rotationStartedCount++;
					if (rotationStartedCount == 4) {
						rotationStarted = true;
					}
				} else {
					if (this.getLayer().getItem() instanceof ItemBeyLayerGT && getLayer().getTag().contains("weight")
							&& ItemStack.of(getLayer().getTag().getCompound("weight"))
									.getItem() instanceof ItemBeyGTWeight) {
						setRotationSpeed(getRotationSpeed() - 0.005F
								* ((ItemBeyDriver) getDriver().getItem()).getFriction(getDriver())
								/ (10 * (((ItemBeyDisc) getDisc().getItem()).getWeight() + ((ItemBeyGTWeight) ItemStack
										.of(getLayer().getTag().getCompound("weight")).getItem()).getWeight())));
					} else {
						setRotationSpeed(getRotationSpeed()
								- 0.005F * ((ItemBeyDriver) getDriver().getItem()).getFriction(getDriver())
										/ (10 * ((ItemBeyDisc) getDisc().getItem()).getWeight()));

					}
					angle += getRotationSpeed() * 30 * -rotationDirection;
				}
			} else {
				if (!stoped) {
					stoped = true;
					setRotationSpeed(0);
				}
			}
			if (isHorizontalCollision() && !isStoped()) {
				for (int i = 0; i < 10; i++) {
					level.addParticle(BeyRegistry.SPARKLE, getX(), getY() + 0.5, getZ(), random.nextInt(5),
							random.nextInt(5), random.nextInt(5));
				}
			}

		}
		if (level.isClientSide) {
			updatePoints(this);
		}
		super.tick();
	}

	@Override
	public void travel(Vector3d p_213352_1_) {
		super.travel(p_213352_1_);
		if (horizontalCollision) {
			yRot += 170 * rotationDirection;
		}
		setHorizontalCollision(horizontalCollision);
	}

	@Override
	protected void doPush(Entity entityIn) {
		if (!level.isClientSide) {
			if (!stoped && entityIn instanceof EntityBey) {
				EntityBey bey = (EntityBey) entityIn;
				playHurtSound(DamageSource.GENERIC);
				double x = (getX() - entityIn.getX()) / 2;
				double y = (getY() - entityIn.getY()) / 2;
				double z = (getZ() - entityIn.getZ()) / 2;
				((ServerWorld) level).sendParticles(BeyRegistry.SPARKLE, getX(), getY(), getZ(), 10, x, y, z,
						10);
				if (!((EntityBey) entityIn).isStoped()) {
					if (((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() instanceof Absorb
							|| ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() instanceof Absorb
							|| ((ItemBeyDisc) getDisc().getItem()).getPrimaryAbility() instanceof Absorb
							|| ((ItemBeyDisc) getDisc().getItem()).getSecundaryAbility() instanceof Absorb) {
						setRotationSpeed(getRotationSpeed()
								+ (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())) / 100);
						bey.setRotationSpeed(bey.getRotationSpeed()
								- (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
										+ ((EntityBey) entityIn).getBladerLevel()) / getBladerLevel());
						if (getRotationSpeed() > getMaxRotationSpeed()) {
							setRotationSpeed(getMaxRotationSpeed());
						}
						if (new Random().nextInt(3) == 1) {
							hurt(DamageSource.mobAttack((LivingEntity) entityIn),
									(int) (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem())
											.getAttack(getLayer())
											+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
											- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
											+ ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
						}
					} else {
						setRotationSpeed(getRotationSpeed()
								- (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
										+ ((EntityBey) entityIn).getBladerLevel()) / getBladerLevel());
						if (new Random().nextInt(3) == 1) {
							hurt(DamageSource.mobAttack((LivingEntity) entityIn),
									(int) (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem())
											.getAttack(getLayer())
											+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
											- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
											+ ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
						}
					}
					if (getRadius() == 0 && new Random().nextInt(5) == 1) {
						increaseRadius = true;
						if (new Random().nextInt(50) == 1) {
							hurt(DamageSource.mobAttack((LivingEntity) entityIn),
									(int) (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem())
											.getAttack(getLayer())
											+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
											- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
											+ ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
						} else {
							hurt(DamageSource.GENERIC,
									(int) (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem())
											.getAttack(getLayer())
											+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
											- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
											+ ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
						}
					} else {
						hurt(DamageSource.GENERIC,
								(int) (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem())
										.getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())*4
										- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
										+ ((EntityBey) entityIn).getBladerLevel() - getBladerLevel()));
					}
				}

			}
		}
		super.doPush(entityIn);
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
	 * @return the inventory
	 */
	public ItemStackHandler getInventory() {
		return inventory;
	}

	public String getPlayerName() {
		return playerName;
	}

	@Override
	protected void updateControlFlags() {
		boolean flag = !(this.getControllingPassenger() instanceof MobEntity);
		boolean flag1 = !(this.getVehicle() instanceof BoatEntity);
		this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
		this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
	}

	public ItemStack getLayer() {
		return inventory.getStackInSlot(0);
	}

	public void setLayer(ItemStack layer) {
		inventory.setStackInSlot(0, layer);
	}

	public ItemStack getDisc() {
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
		return ((Boolean) this.entityData.get(HORIZONTALCOLLISION)).booleanValue();
	}

	public void setHorizontalCollision(boolean horizontalCollision) {
		this.entityData.set(HORIZONTALCOLLISION, Boolean.valueOf(horizontalCollision));
	}

	public final float getRadius() {
		return ((Float) this.entityData.get(RADIUS)).floatValue();
	}

	public void setRadius(float radius) {
		this.entityData.set(RADIUS, Float.valueOf(radius));
	}

	public final float getBladerLevel() {
		return ((Float) this.entityData.get(BLADERLEVEL)).floatValue();
	}

	public void setBladerlevel(float bladerLevel) {
		this.entityData.set(BLADERLEVEL, Float.valueOf(bladerLevel));
	}

	public void setRotationSpeed(float speed) {
		this.entityData.set(ROTATIONSPEED, Float.valueOf(speed));
	}

	public final float getRotationSpeed() {
		return ((Float) this.entityData.get(ROTATIONSPEED)).floatValue();
	}

	public void setMaxRotationSpeed(float speed) {
		this.entityData.set(MAXROTATIONSPEED, Float.valueOf(speed));
	}

	public final float getMaxRotationSpeed() {
		return ((Float) this.entityData.get(MAXROTATIONSPEED)).floatValue();
	}

	public boolean isStoped() {
		return stoped;
	}
}
