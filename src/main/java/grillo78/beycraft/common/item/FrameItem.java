package grillo78.beycraft.common.item;

import grillo78.beycraft.BeyTypes;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.item.GenericPartRenderer;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.util.ResourceLocation;

public class FrameItem extends BeyPartItem{

    private float attack;
    private float defense;

    public FrameItem(String name, String displayName, float attack, float defense, BeyTypes type) {
        super(name, displayName, type, null, null, BeycraftItemGroup.DISCS, new Properties().setISTER(()-> GenericPartRenderer::new));
        this.attack = attack;
        this.defense = defense;
        ModItems.ItemCreator.FRAMES.add(this);
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
