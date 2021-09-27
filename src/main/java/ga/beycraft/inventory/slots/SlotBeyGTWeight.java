package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyGTChipWeight;
import ga.beycraft.items.ItemBeyGTWeight;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyGTWeight extends SlotItemHandler {

	public SlotBeyGTWeight(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyGTWeight && !(getItemHandler().getStackInSlot(2).getItem() instanceof ItemBeyGTChipWeight);
	}
}
