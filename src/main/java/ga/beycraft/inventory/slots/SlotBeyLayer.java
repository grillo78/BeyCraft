package ga.beycraft.inventory.slots;

import ga.beycraft.items.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLayer extends SlotItemHandler {

    private ItemStack launcher;

    public SlotBeyLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack launcher) {
        super(itemHandler, index, xPosition, yPosition);
        this.launcher = launcher;
    }


    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean isValid = false;

        if (stack.getItem() instanceof ItemBeyLayer)
            isValid = ((ItemBeyLayer) stack.getItem()).isBeyAssembled(stack);
        if (launcher.getItem() instanceof ItemDualLauncher)
            isValid = isValid && (stack.getItem() instanceof ItemBeyLayerDual || stack.getItem() instanceof ItemBeyLayerGTDual || stack.getItem() instanceof ItemBeyLayerGTDualNoWeight);
        else
            isValid = isValid && ((ItemBeyLayer)stack.getItem()).getRotationDirection() == ((ItemLauncher)launcher.getItem()).getRotation(launcher);
        return isValid;
    }

}
