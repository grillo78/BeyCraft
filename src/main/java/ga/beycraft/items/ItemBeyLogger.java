package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.inventory.slots.BeyLoggerContainer;
import ga.beycraft.items.render.BeyLoggerItemStackRendererTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemBeyLogger extends Item {

	public ItemBeyLogger(String name) {
		super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1)
				.setISTER(() -> BeyLoggerItemStackRendererTileEntity::new));
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		BeyCraftRegistry.ITEMS.put(name, this);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
		ActionResult<ItemStack> result = super.use(world, player, handIn);
			if (!world.isClientSide) {
				NetworkHooks.openGui((ServerPlayerEntity) player,
						new SimpleNamedContainerProvider(
								(id, playerInventory, playerEntity) -> new BeyLoggerContainer(BeyCraftRegistry.BEYLOGGER_CONTAINER,
										id),
								new StringTextComponent(getDescriptionId())));
			}
		return result;
	}
}
