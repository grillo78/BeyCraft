package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.abilities.Ability;
import ga.beycraft.abilities.MultiMode;
import ga.beycraft.abilities.MultiType;
import ga.beycraft.inventory.*;
import ga.beycraft.items.render.BeyItemStackRendererTileEntity;
import ga.beycraft.util.BeyTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
    private final Color resonanceColor;
    private final Color secondResonanceColor;
    private final Color thirdResonanceColor;

    public ItemBeyLayer(String name, String displayName, float rotationDirection, float attack, float defense, float weight,
                        float burst, Ability primaryAbility, Ability secundaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, type, primaryAbility, secundaryAbility, BeyCraft.BEYCRAFTLAYERS, new Item.Properties().setISTER(() -> BeyItemStackRendererTileEntity::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = rotationDirection;
        this.resonanceColor = resonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
        BeyCraftRegistry.ITEMSLAYER.add(this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new ItemBeyProvider(stack);
    }

    public Color getResonanceColor(ItemStack stack) {
        return resonanceColor;
    }

    public Color getSecondResonanceColor(ItemStack stack) {
        return secondResonanceColor;
    }

    public Color getThirdResonanceColor(ItemStack stack) {
        return thirdResonanceColor;
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
                                            (id, playerInventory, playerEntity) -> new BeyGTContainer(BeyCraftRegistry.BEY_GT_CONTAINER, id,
                                                    stack, playerInventory, playerEntity, handIn),
                                            new StringTextComponent(getDescriptionId())));
                        } else {
                            if (stack.getItem() instanceof ItemBeyLayerGTNoWeight) {
                                NetworkHooks.openGui((ServerPlayerEntity) player,
                                        new SimpleNamedContainerProvider(
                                                (id, playerInventory, playerEntity) -> new BeyGTNoWeightContainer(BeyCraftRegistry.BEY_GT_CONTAINER_NO_WEIGHT, id,
                                                        stack, playerInventory),
                                                new StringTextComponent(getDescriptionId())));
                            } else {
                                if(stack.getItem() instanceof ItemBeyLayerGod){
                                    NetworkHooks.openGui((ServerPlayerEntity) player,
                                            new SimpleNamedContainerProvider(
                                                    (id, playerInventory, playerEntity) -> new BeyGodContainer(BeyCraftRegistry.BEY_GOD_CONTAINER, id,
                                                            stack, playerInventory),
                                                    new StringTextComponent(getDescriptionId())));
                                } else {
                                    NetworkHooks.openGui((ServerPlayerEntity) player,
                                            new SimpleNamedContainerProvider(
                                                    (id, playerInventory, playerEntity) -> new BeyContainer(BeyCraftRegistry.BEY_CONTAINER, id,
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

    public static class Color {
        private final float red;
        private final float green;
        private final float blue;

        public Color(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }
    }

}
