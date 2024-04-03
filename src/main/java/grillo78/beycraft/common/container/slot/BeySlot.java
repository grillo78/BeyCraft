package grillo78.beycraft.common.container.slot;

import grillo78.beycraft.common.item.LayerItem;
import grillo78.beycraft.utils.Direction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BeySlot extends SlotItemHandler {

    private Direction direction;

    public BeySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Direction direction) {
        super(itemHandler, index, xPosition, yPosition);
        this.direction = direction;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof LayerItem && ((LayerItem)stack.getItem()).isBeyAssembled(stack) && ((LayerItem)stack.getItem()).getRotationDirection(stack) == direction;
    }
}
