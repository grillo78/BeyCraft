package com.beycraft.common.item;

import com.beycraft.client.item.BeyloggerRenderer;
import com.beycraft.common.capability.item.beylogger.BeyloggerCapabilityProvider;
import com.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class BeyloggerItem extends Item {

    public BeyloggerItem(Properties properties) {
        super(properties.tab(BeycraftItemGroup.INSTANCE).stacksTo(1).setISTER(()-> BeyloggerRenderer::new));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeyloggerCapabilityProvider();
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();

        CompoundNBT combinedTag = new CompoundNBT();

        if (baseTag != null) {
            combinedTag.put("base_tag", baseTag);
        }

        stack.getCapability(BeyloggerCapabilityProvider.BEYLOGGER).ifPresent(cap -> {
            INBT capabilityTag = BeyloggerCapabilityProvider.BEYLOGGER.writeNBT(cap, null);

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
        stack.getCapability(BeyloggerCapabilityProvider.BEYLOGGER).ifPresent(cap -> BeyloggerCapabilityProvider.BEYLOGGER.readNBT(cap, null, capabilityTag));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        return super.use(level, player, hand);
    }
}
