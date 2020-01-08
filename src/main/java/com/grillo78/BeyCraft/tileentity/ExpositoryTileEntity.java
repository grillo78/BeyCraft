package com.grillo78.BeyCraft.tileentity;

import com.grillo78.BeyCraft.inventory.ExpositoryInventory;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ExpositoryTileEntity extends TileEntity implements ISidedInventoryProvider {

	public ExpositoryTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public ISidedInventory createInventory(BlockState p_219966_1_, IWorld p_219966_2_, BlockPos p_219966_3_) {
		// TODO Auto-generated method stub
		return new ExpositoryInventory();
	}

}
