package grillo78.beycraft.common.item;

import grillo78.beycraft.client.entity.BeltModel;
import grillo78.beycraft.common.capability.item.BeltCapabilityProvider;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import com.mojang.blaze3d.matrix.MatrixStack;
import grillo78.clothes_mod.common.items.ClothItem;
import grillo78.clothes_mod.common.items.ClothesSlot;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class BladerBelt extends ClothItem {

    public BladerBelt() {
        super(new Properties().tab(BeycraftItemGroup.INSTANCE), ClothesSlot.BELT);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeltCapabilityProvider();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderCloth(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, Entity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, PlayerModel bipedModel) {
        BeltModel model = new BeltModel();
        model.belt.copyFrom(bipedModel.body);
        model.renderToBuffer(pMatrixStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTexture())), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
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
        if (nbt != null) {
            if(nbt.contains("base_tag")) {
                CompoundNBT baseTag = nbt.getCompound("base_tag");
                stack.setTag(baseTag);
            }
            if(nbt.contains("capability_tag")){
                INBT capabilityTag = nbt.get("capability_tag");
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
            }
        }
    }
}
