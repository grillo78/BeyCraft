package com.beycraft.common.block_entity;

import com.beycraft.common.item.BeyPartItem;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BeycreatorTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int MAX_PROGRESS = 1000;
    private ItemStack slot = ItemStack.EMPTY;
    private Item resultItem = Items.AIR;
    private int index = 0;
    private int progress = 0;

    public BeycreatorTileEntity() {
        super(ModTileEntities.BEYCREATOR);
    }

    public Item getResultItem() {
        return resultItem;
    }

    public ItemStack getSlot() {
        return slot;
    }

    public void increaseIndex(int value) {
        if (index + value == BeyPartItem.getParts().size())
            index = 0;
        else if (index+value == -1)
            index = BeyPartItem.getParts().size()-1;
        else
            index += value;
    }

    public void setSlot(ItemStack slot) {
        this.slot = slot;
    }

    public int getIndex() {
        return index;
    }

    public void select() {
        resultItem = BeyPartItem.getParts().get(index);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        readNetwork(p_230337_2_);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        writeNetwork(compound);
        return super.save(compound);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.writeNetwork(new CompoundNBT()));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return writeNetwork(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readNetwork(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.readNetwork(tag);
    }

    private CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.put("slot", slot.serializeNBT());
        compound.putString("result", resultItem.getRegistryName().toString());
        compound.putInt("index", index);
        compound.putInt("progress", progress);
        return compound;
    }

    private void readNetwork(CompoundNBT compound) {
        slot = ItemStack.of(compound.getCompound("slot"));
        resultItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("result")));
        index = compound.getInt("index");
        progress = compound.getInt("progress");
    }

    @Override
    public void tick() {
        if (resultItem instanceof BeyPartItem && ((BeyPartItem) resultItem).canBeCrafted(slot.getItem())){
            if (progress<MAX_PROGRESS)
                progress++;
            else {
                progress = 0;
                slot = new ItemStack(resultItem);
                resultItem = Items.AIR;
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
            }
        }
    }
}
