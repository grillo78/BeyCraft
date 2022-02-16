package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyGodChip;
import ga.beycraft.items.ItemFusionWheel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFusionWheel extends SlotItemHandler{

	public SlotFusionWheel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemFusionWheel;
	}
}
