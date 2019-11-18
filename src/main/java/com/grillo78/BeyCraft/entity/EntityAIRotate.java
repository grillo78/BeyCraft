package com.grillo78.BeyCraft.entity;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRotate extends EntityAIBase{

	private final EntityBey bey;
	
	public EntityAIRotate(EntityBey bey) {
		this.bey = bey;
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void updateTask() {
		bey.getLookHelper().setLookPosition(bey.posX, bey.posY, bey.posZ, bey.rotationYawHead+5, 0);
	}
}
