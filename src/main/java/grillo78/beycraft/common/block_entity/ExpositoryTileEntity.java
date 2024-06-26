package grillo78.beycraft.common.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ExpositoryTileEntity extends TileEntity  {

    private LazyOptional<IItemHandler> inventory =  LazyOptional.of(() -> new ItemStackHandler(1));

    public ExpositoryTileEntity() {
        super(ModTileEntities.EXPOSITORYTILEENTITYTYPE);
    }

    /**
     * @return the inventory
     */
    public LazyOptional<IItemHandler> getInventory() {
        return inventory;
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
        super.deserializeCaps(tag);
        this.readNetwork(tag);
    }

    private CompoundNBT writeNetwork(CompoundNBT compound){
        inventory.ifPresent(h-> {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv",tag);
        });
        return compound;
    }

    private void readNetwork(CompoundNBT compound){
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h->((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    }
}
