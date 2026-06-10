package grillo78.beycraft.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class MainBeyPart extends Beypart {

    public MainBeyPart(Properties properties) {
        super(properties);
    }

    public abstract boolean isBeyAssembled(ItemStack stack);

    public abstract boolean canAbsorb(ItemStack beybladeItem);
    public abstract boolean canBurst(ItemStack beybladeItem);

    public abstract double getTotalHeight(ItemStack beybladeStack);
    public abstract float getFriction(ItemStack beybladeItem);
    public abstract float getWeight(ItemStack beybladeItem);
    public abstract float getRadiusReduction(ItemStack beybladeItem);
    public abstract float getSpeed(ItemStack beybladeItem);
    public abstract float getAttack(ItemStack beybladeItem);
    public abstract float getDefense(ItemStack beybladeItem);
    public void die(ItemStack beybladeItem, ServerLevel level, Vec3 position){}

    public float getBurstResistance(ItemStack beybladeItem) {
        return 0;
    }
}