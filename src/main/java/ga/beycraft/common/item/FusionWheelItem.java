package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.abilities.AbilityType;
import ga.beycraft.common.tab.BeycraftCreativeModeTab;
import net.minecraft.world.item.CreativeModeTab;

public class FusionWheelItem extends BeyPartItem{
    public FusionWheelItem(String name, String displayName, float attack, float defense, float weight, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftCreativeModeTab.LAYERS, new Properties());
    }
}
