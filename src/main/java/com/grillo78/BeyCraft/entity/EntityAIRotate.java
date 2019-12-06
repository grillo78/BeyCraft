package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.blocks.StadiumBlock;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIRotate extends EntityAIBase {

	private final EntityBey bey;

	public EntityAIRotate(EntityBey bey) {
		this.bey = bey;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!bey.isStoped()) {
			if (bey.onGround) {
				bey.move(MoverType.SELF, bey.getLookVec().x * bey.radius * 1.5, 0,
						bey.getLookVec().z * bey.radius * 1.5);
				if (!bey.isMovementStarted()) {
					bey.setMovementStarted(true);
				}if (!bey.world.isRemote) {
					if (bey.world.getBlockState(
							new BlockPos(bey.getPositionVector().x + 0.23, bey.getPositionVector().y, bey.getPositionVector().z))
							.getBlock() != BeyRegistry.STADIUM) {
						bey.rotationYaw = 90;
					}
					if (bey.world.getBlockState(
							new BlockPos(bey.getPositionVector().x - 0.23, bey.getPositionVector().y, bey.getPositionVector().z))
							.getBlock() != BeyRegistry.STADIUM) {
						bey.rotationYaw = -90;
					}
					if (bey.world.getBlockState(
							new BlockPos(bey.getPositionVector().x, bey.getPositionVector().y, bey.getPositionVector().z + 0.23))
							.getBlock() != BeyRegistry.STADIUM) {
						bey.rotationYaw = 180;
					}
					if (bey.world.getBlockState(
							new BlockPos(bey.getPositionVector().x, bey.getPositionVector().y, bey.getPositionVector().z - 0.23))
							.getBlock() != BeyRegistry.STADIUM) {
						bey.rotationYaw = 0;
					}
				}
				if (bey.radius == 0 && bey.world
						.getBlockState(new BlockPos(bey.getPositionVector().x,
								bey.getPositionVector().y + 1 - 0.0625 * 7, bey.getPositionVector().z))
						.getBlock() instanceof StadiumBlock) {
					switch (BeyRegistry.STADIUM
							.getMetaFromState(bey.world.getBlockState(new BlockPos(bey.getPositionVector().x,
									bey.getPositionVector().y - 0.1, bey.getPositionVector().z)))) {
					case 0:

						bey.move(MoverType.SELF, 0.1, 0, 0.1);
						break;
					case 1:
						bey.move(MoverType.SELF, -0.1, 0, 0);
						break;
					case 2:
						bey.move(MoverType.SELF, 0.1, 0, -0.1);
						break;
					case 3:
						bey.move(MoverType.SELF, 0, 0, 0.1);
						break;
					case 4:
						bey.move(MoverType.SELF, (bey.getPosition().getX() + 0.5 - bey.getPositionVector().x) / 4, 0,
								(bey.getPosition().getZ() + 0.5 - bey.getPositionVector().z) / 4);
						break;
					case 5:
						bey.move(MoverType.SELF, 0, 0, -0.1);
						break;
					case 6:
						bey.move(MoverType.SELF, -0.1, 0, 0.1);
						break;
					case 7:
						bey.move(MoverType.SELF, 0.1, 0, 0);
						break;
					case 8:
						bey.move(MoverType.SELF, -0.1, 0, -0.1);
						break;
					}
				}
			}
		}
		return super.shouldContinueExecuting();
	}
}
