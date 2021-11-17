package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.capabilities.BladerCapProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBeyCoin extends Item {
    public ItemBeyCoin(String name) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB));
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyCraftRegistry.ITEMS.put(name, this);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ActionResult<ItemStack> result = super.use(worldIn, playerIn, handIn);
        if (!worldIn.isClientSide) {
            ItemStack itemstack = playerIn.getItemInHand(handIn);
            int amount = playerIn.isShiftKeyDown() ? itemstack.getCount() : 1;
            itemstack.shrink(amount);

            playerIn.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
                h.increaseCurrency(amount);
            });

            result = ActionResult.success(itemstack);
        }
        return result;
    }
}
