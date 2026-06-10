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
    public void die(ItemStack beybladeStack, ServerLevel level, Vec3 position) {
        ItemStack layer = beybladeStack;
        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, layer));
        if (beybladeStack.has(ModDataComponents.BURST_DISC)){
            ItemStack disc = beybladeStack.remove(ModDataComponents.BURST_DISC).getStack();
            level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, disc));
        }
        if (beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            ItemStack driver = beybladeStack.remove(ModDataComponents.BURST_DRIVER).getStack();
            level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, driver));
        }
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
    public boolean canBurst(ItemStack beybladeStack) {
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
        float driverHeight = 0;
        if(beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverHeight = driver.getHeight();
            }
        }
        return driverHeight;
    }

    @Override
    public float getFriction(ItemStack beybladeStack) {
        float driverHeight = 0;
        if(beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverHeight = driver.getFriction();
            }
        }
        return driverHeight;
    }

    @Override
    public float getRadiusReduction(ItemStack beybladeStack) {
        float radiusReduction = 0;
        if(beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                radiusReduction = driver.getRadiusReduction();
            }
        }
        return radiusReduction;
    }

    @Override
    public float getSpeed(ItemStack beybladeStack) {
        float speed = 0;
        if(beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            grillo78.beycraft.data.parts.burst.Driver driver = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                speed = driver.getSpeed();
            }
        }
        return speed;
    }

    @Override
    public float getWeight(ItemStack beybladeStack) {
        float layerWeight = 0;
        float discWeight = 0;
        float driverWeight = 0;
        if(beybladeStack.has(ModDataComponents.BEYPART)){
            Beypart layer = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
            if (layer != null) {
                layerWeight = layer.getWeight(beybladeStack);
            }
        }
        if (beybladeStack.has(ModDataComponents.BURST_DISC)){
            Beypart disc = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DISC).getStack().get(ModDataComponents.BEYPART));
            if (disc != null) {
                discWeight = disc.getWeight(beybladeStack.get(ModDataComponents.BURST_DISC).getStack());
            }
        }
        if(beybladeStack.has(ModDataComponents.BURST_DRIVER)){
            Beypart driver = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverWeight = driver.getWeight(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack());
            }
        }
        return layerWeight + discWeight + driverWeight;
    }

    @Override
    public boolean canAbsorb(ItemStack beybladeStack) {
        return false;
    }

    @Override
    public float getAttack(ItemStack beybladeStack) {
        float layerAttack = 0;
        float discAttack = 0;
        if(beybladeStack.has(ModDataComponents.BEYPART)) {
            grillo78.beycraft.data.parts.burst.Layer layer = (grillo78.beycraft.data.parts.burst.Layer) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
            layerAttack = layer.getAttack();
        }
        if (beybladeStack.has(ModDataComponents.BURST_DISC)) {
            grillo78.beycraft.data.parts.burst.Disc disc = (grillo78.beycraft.data.parts.burst.Disc) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DISC).getStack().get(ModDataComponents.BEYPART));
            discAttack = disc.getAttack();
        }
        return layerAttack + discAttack;
    }

    @Override
    public float getBurstResistance(ItemStack beybladeStack) {
        float layerBurstResistance = 0;
        float driverBurstResistance = 0;
        if(beybladeStack.has(ModDataComponents.BEYPART)) {
            grillo78.beycraft.data.parts.burst.Layer layer = (grillo78.beycraft.data.parts.burst.Layer) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
            layerBurstResistance = layer.getAttack();
        }
        if (beybladeStack.has(ModDataComponents.BURST_DISC)) {
            grillo78.beycraft.data.parts.burst.Driver disc = (grillo78.beycraft.data.parts.burst.Driver) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DRIVER).getStack().get(ModDataComponents.BEYPART));
            driverBurstResistance = disc.getBurstResistance();
        }
        return layerBurstResistance + driverBurstResistance;
    }

    @Override
    public float getDefense(ItemStack beybladeStack) {
        float layerDefense = 0;
        float discDefense = 0;
        if(beybladeStack.has(ModDataComponents.BEYPART)) {
            grillo78.beycraft.data.parts.burst.Layer layer = (grillo78.beycraft.data.parts.burst.Layer) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BEYPART));
            layerDefense = layer.getDefense();
        }
        if (beybladeStack.has(ModDataComponents.BURST_DISC)) {
            grillo78.beycraft.data.parts.burst.Disc disc = (grillo78.beycraft.data.parts.burst.Disc) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.BURST_DISC).getStack().get(ModDataComponents.BEYPART));
            discDefense = disc.getDefense();
        }
        return layerDefense + discDefense;
    }

}
