package ga.beycraft.abilities;

import ga.beycraft.entity.EntityBey;
import ga.beycraft.items.ItemBeyPart;
import ga.beycraft.util.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.List;

/**
 * @author grillo78
 *
 */
public class MultiType extends Ability {

	private final List<Type> types;
	private final HashMap<String, Type> typeHashMap = new HashMap<>();

	public MultiType(List<Type> types) {
		this.types = types;
		for (Type type:types) {
			typeHashMap.put(type.getType().name(), type);
		}
	}

	public HashMap<String, Type> getTypeHashMap() {
		return typeHashMap;
	}

	public List<Type> getTypes() {
		return types;
	}

	@Override
	public void executeAbility(EntityBey entity) {}

	@Override
	public void executeAbility(ItemStack stack) {
		if(!stack.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("Type", ((ItemBeyPart)stack.getItem()).getType(stack).name());
			stack.setTag(compound);
		}
		int count = 0;
		for (int i = 0; i < types.size(); i++) {
			if(stack.getTag().getString("Type").equals(types.get(i).getType().name())){
				count = i;
			}
		}
		if((count+1)<types.size()) {
			stack.getTag().putString("Type", types.get(count+1).getType().name());
		} else {
			stack.getTag().putString("Type", types.get(0).getType().name());
		}
	}
}
