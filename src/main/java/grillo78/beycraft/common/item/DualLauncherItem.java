package grillo78.beycraft.common.item;

import grillo78.beycraft.common.container.LauncherContainer;
import grillo78.beycraft.common.container.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class DualLauncherItem extends LauncherItem{


    public DualLauncherItem(Properties p_i48487_1_) {
        super(p_i48487_1_, null);
    }

    @Override
    public LauncherContainer generateContainer(int id, ItemStack stack, PlayerInventory playerInventory, Hand hand) {

        return new LauncherContainer(ModContainers.DUAL_LAUNCHER, id, stack, hand == Hand.MAIN_HAND, playerInventory);
    }
}
