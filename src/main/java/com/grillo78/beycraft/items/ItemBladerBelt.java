package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeltContainer;
import com.grillo78.beycraft.inventory.ItemBeltProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ItemBladerBelt extends Item {


    public ItemBladerBelt(String name) {
        super(new Item.Properties().group(BeyCraft.BEYCRAFTTAB).maxStackSize(1));
        this.setRegistryName(new ResourceLocation(Reference.MODID, name));
        BeyRegistry.ITEMS.put(name,this);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemBeltProvider();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        if (!world.isRemote) {
            player.playSound(BeyRegistry.OPEN_CLOSE_BELT, SoundCategory.PLAYERS, 1, 1);
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new BeltContainer(BeyRegistry.BELT_CONTAINER, id,
                                    player.getHeldItem(handIn), playerInventory, true),
                            new StringTextComponent(getRegistryName().getPath())));
        }
        return ActionResult.resultSuccess(player.getHeldItem(handIn));
    }
}
