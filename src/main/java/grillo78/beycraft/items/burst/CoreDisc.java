package grillo78.beycraft.items.burst;

import grillo78.beycraft.items.Beypart;
import grillo78.beycraft.items.components.ItemContent;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.List;

public class CoreDisc extends Disc {

    public CoreDisc(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        MutableComponent partComponent;
        ResourceLocation partID = stack.get(ModDataComponents.BEYPART);
        partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
        partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
        tooltipComponents.add(partComponent);
        if (stack.has(ModDataComponents.BURST_FRAME)) {
            partID = stack.get(ModDataComponents.BURST_FRAME).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        if (usedHand == InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    if (stack.has(ModDataComponents.BURST_FRAME)) {
                        player.drop(stack.get(ModDataComponents.BURST_FRAME).getStack(), false);
                        stack.remove(ModDataComponents.BURST_FRAME);
                    }
                } else {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Frame) {
                        if (stack.has(ModDataComponents.BURST_FRAME))
                            player.drop(stack.get(ModDataComponents.BURST_FRAME).getStack(), false);
                        stack.set(ModDataComponents.BURST_FRAME, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    }
                }
            }

            result = InteractionResultHolder.success(stack);
        }

        return result;
    }
}
