package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.HandleContainer;
import com.grillo78.beycraft.inventory.ItemHandleProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemLauncherHandle extends Item {

    public ItemLauncherHandle(String name) {
        super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        BeyRegistry.ITEMS.put(name,this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new ItemHandleProvider();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        if (!world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new HandleContainer(BeyRegistry.HANDLE_CONTAINER, id,
                                    player.getHeldItem(handIn), playerInventory, playerEntity, handIn),
                            new StringTextComponent(getRegistryName().getPath())));
        }
        return super.onItemRightClick(world, player, handIn);
    }
}
