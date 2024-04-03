package grillo78.beycraft.common.item;

import grillo78.beycraft.BeyTypes;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.item.GenericPartRenderer;
import grillo78.beycraft.common.ability.AbilityType;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class DiscItem extends BeyPartItem{

    private float attack;
    private float defense;
    private float weight;

    public DiscItem(String name, String displayName, float attack, float defense, float weight, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.DISCS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        ModItems.ItemCreator.DISCS.add(this);
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "discs");
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

    @Override
    public boolean canBeCrafted(Item item) {
        return item == Items.IRON_INGOT;
    }
}
