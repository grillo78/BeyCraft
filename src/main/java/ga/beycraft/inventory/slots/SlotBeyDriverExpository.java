package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyDriver;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotBeyDriverExpository extends Slot {

	public SlotBeyDriverExpository(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemBeyDriver;
	}
}
