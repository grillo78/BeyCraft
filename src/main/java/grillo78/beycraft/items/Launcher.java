package grillo78.beycraft.items;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.data.parts.LaunchersReloadListener;
import grillo78.beycraft.entities.Beyblade;
import grillo78.beycraft.entities.ModEntities;
import grillo78.beycraft.items.burst.Layer;
import grillo78.beycraft.items.components.ItemContent;
import grillo78.beycraft.items.components.ModDataComponents;
import grillo78.beycraft.items.metal.EnergyRing;
import grillo78.beycraft.items.x.Blade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.List;

public class Launcher extends Item {


    public Launcher(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ModDataComponents.LAUNCHER)) {
            ResourceLocation launcherID = stack.get(ModDataComponents.LAUNCHER);
            grillo78.beycraft.data.parts.Launcher launcher = LaunchersReloadListener.LAUNCHERS.get(launcherID);
            MutableComponent partComponent = Component.translatable(launcherID.getNamespace() + ".tooltip.generation." + launcher.getGeneration().toString().toLowerCase());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
        if (stack.has(ModDataComponents.LAUNCHER_BEY)) {
            ItemStack beyblade = stack.get(ModDataComponents.LAUNCHER_BEY).getStack();
            beyblade.getItem().appendHoverText(beyblade, context, tooltipComponents, tooltipFlag);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        ItemStack stack = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.OFF_HAND) {
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    if (stack.has(ModDataComponents.LAUNCHER_BEY)) {
                        player.drop(stack.get(ModDataComponents.LAUNCHER_BEY).getStack(), false);
                        stack.remove(ModDataComponents.LAUNCHER_BEY);
                    }
                } else {
                    if (validBey(player.getItemInHand(InteractionHand.MAIN_HAND), stack)) {
                        if (stack.has(ModDataComponents.LAUNCHER_BEY))
                            player.drop(stack.get(ModDataComponents.LAUNCHER_BEY).getStack(), false);
                        stack.set(ModDataComponents.LAUNCHER_BEY, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    }
                }
            }
            result = InteractionResultHolder.success(stack);
        } else {
            if (!level.isClientSide) {
                Beycraft.LOGGER.debug("Player X Rot: {}", player.getXRot());
                if (stack.has(ModDataComponents.LAUNCHER_BEY)) {
                    Beyblade beyblade = new Beyblade(ModEntities.BEYBLADE.get(), level);
                    beyblade.setPos(player.getEyePosition().add(player.getViewVector(0)));
                    beyblade.setBeybladeItem(stack.get(ModDataComponents.LAUNCHER_BEY).getStack());
                    beyblade.setFlowerPattern(player.getXRot()>15);
                    level.addFreshEntity(beyblade);
                    stack.remove(ModDataComponents.LAUNCHER_BEY);
                }
            }
            result = InteractionResultHolder.success(stack);
        }

        return result;
    }

    private boolean validBey(ItemStack itemStack, ItemStack launcherStack) {
        boolean valid = false;
        if (launcherStack.has(ModDataComponents.LAUNCHER) && LaunchersReloadListener.LAUNCHERS.containsKey(launcherStack.get(ModDataComponents.LAUNCHER)))
        {
            Generations generation = LaunchersReloadListener.LAUNCHERS.get(launcherStack.get(ModDataComponents.LAUNCHER)).getGeneration();
            switch (generation) {
                case PLASTIC:
                    break;
                case METAL:
                    valid = itemStack.getItem() instanceof EnergyRing;
                    break;
                case BURST:
                    valid = itemStack.getItem() instanceof Layer;
                    break;
                case X:
                    valid = itemStack.getItem() instanceof Blade;
                    break;
            }
        }
        return valid && ((MainBeyPart) itemStack.getItem()).isBeyAssembled(itemStack);
    }

    public enum Generations implements StringRepresentable {
        PLASTIC, METAL, BURST, X;


        @Override
        public String getSerializedName() {
            return toString();
        }
    }
}
