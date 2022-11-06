package com.beycraft.common.item;

import com.beycraft.common.container.LauncherContainer;
import com.beycraft.common.container.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class DualLauncherItem extends LauncherItem{


    public DualLauncherItem(Properties p_i48487_1_) {
        super(p_i48487_1_, null);
    }

    @Override
    public LauncherContainer generateContainer(int id, ItemStack stack, PlayerInventory playerInventory) {
        return new LauncherContainer(ModContainers.DUAL_LAUNCHER, id, stack, playerInventory);
    }
}
