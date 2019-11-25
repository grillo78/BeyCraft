package com.grillo78.BeyCraft.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;

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
				bey.move(MoverType.SELF, bey.getLookVec().x * bey.radius, 0,
						bey.getLookVec().z * bey.radius);
			}
		}
		return super.shouldContinueExecuting();
	}
}
