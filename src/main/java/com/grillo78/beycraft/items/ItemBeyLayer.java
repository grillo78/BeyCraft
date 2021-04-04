package com.grillo78.beycraft.items;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.abilities.Ability;
import com.grillo78.beycraft.abilities.MultiMode;
import com.grillo78.beycraft.abilities.MultiType;
import com.grillo78.beycraft.inventory.*;
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        ActionResult<ItemStack> result = super.use(world, player, handIn);
        if (result.getResult() == ActionResultType.FAIL) {
            if (!world.isClientSide) {
                ItemStack stack = player.getItemInHand(handIn);
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                        if (stack.getItem() instanceof ItemBeyLayerGT) {
                            NetworkHooks.openGui((ServerPlayerEntity) player,
                                    new SimpleNamedContainerProvider(
                                            (id, playerInventory, playerEntity) -> new BeyGTContainer(BeyRegistry.BEY_GT_CONTAINER, id,
                                                    stack, playerInventory, playerEntity, handIn),
                                            new StringTextComponent(getDescriptionId())));
                        } else {
                            if (stack.getItem() instanceof ItemBeyLayerGTNoWeight) {
                                NetworkHooks.openGui((ServerPlayerEntity) player,
                                        new SimpleNamedContainerProvider(
                                                (id, playerInventory, playerEntity) -> new BeyGTNoWeightContainer(BeyRegistry.BEY_GT_CONTAINER_NO_WEIGHT, id,
                                                        stack, playerInventory),
                                                new StringTextComponent(getDescriptionId())));
                            } else {
                                if(stack.getItem() instanceof ItemBeyLayerGod){
                                    NetworkHooks.openGui((ServerPlayerEntity) player,
                                            new SimpleNamedContainerProvider(
                                                    (id, playerInventory, playerEntity) -> new BeyGodContainer(BeyRegistry.BEY_GOD_CONTAINER, id,
                                                            stack, playerInventory),
                                                    new StringTextComponent(getDescriptionId())));
                                } else {
                                    NetworkHooks.openGui((ServerPlayerEntity) player,
                                            new SimpleNamedContainerProvider(
                                                    (id, playerInventory, playerEntity) -> new BeyContainer(BeyRegistry.BEY_CONTAINER, id,
                                                            stack, playerInventory),
                                                    new StringTextComponent(getDescriptionId())));
                                }
                            }
                        }
                });
            }
        }
        return result;
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        final String[] text = {super.getName(stack).getString()};
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            for (int i = 0; i < 2; i++) {
                if (h.getStackInSlot(i).getItem() != Items.AIR) {
                    text[0] = text[0] + " " + h.getStackInSlot(i).getHoverName().getString();
                }
            }
        });
        return new StringTextComponent(text[0]);
    }

    public float getAttack(ItemStack stack) {
        if (PRIMARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[0];
            }
            return ((MultiType) PRIMARYABILITY).getTypes().get(0).getValues()[0];
        }
        if (SECUNDARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[0];
            }
            return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getValues()[0];
        }
        if (PRIMARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) PRIMARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) PRIMARYABILITY).getModes().get(0).getValues()[0];
        }
        if (SECUNDARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) SECUNDARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) SECUNDARYABILITY).getModes().get(0).getValues()[0];
        }
        return attack;
    }

    public float getDefense(ItemStack stack) {
        if (PRIMARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[1];
            }
            return ((MultiType) PRIMARYABILITY).getTypes().get(0).getValues()[1];
        }
        if (SECUNDARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[1];
            }
            return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getValues()[1];
        }

        if (PRIMARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) PRIMARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) PRIMARYABILITY).getModes().get(0).getValues()[1];
        }
        if (SECUNDARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) SECUNDARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) SECUNDARYABILITY).getModes().get(0).getValues()[1];
        }
        return defense;
    }

    public float getWeight(ItemStack stack) {
        return weight;
    }

    public float getBurst(ItemStack stack) {
        if (PRIMARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) PRIMARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[2];
            }
            return ((MultiType) PRIMARYABILITY).getTypes().get(0).getValues()[2];
        }
        if (SECUNDARYABILITY instanceof MultiType) {
            if (stack.hasTag() && stack.getTag().contains("Type")) {
                return ((MultiType) SECUNDARYABILITY).getTypeHashMap().get(stack.getTag().getString("Type")).getValues()[2];
            }
            return ((MultiType) SECUNDARYABILITY).getTypes().get(0).getValues()[2];
        }
        if (PRIMARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) PRIMARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) PRIMARYABILITY).getModes().get(0).getValues()[2];
        }
        if (SECUNDARYABILITY instanceof MultiMode) {
            if (stack.hasTag() && stack.getTag().contains("Mode")) {
                return ((MultiMode) SECUNDARYABILITY).getModeHashMap().get(stack.getTag().getString("Mode"))
                        .getValues()[1];
            }
            return ((MultiMode) SECUNDARYABILITY).getModes().get(0).getValues()[2];
        }
        return burst;
    }

    public float getRotationDirection() {
        return rotationDirection;
    }

}
