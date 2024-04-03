package grillo78.beycraft.common.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class Launcher implements ILauncher {
    private IItemHandler inventory = new ItemStackHandler(3);
    private int red = 255;
    private int green = 255;
    private int blue = 255;

    @Override
    public void setColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public IItemHandler getInventory() {
        return inventory;
    }

    @Override
    public int getRed() {
        return red;
    }

    @Override
    public int getGreen() {
        return green;
    }

    @Override
    public int getBlue() {
        return blue;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put("inventory",CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
        compoundNBT.putInt("red", red);
        compoundNBT.putInt("green", green);
        compoundNBT.putInt("blue", blue);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, nbt.get("inventory"));
        red = nbt.getInt("red");
        green = nbt.getInt("green");
        blue = nbt.getInt("blue");
    }
}
