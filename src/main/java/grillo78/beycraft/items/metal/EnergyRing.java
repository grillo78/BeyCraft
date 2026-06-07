package grillo78.beycraft.items.metal;

import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.MainBeyPart;
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

public class EnergyRing extends MainBeyPart {

    public EnergyRing(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBeyAssembled(ItemStack stack) {
        boolean validDisc = stack.has(ModDataComponents.BURST_DISC) && stack.get(ModDataComponents.BURST_DISC).getStack().getItem() instanceof FusionWheel;
        boolean validDriver = stack.has(ModDataComponents.BURST_DRIVER) && stack.get(ModDataComponents.BURST_DRIVER).getStack().getItem() instanceof SpinTrack;
        return validDisc && validDriver;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        MutableComponent partComponent;
        if(stack.has(ModDataComponents.METAL_FACEBOLT)){
            ResourceLocation partID = stack.get(ModDataComponents.METAL_FACEBOLT).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
        ResourceLocation partID = stack.get(ModDataComponents.BEYPART);
        partComponent = Component.translatable(partID.getNamespace() +".tooltip." + partID.getPath());
        partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
        tooltipComponents.add(partComponent);
        if(stack.has(ModDataComponents.METAL_FUSION_WHEEL)){
            partID = stack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
        if(stack.has(ModDataComponents.METAL_SPIN_TRACK)){
            partID = stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
        if(stack.has(ModDataComponents.METAL_PERFORMANCE_TIP)){
            partID = stack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
    }

    @Override
    public boolean canBurst() {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        if (usedHand == InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    if (stack.has(ModDataComponents.METAL_PERFORMANCE_TIP)) {
                        player.drop(stack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack(), false);
                        stack.remove(ModDataComponents.METAL_PERFORMANCE_TIP);
                    } else {
                        if (stack.has(ModDataComponents.METAL_SPIN_TRACK)) {
                            player.drop(stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack(), false);
                            stack.remove(ModDataComponents.METAL_SPIN_TRACK);
                        } else {
                            if (stack.has(ModDataComponents.METAL_FUSION_WHEEL)) {
                                player.drop(stack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack(), false);
                                stack.remove(ModDataComponents.METAL_FUSION_WHEEL);
                            } else {
                                if (stack.has(ModDataComponents.METAL_FACEBOLT)) {
                                    player.drop(stack.get(ModDataComponents.METAL_FACEBOLT).getStack(), false);
                                    stack.remove(ModDataComponents.METAL_FACEBOLT);
                                }
                            }
                        }
                    }
                } else {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Facebolt) {
                        if (stack.has(ModDataComponents.METAL_FACEBOLT))
                            player.drop(stack.get(ModDataComponents.METAL_FACEBOLT).getStack(), false);
                        stack.set(ModDataComponents.METAL_FACEBOLT, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    }
                    if (stack.has(ModDataComponents.METAL_FUSION_WHEEL) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpinTrack) {
                        if (stack.has(ModDataComponents.METAL_SPIN_TRACK))
                            player.drop(stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack(), false);
                        stack.set(ModDataComponents.METAL_SPIN_TRACK, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    } else {
                        if (stack.has(ModDataComponents.METAL_SPIN_TRACK) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PerformanceTip) {
                            if (stack.has(ModDataComponents.METAL_PERFORMANCE_TIP))
                                player.drop(stack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack(), false);
                            stack.set(ModDataComponents.METAL_PERFORMANCE_TIP, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                            player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        } else {
                            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof FusionWheel) {
                                if (stack.has(ModDataComponents.METAL_FUSION_WHEEL))
                                    player.drop(stack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack(), false);
                                stack.set(ModDataComponents.METAL_FUSION_WHEEL, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                            }
                        }
                    }
                }
            }

            result = InteractionResultHolder.success(stack);
        }

        return result;
    }

    @Override
    public double getTotalHeight(ItemStack beybladeStack) {
        grillo78.beycraft.data.parts.metal.SpinTrack spinTrack = (grillo78.beycraft.data.parts.metal.SpinTrack) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART));
        grillo78.beycraft.data.parts.metal.PerformanceTip performanceTip = (grillo78.beycraft.data.parts.metal.PerformanceTip) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
        float spinTrackHeight = 0;
        float performanceTipHeight = 0;
        if(spinTrack != null) {
            spinTrackHeight = spinTrack.getHeight();
        }
        if(spinTrack != null) {
            performanceTipHeight = performanceTip.getHeight();
        }
        return spinTrackHeight + performanceTipHeight;
    }

    @Override
    public float getFriction(ItemStack beybladeStack) {

        grillo78.beycraft.data.parts.metal.PerformanceTip performanceTip = (grillo78.beycraft.data.parts.metal.PerformanceTip) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
        float driverHeight = 0;
        if (performanceTip != null) {
            driverHeight = performanceTip.getFriction();
        }
        return driverHeight;
    }

    @Override
    public float getWeight(ItemStack beybladeStack) {

        Beypart performanceTip = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
        Beypart spinTrack = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART));
        Beypart fusionWheel = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack().get(ModDataComponents.BEYPART));
        Beypart energyRing = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
        Beypart facebolt = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_FACEBOLT).getStack().get(ModDataComponents.BEYPART));
        float performanceTipWeight = 0;
        if (performanceTip != null) {
            performanceTipWeight = performanceTip.getWeight(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack());
        }
        float spinTrackWeight = 0;
        if (spinTrack != null) {
            spinTrackWeight = spinTrack.getWeight(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack());
        }
        float fusionWheelWeight = 0;
        if (spinTrack != null) {
            fusionWheelWeight = fusionWheel.getWeight(beybladeStack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack());
        }
        float energyRingWeight = 0;
        if (spinTrack != null) {
            energyRingWeight = energyRing.getWeight(beybladeStack);
        }
        float faceboltWeight = 0;
        if (spinTrack != null) {
            faceboltWeight = facebolt.getWeight(beybladeStack.get(ModDataComponents.METAL_FACEBOLT).getStack());
        }
        return faceboltWeight + energyRingWeight + fusionWheelWeight + spinTrackWeight + performanceTipWeight;
    }
}
