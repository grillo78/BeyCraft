package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.inventory.BeyContainer;
import com.grillo78.beycraft.inventory.ItemBeyProvider;
import com.grillo78.beycraft.items.render.BeyItemStackRendererTileEntity;
import com.grillo78.beycraft.util.BeyTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemBeyLayer extends ItemBeyPart {

    protected final float rotationDirection;
    private final float attack;
    private final float defense;
    private final float weight;
    private final float burst;

    public ItemBeyLayer(String name, float rotationDirection, float attack, float defense, float weight,
                        float burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type) {
        super(name, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTLAYERS, new Item.Properties().setISTER(() -> BeyItemStackRendererTileEntity::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = rotationDirection;
        BeyRegistry.ITEMSLAYER.add(this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new ItemBeyProvider(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        ActionResult<ItemStack> result = super.onItemRightClick(world, player, handIn);
        if(result.getType() == ActionResultType.FAIL){
            if (!world.isRemote) {
                NetworkHooks.openGui((ServerPlayerEntity) player,
                        new SimpleNamedContainerProvider(
                                (id, playerInventory, playerEntity) -> new BeyContainer(BeyRegistry.BEY_CONTAINER, id,
                                        player.getHeldItem(handIn), playerInventory, playerEntity, handIn),
                                new StringTextComponent(getRegistryName().getPath())));
            }
        }
        return result;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ITextComponent textComponent = super.getDisplayName(stack);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            for (int i = 0; i < 2; i++) {
                if (h.getStackInSlot(i).getItem() != Items.AIR) {
                    textComponent.appendText(" " + h.getStackInSlot(i).getDisplayName().getString());
                }
            }
        });
        return textComponent;
    }

    public float getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }

    public float getWeight() {
        return weight;
    }

    public float getBurst() {
        return burst;
    }

    public float getRotationDirection() {
        return rotationDirection;
    }

}
