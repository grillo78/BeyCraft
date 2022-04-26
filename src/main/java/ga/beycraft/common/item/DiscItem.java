package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import ga.beycraft.client.item.GenericPartRenderer;
import ga.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.item.ItemStack;

public class DiscItem extends BeyPartItem{

    private float attack;
    private float defense;
    private float weight;

    public DiscItem(String name, String displayName, float attack, float defense, float weight, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.DISCS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
    }

    public float getAttack(ItemStack disc) {
        return attack;
    }

    public float getDefense(ItemStack disc) {
        return defense;
    }

    public float getWeight(ItemStack disc) {
        return weight;
    }
}
