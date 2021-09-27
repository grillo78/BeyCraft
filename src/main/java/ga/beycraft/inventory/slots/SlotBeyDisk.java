package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyDisc;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyDisk extends SlotItemHandler{

	public SlotBeyDisk(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyDisc;
	}
}
