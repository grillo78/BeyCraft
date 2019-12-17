package com.grillo78.BeyCraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAILookToBey extends EntityAIWatchClosest {

	private final EntityBey bey;

	public EntityAILookToBey(EntityBey entityIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
		super(entityIn, watchTargetClass, maxDistance);
		bey = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.closestEntity.isEntityAlive())
        {
            return false;
        }
        else if (this.entity.getDistanceSq(this.closestEntity) > (double)(this.maxDistanceForPlayer * this.maxDistanceForPlayer))
        {
            return false;
        } else {
        	return true;
        }
	}
}
