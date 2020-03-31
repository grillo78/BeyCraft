package com.grillo78.beycraft.tileentity;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyFrame;
import com.grillo78.beycraft.items.ItemBeyLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Random;

public class BeyCreatorTileEntity extends TileEntity implements ITickableTileEntity {

    private static int MAXPROGRESS = 1000;
    private int actualProgress = 0;
    private LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(2));
    private Random rand = new Random();

    public BeyCreatorTileEntity() {
        super(BeyRegistry.BEYCREATORTILEENTITYTYPE);
    }

    public LazyOptional<IItemHandler> getInventory() {
        return inventory;
    }

    @Override
    public void read(CompoundNBT compound) {
        readNetwork(compound);
        super.read(compound);
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
    public void handleUpdateTag(CompoundNBT tag) {
        super.read(tag);
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
        if (!world.isRemote) {
            inventory.ifPresent(h -> {
                if (!h.getStackInSlot(1).isEmpty()) {
                    if ((h.getStackInSlot(1).getItem() instanceof ItemBeyLayer || h.getStackInSlot(1).getItem() instanceof ItemBeyFrame || h.getStackInSlot(1).getItem() instanceof ItemBeyDriver) && h.getStackInSlot(0).getItem() == BeyRegistry.PLASTIC) {
                        updateProcess(h);
                    } else {
                        if (h.getStackInSlot(1).getItem() instanceof ItemBeyDisc && h.getStackInSlot(0).getItem() == Items.IRON_INGOT) {
                            updateProcess(h);
                        }
                    }
                } else {
                    actualProgress = 0;
                }
            });
        }
    }

    private void updateProcess(IItemHandler h) {
        actualProgress++;
        BeyCraft.logger.info(actualProgress);
        world.addParticle(BeyRegistry.SPARKLE, pos.getX(), pos.getY(), pos.getZ(), rand.nextInt(5), rand.nextInt(5), rand.nextInt(5));
        if (actualProgress >= MAXPROGRESS) {
            actualProgress = 0;
            h.getStackInSlot(0).shrink(1);
            h.insertItem(0, h.getStackInSlot(1).copy(), false);
            h.getStackInSlot(1).shrink(1);
        }
        this.markDirty();
    }

    public int getActualProgress() {
        return actualProgress;
    }


    public void setActualProgress(int actualProgress) {
        this.actualProgress = actualProgress;
    }
}
