package com.grillo78.beycraft.abilities;

import java.util.List;

import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.items.ItemBeyPart;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author a19guillermong
 *
 */
public class MultiType extends Ability {

	private final List<BeyTypes> types;

	/**
	 * 
	 */
	public MultiType(List<BeyTypes> types) {
		this.types = types;

	}

	public List<BeyTypes> getTypes() {
		return types;
	}

	@Override
	public void executeAbility(EntityBey entity) {}

	@Override
	public void executeAbility(ItemStack stack) {
		if(!stack.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("Type", ((ItemBeyPart)stack.getItem()).getType().name());
			stack.setTag(compound);
		}
		int count = 0;
		for (int i = 0; i < types.size(); i++) {
			if(stack.getTag().getString("Type").equals(types.get(i).name())){
				count = i;
			}
		}
		if((count+1)<types.size()) {
			stack.getTag().putString("Type", types.get(count+1).name());
		} else {
			stack.getTag().putString("Type", types.get(0).name());
		}
	}
}
