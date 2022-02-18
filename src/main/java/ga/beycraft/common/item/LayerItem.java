package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.abilities.AbilityType;
import ga.beycraft.common.tab.BeycraftCreativeModeTab;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LayerItem extends BeyPartItem {
    protected final int rotationDirection;
    private final float attack;
    private final float defense;
    private final float weight;
    private final float burst;
    private final Color resonanceColor;
    private final Color secondResonanceColor;
    private final Color thirdResonanceColor;

    public LayerItem(String name, String displayName, int rotationDirection, float attack, float defense, float weight,
                     float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftCreativeModeTab.LAYERS, new Properties());
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = rotationDirection;
        this.resonanceColor = resonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
    }

    public int getRotationDirection(ItemStack stack) {
        return rotationDirection;
    }

    public boolean isBeyAssembled(ItemStack stack) {
        return true;
    }

    public static class Color {
        private final float red;
        private final float green;
        private final float blue;

        public Color(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }
    }
}
