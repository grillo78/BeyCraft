package grillo78.beycraft.items.burst;

import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.MainBeyPart;
import grillo78.beycraft.items.components.ItemContent;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

public class Layer extends MainBeyPart {

    public Layer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBeyAssembled(ItemStack stack) {
        boolean validDisc = stack.has(ModDataComponents.BURST_DISC) && stack.get(ModDataComponents.BURST_DISC).getStack().getItem() instanceof Disc;
        boolean validDriver = stack.has(ModDataComponents.BURST_DRIVER) && stack.get(ModDataComponents.BURST_DRIVER).getStack().getItem() instanceof Driver;
        return validDisc && validDriver;
    }

    @Override
    public void die(ItemStack beybladeItem, ServerLevel level, Vec3 position) {
        ItemStack layer = beybladeItem;
        ItemStack disc = beybladeItem.remove(ModDataComponents.BURST_DISC).getStack();
        ItemStack driver = beybladeItem.remove(ModDataComponents.BURST_DRIVER).getStack();

        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, driver));
        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, disc));
        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, layer));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        MutableComponent partComponent;
        ResourceLocation partID = stack.get(ModDataComponents.BEYPART);
        partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
        partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
        tooltipComponents.add(partComponent);
        if (stack.has(ModDataComponents.BURST_DISC)) {
            ItemStack discStack = stack.get(ModDataComponents.BURST_DISC).getStack();
            partID = discStack.get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
            if (discStack.getItem() instanceof CoreDisc && discStack.has(ModDataComponents.BURST_FRAME)) {
                partID = discStack.get(ModDataComponents.BURST_FRAME).getStack().get(ModDataComponents.BEYPART);
                partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
                partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
                tooltipComponents.add(partComponent);
            }
        }
        if (stack.has(ModDataComponents.BURST_DRIVER)) {
            partID = stack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
    }

    @Override
    public boolean canBurst() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        if (usedHand == InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
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
                    if (stack.has(ModDataComponents.BURST_DISC) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Driver) {
                        if (stack.has(ModDataComponents.BURST_DRIVER))
                            player.drop(stack.get(ModDataComponents.BURST_DRIVER).getStack(), false);
                        stack.set(ModDataComponents.BURST_DRIVER, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    } else {
                        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Disc) {
                            if (stack.has(ModDataComponents.BURST_DISC))
                                player.drop(stack.get(ModDataComponents.BURST_DISC).getStack(), false);
                            stack.set(ModDataComponents.BURST_DISC, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                            player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
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
        grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
        float driverHeight = 0;
        if (driver != null) {
            driverHeight = driver.getHeight();
        }
        return driverHeight;
    }

    @Override
    public float getFriction(ItemStack beybladeStack) {
        grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
        float driverHeight = 0;
        if (driver != null) {
            driverHeight = driver.getFriction();
        }
        return driverHeight;
    }

    @Override
    public float getWeight(ItemStack beybladeStack) {
        Beypart layer = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
        Beypart disc = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
        Beypart driver = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));

        float layerWeight = 0;
        if (layer != null) {
            layerWeight = layer.getWeight(beybladeStack);
        }
        float discWeight = 0;
        if (disc != null) {
            discWeight = disc.getWeight(beybladeStack.get(ModDataComponents.BURST_DISC).getStack());
        }
        float driverWeight = 0;
        if (driver != null) {
            driverWeight = driver.getWeight(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack());
        }

        return layerWeight + discWeight + driverWeight;
    }
}
