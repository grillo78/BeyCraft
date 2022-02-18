package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.abilities.AbilityType;
import ga.beycraft.common.tab.BeycraftCreativeModeTab;

public class DriverItem extends BeyPartItem{
    public DriverItem(String name, String displayName, float friction, float radiusReduction, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftCreativeModeTab.DRIVERS, new Properties());
    }
}
