package grillo78.beycraft.items;

import grillo78.beycraft.entities.Beyblade;
import grillo78.beycraft.entities.ModEntities;
import grillo78.beycraft.items.components.ItemContent;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Launcher extends Item {
    public Launcher(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(stack.has(ModDataComponents.LAUNCHER_BEY)){
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
                    if (validBey(player.getItemInHand(InteractionHand.MAIN_HAND).getItem())) {
                        if (stack.has(ModDataComponents.LAUNCHER_BEY))
                            player.drop(stack.get(ModDataComponents.LAUNCHER_BEY).getStack(), false);
                        stack.set(ModDataComponents.LAUNCHER_BEY, new ItemContent(player.getItemInHand(InteractionHand.MAIN_HAND).copy()));
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    }
                }
            }
            result = InteractionResultHolder.success(stack);
        } else {
            if(!level.isClientSide){
                if (stack.has(ModDataComponents.LAUNCHER_BEY)){
                    Beyblade beyblade = new Beyblade(ModEntities.BEYBLADE.get(), level);
                    beyblade.setPos(player.position());
                    beyblade.setBeybladeItem(stack.get(ModDataComponents.LAUNCHER_BEY).getStack());
                    level.addFreshEntity(beyblade);
                    stack.remove(ModDataComponents.LAUNCHER_BEY);
                }
            }
            result = InteractionResultHolder.success(stack);
        }

        return result;
    }

    private boolean validBey(Item item) {
        return item instanceof MainBeyPart;
    }
}
