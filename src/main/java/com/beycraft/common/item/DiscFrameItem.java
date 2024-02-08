package com.beycraft.common.item;

import com.beycraft.BeyTypes;
import com.beycraft.common.ability.AbilityType;
import com.beycraft.common.capability.item.parts.DiscFrameCapabilityProvider;
import com.beycraft.common.container.DiscFrameContainer;
import com.beycraft.common.container.ModContainers;
import com.google.common.util.concurrent.AtomicDouble;
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

public class DiscFrameItem extends DiscItem {

    private float frameRotation;
    private float attack;
    private float defense;

    public DiscFrameItem(String name, String displayName, float attack, float defense, float weight, float frameRotation, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type) {
        super(name, displayName, attack, defense, weight, primaryAbility, secondaryAbility, type);
        this.frameRotation = frameRotation;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public StringTextComponent getName(ItemStack stack) {
        StringTextComponent name = super.getName(stack);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            if (!itemHandler.getStackInSlot(0).isEmpty()) name.append(" ").append(itemHandler.getStackInSlot(0).getHoverName());
        });
        return name;
    }

    @Override
    public float getAttack(ItemStack disc) {
        AtomicDouble attack = new AtomicDouble(this.attack);
        disc.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(0).getItem() instanceof FrameItem)
                attack.set(((FrameItem)cap.getStackInSlot(0).getItem()).getAttack());
        });
        return (float) attack.get();
    }

    @Override
    public float getDefense(ItemStack disc) {
        AtomicDouble defense = new AtomicDouble(this.defense);
        disc.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(0).getItem() instanceof FrameItem)
                defense.set(((FrameItem)cap.getStackInSlot(0).getItem()).getDefense());
        });
        return (float) defense.get();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new DiscFrameCapabilityProvider(new ItemStackHandler(1));
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
        CompoundNBT baseTag = nbt.getCompound("base_tag"); // empty if not found
        INBT capabilityTag = nbt.get("capability_tag");    // empty if not found
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = super.use(level, player, hand);
        if (result.getResult() != ActionResultType.SUCCESS && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new DiscFrameContainer(ModContainers.DISC_FRAME, id, stack, playerInventory),
                            new StringTextComponent(getDescriptionId())));
            result = ActionResult.success(stack);
        }
        return result;
    }

    public float getFrameRotation() {
        return frameRotation;
    }
}
