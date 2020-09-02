package com.grillo78.beycraft.entity;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.blocks.StadiumBlock;
import com.grillo78.beycraft.items.ItemBeyDriver;

import com.grillo78.beycraft.util.BeyTypes;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class EntityGoalRotate extends Goal {

	private EntityBey bey;
	private int count = 0;

	public EntityGoalRotate(EntityBey bey) {
		this.bey = bey;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!bey.isStoped()) {
			if (!bey.isStoped() && !bey.isDescending() && bey.isRotationStarted()) {
				if(bey.isOnGround()){
					if (((ItemBeyDriver) bey.getDriver().getItem()).getType(bey.getDriver()) == BeyTypes.ATTACK) {
						if (count < 6) {
							count++;
							if(count%2 == 0)bey.rotationYaw += bey.getRotationSpeed() * -bey.getRotationDirection() * 2
									/ (-bey.getMaxRotationSpeed() * 0.1);
						} else {
							count = 0;
							bey.rotationYaw += 160 * bey.getRotationDirection();
						}
					} else {
						bey.rotationYaw += bey.getRotationSpeed() * -bey.getRotationDirection() * 2
								/ (-bey.getMaxRotationSpeed() * 0.1);
					}
				}
				moveToCenter();
				if (bey.getRadius() > 0 && !bey.isIncreaseRadius()) {
					bey.setRadius(bey.getRadius() - (0.002f * bey.getMaxRotationSpeed() / 10)
							* ((ItemBeyDriver) bey.getInventory().getStackInSlot(2).getItem())
									.getRadiusReduction(bey.getDriver())
							/ (bey.getRotationSpeed() / bey.getMaxRotationSpeed()));
				} else {
					if (bey.isIncreaseRadius()) {
						bey.setRadius(bey.getRadius() + 0.001f
								* ((ItemBeyDriver) bey.getInventory().getStackInSlot(2).getItem())
										.getRadiusReduction(bey.getDriver())
								* bey.getRotationSpeed() / (bey.getMaxRotationSpeed()));
						if (bey.getRadius() >= bey.getMaxRadius() * bey.getRotationSpeed()
								/ bey.getMaxRotationSpeed()) {
							bey.setIncreaseRadius(false);
						}
					} else {
						bey.setRadius(0);
					}
				}
			}

			if (bey.getRadius() != 0) {
				if(((ItemBeyDriver)bey.getDriver().getItem()).getType(bey.getDriver()) != BeyTypes.ATTACK){
					bey.move(MoverType.SELF, new Vector3d(bey.getLookVec().x * bey.getRadius() / 2.1, 0,
							bey.getLookVec().z * bey.getRadius() / 2.1));
				} else {
					bey.move(MoverType.SELF, new Vector3d(bey.getLookVec().x * bey.getRadius() / 2.1, 0,
							bey.getLookVec().z * bey.getRadius() / 2.1));
				}
			}

		}
		return !bey.isStoped();
	}

	private void moveToCenter() {
		if (bey.world.getBlockState(
				new BlockPos(bey.getPositionVec().x, bey.getPositionVec().y + 1 - 0.0625 * 7, bey.getPositionVec().z))
				.getBlock() instanceof StadiumBlock) {
			switch (bey.world.getBlockState(new BlockPos(bey.getPositionVec().x, bey.getPositionVec().y + 1 - 0.0625 * 7, bey.getPositionVec().z))
					.get(StadiumBlock.PART).getID()) {
			case 0:
				if (bey.getRadius() == 0)
					bey.move(MoverType.SELF, new Vector3d(0.1, 0, 0.1));
				break;
			case 1:
				bey.move(MoverType.SELF, new Vector3d(
						-0.01 * ((ItemBeyDriver) bey.getDriver().getItem()).getRadiusReduction(bey.getDriver()), 0, 0));
				break;
			case 2:
				if (bey.getRadius() == 0)
					bey.move(MoverType.SELF, new Vector3d(0.1, 0, -0.1));
				break;
			case 3:
				bey.move(MoverType.SELF, new Vector3d(0, 0,
						0.01 * ((ItemBeyDriver) bey.getDriver().getItem()).getRadiusReduction(bey.getDriver())));
				break;
			case 4:
				bey.move(MoverType.SELF,
						new Vector3d(
								(new BlockPos(bey.getPosX(), bey.getPosY(), bey.getPosZ()).getX() + 0.5
										- bey.getPositionVec().x) / 4,
								0, (new BlockPos(bey.getPosX(), bey.getPosY(), bey.getPosZ()).getZ() + 0.5
										- bey.getPositionVec().z) / 4));
				break;
			case 5:
				bey.move(MoverType.SELF, new Vector3d(0, 0,
						-0.01 * ((ItemBeyDriver) bey.getDriver().getItem()).getRadiusReduction(bey.getDriver())));
				break;
			case 6:
				if (bey.getRadius() == 0)
					bey.move(MoverType.SELF, new Vector3d(-0.1, 0, 0.1));
				break;
			case 7:
				bey.move(MoverType.SELF, new Vector3d(
						0.01 * ((ItemBeyDriver) bey.getDriver().getItem()).getRadiusReduction(bey.getDriver()), 0, 0));
				break;
			case 8:
				if (bey.getRadius() == 0)
					bey.move(MoverType.SELF, new Vector3d(-0.1, 0, -0.1));
				break;
			}
		}
	}
}
