package ga.beycraft.tileentity;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.items.*;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class BeyCreatorTileEntity extends TileEntity implements ITickableTileEntity {

    private static int MAXPROGRESS = 1000;
    private int actualProgress = 0;
    private LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(2));
    private Random rand = new Random();

    public BeyCreatorTileEntity() {
        super(BeyCraftRegistry.BEYCREATORTILEENTITYTYPE);
    }

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
        super.deserializeNBT(tag);
        this.readNetwork(tag);
    }

    private CompoundNBT writeNetwork(CompoundNBT compound) {
        inventory.ifPresent(h -> {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("ActualProgress", actualProgress);
        return compound;
    }

    private void readNetwork(CompoundNBT compound) {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        actualProgress = compound.getInt("ActualProgress");
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            inventory.ifPresent(h -> {
                if (!h.getStackInSlot(1).isEmpty() && !h.getStackInSlot(0).isEmpty()) {
                    if ((h.getStackInSlot(1).getItem() instanceof ItemBeyLayer || h.getStackInSlot(1).getItem() instanceof ItemBeyFrame || h.getStackInSlot(1).getItem() instanceof ItemBeyDriver || h.getStackInSlot(1).getItem() instanceof ItemBeyGTChip) && h.getStackInSlot(0).getItem() == BeyCraftRegistry.PLASTIC) {
                        updateProcess(h);
                    } else {
                        if ((h.getStackInSlot(1).getItem() instanceof ItemBeyDisc || h.getStackInSlot(1).getItem() instanceof ItemBeyGTWeight) && h.getStackInSlot(0).getItem() == Items.IRON_INGOT) {
                            updateProcess(h);
                        }
                    }
                } else {
                    actualProgress = 0;
                }
            });
        } else {
            inventory.ifPresent(h -> {
                if (!h.getStackInSlot(1).isEmpty() && !h.getStackInSlot(0).isEmpty()) {
                    if ((h.getStackInSlot(1).getItem() instanceof ItemBeyLayer || h.getStackInSlot(1).getItem() instanceof ItemBeyFrame || h.getStackInSlot(1).getItem() instanceof ItemBeyDriver || h.getStackInSlot(1).getItem() instanceof ItemBeyGTChip) && h.getStackInSlot(0).getItem() == BeyCraftRegistry.PLASTIC) {

                        level.addParticle(BeyCraftRegistry.SPARKLE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.25, worldPosition.getZ() + 0.5, rand.nextInt(5), rand.nextInt(5), rand.nextInt(5));
                    } else {
                        if ((h.getStackInSlot(1).getItem() instanceof ItemBeyDisc || h.getStackInSlot(1).getItem() instanceof ItemBeyGTWeight) && h.getStackInSlot(0).getItem() == Items.IRON_INGOT) {
                            level.addParticle(BeyCraftRegistry.SPARKLE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.15, worldPosition.getZ() + 0.5, rand.nextInt(5), rand.nextInt(5), rand.nextInt(5));
                        }
                    }
                }
            });
        }
    }

    private void updateProcess(IItemHandler h) {
        actualProgress++;
        if (actualProgress >= MAXPROGRESS) {
            actualProgress = 0;
            h.getStackInSlot(0).shrink(1);
            h.insertItem(0, h.getStackInSlot(1).copy(), false);
            h.getStackInSlot(1).shrink(1);
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
        }
    }

    public int getActualProgress() {
        return actualProgress;
    }


    public void setActualProgress(int actualProgress) {
        this.actualProgress = actualProgress;
    }
}
