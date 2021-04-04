package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import com.grillo78.beycraft.inventory.BeyContainer;
import com.grillo78.beycraft.inventory.BeyGTContainer;
import com.grillo78.beycraft.inventory.slots.BeyLoggerContainer;
import com.grillo78.beycraft.items.render.BeyItemStackRendererTileEntity;
import com.grillo78.beycraft.items.render.BeyLoggerItemStackRendererTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemBeyLogger extends Item {

	public ItemBeyLogger(String name) {
		super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1)
				.setISTER(() -> BeyLoggerItemStackRendererTileEntity::new));
		setRegistryName(new ResourceLocation(Reference.MODID, name));
		BeyRegistry.ITEMS.put(name, this);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
		ActionResult<ItemStack> result = super.use(world, player, handIn);
			if (!world.isClientSide) {
				NetworkHooks.openGui((ServerPlayerEntity) player,
						new SimpleNamedContainerProvider(
								(id, playerInventory, playerEntity) -> new BeyLoggerContainer(BeyRegistry.BEYLOGGER_CONTAINER,
										id),
								new StringTextComponent(getDescriptionId())));
			}
		return result;
	}
}
