package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyCraft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.world.World;

public class EntityBey extends EntityCreature{

	public EntityBey(World worldIn) {
		super(worldIn);
		this.setSize(100, 100);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAIWander(this, 1.0D));
		super.initEntityAI();
	}
	
	
	@Override
	public void onRemovedFromWorld() {
		BeyCraft.logger.info("Valtryek muerto");
		super.onRemovedFromWorld();
	}

}
