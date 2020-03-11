package com.grillo78.BeyCraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.world.World;

public class EntityBeyFactory implements IFactory<EntityBey> {
	
	public static EntityBeyFactory INSTANCE;
	/** Returns the main Entity Factory instance or creates it and returns it. **/
	public static EntityBeyFactory getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EntityBeyFactory();
		}
		return INSTANCE;
	}

	@Override
	public EntityBey create(EntityType<EntityBey> type, World world) {
		return new EntityBey(type, world);
	}

}
