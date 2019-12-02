package com.grillo78.BeyCraft.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
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
		if (bey.rotationSpeed < 0) {
			if (bey.onGround) {
				bey.move(MoverType.SELF, bey.getLookVec().x * bey.radius * 1.5, 0,
						bey.getLookVec().z * bey.radius * 1.5);
			}
		}
		return super.shouldContinueExecuting();
	}
}
