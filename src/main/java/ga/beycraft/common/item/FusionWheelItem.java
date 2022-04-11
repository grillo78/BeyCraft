package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import ga.beycraft.client.item.GenericPartRenderer;
import ga.beycraft.common.tab.BeycraftItemGroup;

public class FusionWheelItem extends BeyPartItem{
    public FusionWheelItem(String name, String displayName, float attack, float defense, float weight, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.LAYERS, new Properties().setISTER(()-> GenericPartRenderer::new));
    }
}
