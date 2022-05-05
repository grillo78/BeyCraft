package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.Beycraft;
import ga.beycraft.client.item.GenericPartRenderer;
import ga.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.util.ResourceLocation;

public class FrameItem extends BeyPartItem{

    private float attack;
    private float defense;

    public FrameItem(String name, String displayName, float attack, float defense, BeyTypes type) {
        super(name, displayName, type, null, null, BeycraftItemGroup.DISCS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.attack = attack;
        this.defense = defense;
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "discs");
    }

    public double getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }
}
