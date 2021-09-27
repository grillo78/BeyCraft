package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyGTChip;
import ga.beycraft.items.ItemBeyGTWeight;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyGTChipNoWeight extends SlotItemHandler{

	public SlotBeyGTChipNoWeight(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyGTChip && !(stack.getItem() instanceof ItemBeyGTWeight);
	}
}
