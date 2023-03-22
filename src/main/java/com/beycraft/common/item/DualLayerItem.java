package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.ability.AbilityType;
import com.beycraft.utils.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DualLayerItem extends LayerItem{
    public DualLayerItem(String name, String displayName, float attack, float defense, float weight, float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, 1, attack, defense, weight, burst, primaryAbility, secondaryAbility, type, resonanceColor, secondResonanceColor, thirdResonanceColor);
    }

    @Override
    public Direction getRotationDirection(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("direction") ? Direction.getFromValue(stack.getTag().getInt("direction")):super.getRotationDirection(stack);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ActionResult<ItemStack> result = ActionResult.success(itemstack);
        if (player.isShiftKeyDown()){
            if(!itemstack.hasTag()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("direction", 1);
                itemstack.setTag(tag);
            }
            itemstack.getTag().putInt("direction", getRotationDirection(itemstack).inverted().getValue());
        } else
            result = super.use(level, player, hand);

        return result;
    }
}
