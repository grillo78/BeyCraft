package grillo78.beycraft.items.burst;

import grillo78.beycraft.items.Beypart;
import grillo78.beycraft.items.components.ItemContent;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Layer extends Beypart {

    public Layer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        if (usedHand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                    if (stack.has(ModDataComponents.BURST_DRIVER)) {
                        player.drop(stack.get(ModDataComponents.BURST_DRIVER).getStack(), false);
                        stack.remove(ModDataComponents.BURST_DRIVER);
                    } else {
                        if (stack.has(ModDataComponents.BURST_DISC)) {
                            player.drop(stack.get(ModDataComponents.BURST_DISC).getStack(), false);
                            stack.remove(ModDataComponents.BURST_DISC);
                        }
                    }
                } else {
                    if (stack.has(ModDataComponents.BURST_DISC) && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof Driver) {
                        if (stack.has(ModDataComponents.BURST_DRIVER))
                            player.drop(stack.get(ModDataComponents.BURST_DRIVER).getStack(), false);
                        stack.set(ModDataComponents.BURST_DRIVER, new ItemContent(player.getItemInHand(InteractionHand.OFF_HAND).copy()));
                        player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
                    } else {
                        if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof Disc) {
                            if (stack.has(ModDataComponents.BURST_DISC))
                                player.drop(stack.get(ModDataComponents.BURST_DISC).getStack(), false);
                            stack.set(ModDataComponents.BURST_DISC, new ItemContent(player.getItemInHand(InteractionHand.OFF_HAND).copy()));
                            player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);
                        }
                    }
                }
            }

            result = InteractionResultHolder.success(stack);
        }

        return result;
    }
}
