package com.grillo78.BeyCraft.tileentity;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ExpositoryTileEntity extends TileEntity  {

	private LazyOptional<IItemHandler> inventory =  LazyOptional.of(() -> new ItemStackHandler(3));
	
	public ExpositoryTileEntity() {
		super(BeyCraft.EXPOSITORYTILEENTITYTYPE);
	}

	/**
	 * @return the inventory
	 */
	public LazyOptional<IItemHandler> getInventory() {
		return inventory;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		
		return compound;
	}
}
