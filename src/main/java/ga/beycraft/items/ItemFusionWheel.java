package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.items.render.GenericPartItemStackRendererTileEntity;
import ga.beycraft.util.BeyTypes;

public class ItemFusionWheel extends ItemBeyPart{

    float attack;
    float defense;
    float weight;
    public ItemFusionWheel(String name, String displayName, BeyTypes type,float attack, float defense, float weight, Ability primaryAbility, Ability secondaryAbility) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeyCraft.BEYCRAFTLAYERS, new Properties().setISTER(()-> GenericPartItemStackRendererTileEntity::new));
        BeyCraftRegistry.ITEMSFUSIONWHEEL.add(this);
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
    }


    public float getWeight() {
        return weight;
    }

    public float getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }
}
