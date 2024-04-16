package grillo78.beycraft.common.item;

import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BeycoinItem extends Item {

    public BeycoinItem() {
        super(new Item.Properties().tab(BeycraftItemGroup.INSTANCE));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ActionResult<ItemStack> result = super.use(worldIn, playerIn, handIn);
        if (!worldIn.isClientSide) {
            ItemStack itemstack = playerIn.getItemInHand(handIn);
            int amount = playerIn.isShiftKeyDown() ? itemstack.getCount() : 1;
            itemstack.shrink(amount);

            playerIn.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
//                h.getWallet().increaseCurrency(amount);
            });

            result = ActionResult.success(itemstack);
        }
        return result;
    }
}
