package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import ga.beycraft.client.item.LayerRenderer;
import ga.beycraft.common.capability.BeyCapabilityProvider;
import ga.beycraft.common.container.LayerContainer;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayerItem extends BeyPartItem {
    protected final int rotationDirection;
    private final float attack;
    private final float defense;
    private final float weight;
    private final float burst;
    private final Color resonanceColor;
    private final Color secondResonanceColor;
    private final Color thirdResonanceColor;

    public LayerItem(String name, String displayName, int rotationDirection, float attack, float defense, float weight,
                     float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.LAYERS, new Properties().setISTER(()-> LayerRenderer::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = rotationDirection;
        this.resonanceColor = resonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
    }

    public int getSlotsAmount() {
        return 2;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeyCapabilityProvider(new ItemStackHandler(getSlotsAmount()));
    }

    public int getRotationDirection(ItemStack stack) {
        return rotationDirection;
    }

    public boolean isBeyAssembled(ItemStack stack) {
        AtomicBoolean assembled = new AtomicBoolean(true);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap->{

        });
        return assembled.get();
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        ActionResult<ItemStack> result = super.use(world, player, handIn);
        if (result.getResult() != ActionResultType.SUCCESS && !world.isClientSide) {
            ItemStack stack = player.getItemInHand(handIn);
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new LayerContainer(ModContainers.LAYER, id,
                                    stack, playerInventory),
                            new StringTextComponent(getDescriptionId())));
            result = ActionResult.success(stack);
        }
        return result;
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();

        CompoundNBT combinedTag = new CompoundNBT();

        if (baseTag != null) {
            combinedTag.put("base_tag", baseTag);
        }

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            INBT capabilityTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(cap, null);

            if (capabilityTag != null) {
                combinedTag.put("capability_tag", capabilityTag);
            }
        });

        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound("base_tag");              // empty if not found
        CompoundNBT capabilityTag = nbt.getCompound("capability_tag"); // empty if not found
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
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
