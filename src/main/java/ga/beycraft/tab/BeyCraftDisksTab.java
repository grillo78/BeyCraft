package ga.beycraft.tab;

import ga.beycraft.BeyCraftRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeyCraftDisksTab extends ItemGroup{

	
	public BeyCraftDisksTab(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BeyCraftRegistry.DISCICON);
	}
}
