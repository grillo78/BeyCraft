package com.grillo78.BeyCraft.tileentity;

import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.inventory.ExpositoryContainer;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class ExpositoryTileEntity extends TileEntityLockable implements ISidedInventory {

	private NonNullList<ItemStack> printerItemStackArray = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

	@Override
	public int getSizeInventory() {
		return printerItemStackArray.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.printerItemStackArray) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return printerItemStackArray.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		return ItemStackHelper.getAndSplit(this.printerItemStackArray, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.printerItemStackArray, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.printerItemStackArray.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.printerItemStackArray.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0) {
			ItemStack itemstack = this.printerItemStackArray.get(1);
			return itemstack.getItem() instanceof ItemBeyDriver;
		} else if (index == 1) {
			ItemStack itemstack = this.printerItemStackArray.get(1);
			return itemstack.getItem() instanceof ItemBeyDisk;
		} else {
			ItemStack itemstack = this.printerItemStackArray.get(1);
			return itemstack.getItem() instanceof ItemBeyDriver;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		this.printerItemStackArray.clear();

	}

	@Override
	public String getName() {
		String name = "";
		if (this.getStackInSlot(0).getCount() != 0) {
			name = this.getStackInSlot(0).getDisplayName() + name;
		}
		if (this.getStackInSlot(1).getCount() != 0) {
			name = name + " " + this.getStackInSlot(1).getDisplayName() + " ";
		}
		if (this.getStackInSlot(2).getCount() != 0) {
			name = name + this.getStackInSlot(2).getDisplayName();
		}
		return name;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ExpositoryContainer(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return Reference.MODID + ":expository";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Layer", this.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		compound.setTag("Disk", this.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
		compound.setTag("Driver", this.getStackInSlot(2).writeToNBT(new NBTTagCompound()));
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.setInventorySlotContents(0, new ItemStack(compound.getCompoundTag("Layer")));
		this.setInventorySlotContents(1, new ItemStack(compound.getCompoundTag("Disk")));
		this.setInventorySlotContents(2, new ItemStack(compound.getCompoundTag("Driver")));
	}
}
