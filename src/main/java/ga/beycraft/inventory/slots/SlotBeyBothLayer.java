package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyLayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyBothLayer extends SlotItemHandler {

    public SlotBeyBothLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean isValid = false;

        if (stack.getItem() instanceof ItemBeyLayer) {
            isValid = ((ItemBeyLayer) stack.getItem()).isBeyAssembled(stack);
        }
        return isValid;
    }

}
