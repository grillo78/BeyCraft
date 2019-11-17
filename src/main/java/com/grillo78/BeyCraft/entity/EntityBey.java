package com.grillo78.BeyCraft.entity;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.util.SoundHandler;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBey extends EntityCreature{

	public ItemStack layer;
	public ItemStack disk;
	public ItemStack driver;

	public EntityBey(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.height = 0.253F;
	}
	
	public EntityBey(World worldIn, ItemStack layerIn, ItemStack diskIn, ItemStack driverIn) {
		this(worldIn);
		layer = layerIn;
		disk = diskIn;
		driver = driverIn;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
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
	
	public float getEyeHeight()
    {
        return this.height;
    }
	
	@Override
	public void onRemovedFromWorld() {
		BeyCraft.logger.info("Bey die");
		super.onRemovedFromWorld();
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundHandler.BEY_HIT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundHandler.BEY_HIT;
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
	
//	@Override
//	public void setEntityBoundingBox(AxisAlignedBB bb) {
//		bb = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
//		super.setEntityBoundingBox(bb);
//	}
}
