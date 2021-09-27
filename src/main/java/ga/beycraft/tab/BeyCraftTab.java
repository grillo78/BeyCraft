package ga.beycraft.tab;

import ga.beycraft.BeyRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BeyCraftTab extends ItemGroup{

	
	public BeyCraftTab(String label) {
		super(label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(BeyRegistry.LAUNCHER);
	}
}
