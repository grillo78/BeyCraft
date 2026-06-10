package grillo78.beycraft.items.x;

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

public class Blade extends MainBeyPart {

    public Blade(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBeyAssembled(ItemStack stack) {
        boolean validDisc = stack.has(ModDataComponents.X_RATCHET) && stack.get(ModDataComponents.X_RATCHET).getStack().getItem() instanceof Ratchet;
        boolean validDriver = stack.has(ModDataComponents.X_BIT) && stack.get(ModDataComponents.X_BIT).getStack().getItem() instanceof Bit;
        return validDisc && validDriver;
    }

    @Override
    public void die(ItemStack beybladeStack, ServerLevel level, Vec3 position) {
        ItemStack layer = beybladeStack;
        level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, layer));
        if (beybladeStack.has(ModDataComponents.X_RATCHET)){
            ItemStack disc = beybladeStack.remove(ModDataComponents.X_RATCHET).getStack();
            level.addFreshEntity(new ItemEntity(level, position.x, position.y, position.z, disc));
        }
        if (beybladeStack.has(ModDataComponents.X_BIT)){
            ItemStack driver = beybladeStack.remove(ModDataComponents.X_BIT).getStack();
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
        if (stack.has(ModDataComponents.X_RATCHET)) {
            ItemStack discStack = stack.get(ModDataComponents.X_RATCHET).getStack();
            partID = discStack.get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
        if (stack.has(ModDataComponents.X_BIT)) {
            partID = stack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART);
            partComponent = Component.translatable(partID.getNamespace() + ".tooltip." + partID.getPath());
            partComponent.setStyle(partComponent.getStyle().withColor(Color.ORANGE.hashCode()));
            tooltipComponents.add(partComponent);
        }
    }

    @Override
    public boolean canBurst(ItemStack beybladeItem) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, usedHand);
        if (usedHand == InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (!level.isClientSide) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    if (stack.has(ModDataComponents.X_BIT)) {
                        player.drop(stack.get(ModDataComponents.X_BIT).getStack(), false);
                        stack.remove(ModDataComponents.X_BIT);
                    } else {
                        if (stack.has(ModDataComponents.X_RATCHET)) {
                            player.drop(stack.get(ModDataComponents.X_RATCHET).getStack(), false);
                            stack.remove(ModDataComponents.X_RATCHET);
                        }
                    }
                } else {
                    if (stack.has(ModDataComponents.X_RATCHET) && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Bit) {
                        if (stack.has(ModDataComponents.X_BIT))
                            player.drop(stack.get(ModDataComponents.X_BIT).getStack(), false);
                        stack.set(ModDataComponents.X_BIT, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    } else {
                        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof Ratchet) {
                            if (stack.has(ModDataComponents.X_RATCHET))
                                player.drop(stack.get(ModDataComponents.X_RATCHET).getStack(), false);
                            stack.set(ModDataComponents.X_RATCHET, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
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
        if(beybladeStack.has(ModDataComponents.X_BIT)){
            grillo78.beycraft.data.parts.x.Bit driver = (grillo78.beycraft.data.parts.x.Bit) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverHeight = driver.getHeight();
            }
        }
        return driverHeight;
    }

    @Override
    public float getFriction(ItemStack beybladeStack) {
        float driverHeight = 0;
        if(beybladeStack.has(ModDataComponents.X_BIT)){
            grillo78.beycraft.data.parts.x.Bit driver = (grillo78.beycraft.data.parts.x.Bit) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverHeight = driver.getFriction();
            }
        }
        return driverHeight;
    }

    @Override
    public float getRadiusReduction(ItemStack beybladeStack) {
        float radiusReduction = 0;
        if(beybladeStack.has(ModDataComponents.X_BIT)){
            grillo78.beycraft.data.parts.x.Bit driver = (grillo78.beycraft.data.parts.x.Bit) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                radiusReduction = driver.getRadiusReduction();
            }
        }
        return radiusReduction;
    }

    @Override
    public float getSpeed(ItemStack beybladeStack) {
        float speed = 0;
        if(beybladeStack.has(ModDataComponents.X_BIT)){
            grillo78.beycraft.data.parts.x.Bit driver = (grillo78.beycraft.data.parts.x.Bit) BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART));
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
        if (beybladeStack.has(ModDataComponents.X_RATCHET)){
            Beypart disc = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_RATCHET).getStack().get(ModDataComponents.BEYPART));
            if (disc != null) {
                discWeight = disc.getWeight(beybladeStack.get(ModDataComponents.X_RATCHET).getStack());
            }
        }
        if(beybladeStack.has(ModDataComponents.X_BIT)){
            Beypart driver = BeypartsReloadListener.BEYPARTS.get(beybladeStack.get(ModDataComponents.X_BIT).getStack().get(ModDataComponents.BEYPART));
            if (driver != null) {
                driverWeight = driver.getWeight(beybladeStack.get(ModDataComponents.X_BIT).getStack());
            }
        }
        return layerWeight + discWeight + driverWeight;
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
