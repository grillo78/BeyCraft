package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Absorb;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.capabilities.BladerLevelProvider;
import com.grillo78.beycraft.items.*;
import com.grillo78.beycraft.util.ConfigManager;
import com.mojang.brigadier.Message;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextComponentUtils;
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
	private static final DataParameter<Float> BLADERLEVEL = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> ROTATIONSPEED = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> MAXROTATIONSPEED = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Boolean> HORIZONTALCOLLISION = EntityDataManager.createKey(EntityBey.class,
			DataSerializers.BOOLEAN);
	private String playerName;
	public float angle = 0;
	private boolean increaseRadius = false;
	private boolean stoped = false;
	private float maxRadius;

	private Vector3d[] points = new Vector3d[5];

	/**
	 * @param type
	 * @param world
	 */
	public EntityBey(EntityType<? extends EntityBey> type, World world) {
		this(type, world, ItemStack.EMPTY, 1, null);
	}

	public EntityBey(EntityType<? extends EntityBey> type, World world, ItemStack layer, int rotationDirection,
			PlayerEntity playerEntity) {
		super(type, world);
		this.rotationDirection = rotationDirection;
		this.inventory = new ItemStackHandler(3);
		if (layer.hasTag()) {
			this.inventory.setStackInSlot(0, layer);
			this.inventory.setStackInSlot(1, ItemStack.read(layer.getTag().getCompound("disc")));
			this.inventory.setStackInSlot(2, ItemStack.read(layer.getTag().getCompound("driver")));
		}
		if (playerEntity != null) {
			this.playerName = playerEntity.getName().getString();
			playerEntity.getCapability(BladerLevelProvider.BLADERLEVEL_CAP).ifPresent(h -> {
				setBladerlevel(h.getBladerLevel());
			});
		} else {
			this.playerName = "";
		}
		if (!world.isRemote) {
			this.setMaxRotationSpeed(7);
			this.setRotationSpeed(getMaxRotationSpeed());
		}
		maxRadius = 1.6F * getMaxRotationSpeed() / 15;
		this.setRadius(maxRadius);
		stepHeight = 0;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return super.getCollisionBoundingBox();
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 74.0D);
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
		for(PlayerEntity player : world.getPlayers()){
			if(playerName.equals(player)){
				pos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
			}
		}
		inventory.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			h.getStackInSlot(0).shrink(1);

			h.getStackInSlot(1).shrink(1);

		});
		inventory.getStackInSlot(0).getTag().put("disc", ItemStack.EMPTY.write(new CompoundNBT()));
		inventory.getStackInSlot(0).getTag().put("driver", ItemStack.EMPTY.write(new CompoundNBT()));
		if(pos == null){
			entityDropItem(inventory.getStackInSlot(0));
			entityDropItem(inventory.getStackInSlot(1));
			entityDropItem(inventory.getStackInSlot(2));
		} else {
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(),
					pos.getZ(), inventory.getStackInSlot(0)));
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(),
					pos.getZ(), inventory.getStackInSlot(1)));
			world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(),
					pos.getZ(), inventory.getStackInSlot(2)));
		}
	}

	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}

	private void dropItem(PlayerEntity player) {
		inventory.getStackInSlot(0).getTag().putBoolean("isEntity", false);
		world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(),
				player.getPosZ(), inventory.getStackInSlot(0)));
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(ROTATIONSPEED, 1f);
		this.dataManager.register(MAXROTATIONSPEED, 1f);
		this.dataManager.register(BLADERLEVEL, 1f);
		this.dataManager.register(HORIZONTALCOLLISION, false);
		this.dataManager.register(RADIUS, 1.6F);
		super.registerData();
	}

//	@Override
//	protected void registerAttributes() {
//		super.registerAttributes();
//		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(74.0D);
//	}

	@Override
	public void onDeath(DamageSource cause) {
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
			buffer.writeItemStack(inventory.getStackInSlot(i));
		}
		buffer.writeInt(rotationDirection);
		buffer.writeString(playerName);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		for (int i = 0; i < 3; i++) {
			this.inventory.setStackInSlot(i, additionalData.readItemStack());
		}
		rotationDirection = additionalData.readInt();
		playerName = additionalData.readString();
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("RotationDirection", rotationDirection);
		compound.putFloat("MaxRotationSpeed", getMaxRotationSpeed());
		compound.putFloat("RotationSpeed", getRotationSpeed());
		compound.putFloat("Radius", getRadius());
		compound.putFloat("MaxRadius", maxRadius);
		compound.putString("PlayerName", playerName);
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		rotationDirection = compound.getInt("RotationDirection");
		setRotationSpeed(compound.getFloat("RotationSpeed"));
		setMaxRotationSpeed(compound.getFloat("MaxRotationSpeed"));
		inventory.deserializeNBT(compound.getCompound("inventory"));
		setRadius(compound.getFloat("Radius"));
		maxRadius = compound.getFloat("MaxRadius");
		playerName = compound.getString("PlayerName");
		super.readAdditional(compound);
	}

	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
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

	public Vector3d[] getPoints() {
		return points;
	}

	@Override
	public void tick() {
		if (!onGround || inventory.getStackInSlot(0).getItem() instanceof ItemBeyLayer
				&& inventory.getStackInSlot(1).getItem() instanceof ItemBeyDisc
				&& inventory.getStackInSlot(2).getItem() instanceof ItemBeyDriver) {
			if (this.getRotationSpeed() > 0 && (world.getBlockState(this.getOnPosition().down())
					.getBlock() instanceof StadiumBlock
					|| world.getBlockState(this.getOnPosition().down()).getBlock() == Blocks.AIR
					|| (world.getBlockState(getOnPosition()).getBlock() instanceof StadiumBlock && world.getBlockState(
							new BlockPos(getPositionVec().x, getPositionVec().y - 0.1, getPositionVec().z))
							.getBlock() instanceof StadiumBlock) || (!ConfigManager.isOnlyStadium() && (!ConfigManager.getBlockBlackList().contains(world.getBlockState(
					new BlockPos(getPositionVec().x, getPositionVec().y - 0.1, getPositionVec().z))
					.getBlock()))))) {

				if (!rotationStarted) {
					rotationStartedCount++;
					if (rotationStartedCount == 4) {
						rotationStarted = true;
					}
				} else {
					if (this.getLayer().getItem() instanceof ItemBeyLayerGT) {
						setRotationSpeed(getRotationSpeed() - 0.005F
								* ((ItemBeyDriver) getDriver().getItem()).getFriction(getDriver())
								/ (10 * (((ItemBeyDisc) getDisc().getItem()).getWeight() + ((ItemBeyGTWeight) ItemStack
										.read(getLayer().getTag().getCompound("weight")).getItem()).getWeight())));
					} else {
						setRotationSpeed(
								getRotationSpeed() - 0.005F * ((ItemBeyDriver) getDriver().getItem()).getFriction(getDriver())
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
					world.addParticle(BeyRegistry.SPARKLE, getPosX(), getPosY() + 0.5, getPosZ(), rand.nextInt(5),
							rand.nextInt(5), rand.nextInt(5));
				}
			}

		}
		if (world.isRemote) {
			updatePoints(this);
		}
		super.tick();
	}

	@Override
	public void travel(Vector3d p_213352_1_) {
		super.travel(p_213352_1_);
		if (collidedHorizontally) {
			rotationYaw += 90 * rotationDirection;
		}
		setHorizontalCollision(collidedHorizontally);
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (!world.isRemote) {
			if (!stoped && entityIn instanceof EntityBey) {
				EntityBey bey = (EntityBey) entityIn;
				playHurtSound(DamageSource.GENERIC);
				double x = (getPosX() - entityIn.getPosX()) / 2;
				double y = (getPosY() - entityIn.getPosY()) / 2;
				double z = (getPosZ() - entityIn.getPosZ()) / 2;
				((ServerWorld) world).spawnParticle(BeyRegistry.SPARKLE, getPosX(), getPosY(), getPosZ(), 10, x, y, z,
						10);
				if (((ItemBeyLayer) getLayer().getItem()).getPrimaryAbility() instanceof Absorb
						|| ((ItemBeyLayer) getLayer().getItem()).getSecundaryAbility() instanceof Absorb) {
					setRotationSpeed(getRotationSpeed()
							+ (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
									+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())) / 100);
					bey.setRotationSpeed(bey.getRotationSpeed()
							- (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
									+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
									+ ((EntityBey) entityIn).getBladerLevel()) / (10 + getBladerLevel()));
					if (getRotationSpeed() > getMaxRotationSpeed()) {
						setRotationSpeed(getMaxRotationSpeed());
					}
				} else {
					setRotationSpeed(getRotationSpeed()
							- (((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
									+ ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
									+ ((EntityBey) entityIn).getBladerLevel()) / (10 + getBladerLevel()));
				}
				if (getRadius() == 0 && new Random().nextInt(5) == 1) {
					increaseRadius = true;
					if (new Random().nextInt(100) == 1) {
						attackEntityFrom(DamageSource.causeMobDamage((LivingEntity) entityIn),
								(((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
										- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
										+ ((EntityBey) entityIn).getBladerLevel())
										/ (getBladerLevel() *10));
					} else {
							attackEntityFrom(DamageSource.GENERIC,
									(((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
											+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
											- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
											+ ((EntityBey) entityIn).getBladerLevel())
											/ (getBladerLevel() +10));
					}
				} else {
						attackEntityFrom(DamageSource.GENERIC,
								(((ItemBeyLayer) ((EntityBey) entityIn).getLayer().getItem()).getAttack(getLayer())
										+ ((ItemBeyLayer) getLayer().getItem()).getBurst(getLayer())
										- ((ItemBeyLayer) getLayer().getItem()).getDefense(getLayer())
										+ ((EntityBey) entityIn).getBladerLevel())
										/ (getBladerLevel() +10));
				}
			}
		}
		super.collideWithEntity(entityIn);
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

	public final float getBladerLevel() {
		return ((Float) this.dataManager.get(RADIUS)).floatValue();
	}

	public void setBladerlevel(float bladerLevel) {
		this.dataManager.set(BLADERLEVEL, Float.valueOf(bladerLevel));
	}

	public void setRotationSpeed(float speed) {
		this.dataManager.set(ROTATIONSPEED, Float.valueOf(speed));
	}

	public final float getRotationSpeed() {
		return ((Float) this.dataManager.get(ROTATIONSPEED)).floatValue();
	}

	public void setMaxRotationSpeed(float speed) {
		this.dataManager.set(MAXROTATIONSPEED, Float.valueOf(speed));
	}

	public final float getMaxRotationSpeed() {
		return ((Float) this.dataManager.get(MAXROTATIONSPEED)).floatValue();
	}

	public boolean isStoped() {
		return stoped;
	}
}
