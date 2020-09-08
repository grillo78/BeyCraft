package com.grillo78.beycraft.tileentity;

import com.grillo78.beycraft.BeyRegistry;

import com.grillo78.beycraft.gui.RobotGUI;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RobotTileEntity extends TileEntity  {

	private LazyOptional<IItemHandler> inventory =  LazyOptional.of(() -> new ItemStackHandler(1));
	private AxisAlignedBB renderBox;

	private int bladerLevel = 0;

	public RobotTileEntity() {
		super(BeyRegistry.ROBOTTILEENTITYTYPE);
	}

	@OnlyIn(Dist.CLIENT)
	public void openGUI(){
		Minecraft.getInstance().displayGuiScreen(new RobotGUI(new StringTextComponent(""), this));
	}

	/**
	 * @return the inventory
	 */
	public LazyOptional<IItemHandler> getInventory() {
		return inventory;
	}

	public int getBladerLevel() {
		return bladerLevel;
	}

	public void setBladerLevel(int bladerLevel) {
		this.bladerLevel = bladerLevel;
	}

	@Override
	public void read(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
		super.read(p_230337_1_, p_230337_2_);
		readNetwork(p_230337_2_);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if(renderBox == null){
			renderBox = new AxisAlignedBB(pos.down().getX(),pos.down().getY(),pos.down().getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1);
		}
		return renderBox;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		writeNetwork(compound);
		return super.write(compound);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.writeNetwork(new CompoundNBT()));
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return writeNetwork(super.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.readNetwork(pkt.getNbtCompound());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.deserializeNBT(tag);
		this.readNetwork(tag);
	}

	private CompoundNBT writeNetwork(CompoundNBT compound){
		compound.putInt("bladerLevel",bladerLevel);
		inventory.ifPresent(h-> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("inv",tag);
		});
		return compound;
	}

	private void readNetwork(CompoundNBT compound){
		CompoundNBT invTag = compound.getCompound("inv");
		bladerLevel = compound.getInt("bladerLevel");
		inventory.ifPresent(h->((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
	}
}
