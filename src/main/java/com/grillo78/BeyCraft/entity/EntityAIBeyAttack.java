package com.grillo78.BeyCraft.entity;

import java.util.Random;

import com.grillo78.BeyCraft.items.ItemBeyLayer;

import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIBeyAttack extends EntityAIAttackMelee {

	private EntityBey bey;

	/**
	 * @param creature
	 * @param speedIn
	 * @param useLongMemory
	 */
	public EntityAIBeyAttack(EntityBey bey) {
		super(bey, 10, false);
		this.bey = bey;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (bey.rotationSpeed != 0) {
			if (bey.rotationSpeed < 0) {
				float damage = new Random().nextFloat();
				if (((ItemBeyLayer) bey.layer.getItem()).canAbsorb(bey)) {
					bey.rotationSpeed -= damage;
				}
				bey.rotationSpeed += damage;
				bey.radius = 0.1f;
			} else {
				bey.rotationSpeed = 0;
//				if (bey.rotationSpeed != 0) {
//					bey.move(MoverType.SELF, bey.getLookVec().x, bey.getLookVec().y,
//							bey.getLookVec().z);
//				}
			}
			return super.shouldContinueExecuting();
		}
		return false;
	}

}
