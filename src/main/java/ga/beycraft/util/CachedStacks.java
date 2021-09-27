package ga.beycraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;

public class CachedStacks {

    public static CachedStacks INSTANCE = new CachedStacks();
    private HashMap<CompoundNBT, ItemStack> stacks = new HashMap<>();

    public HashMap<CompoundNBT, ItemStack> getStacks() {
        return stacks;
    }
}
