package grillo78.beycraft.common.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class LockedSlot extends SlotItemHandler {
    public LockedSlot(InvWrapper playerinventory, int row, int x, int y) {
        super(playerinventory,row,x,y);
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        return false;
    }
}