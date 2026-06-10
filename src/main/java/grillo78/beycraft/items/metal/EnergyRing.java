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
        boolean validFacebolt = stack.has(ModDataComponents.METAL_FACEBOLT) && stack.get(ModDataComponents.METAL_FACEBOLT).getStack().getItem() instanceof Facebolt;
        boolean validFusionWheel = stack.has(ModDataComponents.METAL_FUSION_WHEEL) && stack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack().getItem() instanceof FusionWheel;
        boolean validSpinTrack = stack.has(ModDataComponents.METAL_SPIN_TRACK) && stack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().getItem() instanceof SpinTrack;
        boolean validPerformanceTip = stack.has(ModDataComponents.METAL_PERFORMANCE_TIP) && stack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().getItem() instanceof PerformanceTip;
        return validFacebolt && validFusionWheel && validSpinTrack && validPerformanceTip;
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
    public boolean canBurst(ItemStack beybladeItem) {
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
        float spinTrackHeight = 0;
        float performanceTipHeight = 0;
        if(beybladeStack.has(ModDataComponents.METAL_SPIN_TRACK)){
            grillo78.beycraft.data.parts.metal.SpinTrack spinTrack = (grillo78.beycraft.data.parts.metal.SpinTrack) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART));
            if (spinTrack != null) {
                spinTrackHeight = spinTrack.getHeight();
            }
        }
        if(beybladeStack.has(ModDataComponents.METAL_PERFORMANCE_TIP)){
            grillo78.beycraft.data.parts.metal.PerformanceTip performanceTip = (grillo78.beycraft.data.parts.metal.PerformanceTip) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
            if (performanceTip != null) {
                performanceTipHeight = performanceTip.getHeight();
            }
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
    public float getRadiusReduction(ItemStack beybladeStack) {

        grillo78.beycraft.data.parts.metal.PerformanceTip performanceTip = (grillo78.beycraft.data.parts.metal.PerformanceTip) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
        float radiusReduction = 0;
        if (performanceTip != null) {
            radiusReduction = performanceTip.getRadiusReduction();
        }
        return radiusReduction;
    }

    @Override
    public float getSpeed(ItemStack beybladeStack) {

        grillo78.beycraft.data.parts.metal.PerformanceTip performanceTip = (grillo78.beycraft.data.parts.metal.PerformanceTip) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
        float speed = 0;
        if (performanceTip != null) {
            speed = performanceTip.getSpeed();
        }
        return speed;
    }

    @Override
    public float getWeight(ItemStack beybladeStack) {

        float performanceTipWeight = 0;
        float spinTrackWeight = 0;
        float fusionWheelWeight = 0;
        float energyRingWeight = 0;
        float faceboltWeight = 0;
        if(beybladeStack.has(ModDataComponents.METAL_PERFORMANCE_TIP)){
            Beypart performanceTip = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack().get(ModDataComponents.BEYPART));
            if (performanceTip != null) {
                performanceTipWeight = performanceTip.getWeight(beybladeStack.get(ModDataComponents.METAL_PERFORMANCE_TIP).getStack());
            }
        }
        if(beybladeStack.has(ModDataComponents.METAL_SPIN_TRACK)){
            Beypart spinTrack = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack().get(ModDataComponents.BEYPART));
            if (spinTrack != null) {
                spinTrackWeight = spinTrack.getWeight(beybladeStack.get(ModDataComponents.METAL_SPIN_TRACK).getStack());
            }
        }
        if(beybladeStack.has(ModDataComponents.METAL_FUSION_WHEEL)){
            Beypart fusionWheel = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack().get(ModDataComponents.BEYPART));
            if (fusionWheel != null) {
                fusionWheelWeight = fusionWheel.getWeight(beybladeStack.get(ModDataComponents.METAL_FUSION_WHEEL).getStack());
            }
        }
        if(beybladeStack.has(ModDataComponents.BEYPART)){
            Beypart energyRing = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
            if (energyRing != null) {
                energyRingWeight = energyRing.getWeight(beybladeStack);
            }
        }
        if (beybladeStack.has(ModDataComponents.METAL_FACEBOLT)){
            Beypart facebolt = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.METAL_FACEBOLT).getStack().get(ModDataComponents.BEYPART));
            if (facebolt != null) {
                faceboltWeight = facebolt.getWeight(beybladeStack.get(ModDataComponents.METAL_FACEBOLT).getStack());
            }
        }

        return faceboltWeight + energyRingWeight + fusionWheelWeight + spinTrackWeight + performanceTipWeight;
    }

    @Override
    public boolean canAbsorb(ItemStack beybladeItem) {
        return false;
    }

    @Override
    public float getAttack(ItemStack beybladeItem) {
        return 0;
    }

    @Override
    public float getDefense(ItemStack beybladeItem) {
        return 0;
    }

}
