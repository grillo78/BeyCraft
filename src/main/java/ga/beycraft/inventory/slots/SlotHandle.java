package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemLauncherHandle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHandle extends SlotItemHandler{

	public SlotHandle(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemLauncherHandle;
	}
	
}
