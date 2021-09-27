package ga.beycraft.inventory.slots;

import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.items.ItemLauncher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBeyLauncher extends SlotItemHandler {

	public SlotBeyLauncher(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		boolean isValid = false;

		if(stack.getItem() instanceof ItemLauncher){
			if(!stack.hasTag() || !(ItemStack.of((CompoundNBT) stack.getTag().get("bey")).getItem() instanceof ItemBeyLayer)){
				isValid = true;
			}
		}
		return isValid;
	}

}
