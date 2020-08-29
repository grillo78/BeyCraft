package com.grillo78.beycraft.abilities;

import java.util.HashMap;
import java.util.List;

import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.items.ItemBeyPart;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author grillo78
 *
 */
public class MultiMode extends Ability {

	private final List<Mode> modes;
	private final HashMap<String, Mode> modeHashMap = new HashMap<>();

	public MultiMode(List<Mode> modes) {
		this.modes = modes;
		for (Mode mode:modes) {
			modeHashMap.put(mode.getName(), mode);
		}
	}

	public HashMap<String, Mode> getModeHashMap() {
		return modeHashMap;
	}

	public List<Mode> getModes() {
		return modes;
	}

	@Override
	public void executeAbility(EntityBey entity) {}

	@Override
	public void executeAbility(ItemStack stack) {
		if(!stack.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("Mode", modes.get(0).getName());
			stack.setTag(compound);
		}
		int count = 0;
		for (int i = 0; i < modes.size(); i++) {
			if(stack.getTag().getString("Mode").equals(modes.get(i).getName())){
				count = i;
			}
		}
		if((count+1)<modes.size()) {
			stack.getTag().putString("Mode", modes.get(count+1).getName());
		} else {
			stack.getTag().putString("Mode", modes.get(0).getName());
		}
	}

	public static class Mode {
		private String name;
		private float[] values;
		
		public Mode(String input){
			this.name =input.substring(0, input.indexOf('('));
			String[] valuesStr = input.substring(input.indexOf('(')+1, input.indexOf(')')).split(",");
			values = new float[valuesStr.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = Float.valueOf(valuesStr[i]);
			}
		}
		
		public String getName(){
			return name;
		}

		public float[] getValues() {
			return values;
		}
	}
}
