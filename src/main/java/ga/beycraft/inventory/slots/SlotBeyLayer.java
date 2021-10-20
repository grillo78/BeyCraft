package ga.beycraft.inventory.slots;

import ga.beycraft.items.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class SlotBeyLayer extends SlotItemHandler {

    private ItemStack launcher;

    public SlotBeyLayer(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemStack launcher) {
        super(itemHandler, index, xPosition, yPosition);
        this.launcher = launcher;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean[] isValid = {false};
        if (!(launcher.getItem() instanceof ItemDualLauncher)) {
            if (stack.getItem() instanceof ItemBeyLayer && (((ItemBeyLayer) stack.getItem())
                    .getRotationDirection() == ((ItemLauncher) launcher.getItem()).getRotation(launcher)
                    || stack.getItem() instanceof ItemBeyLayerDual || stack.getItem() instanceof ItemBeyLayerGTDual)) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    if (stack.hasTag()) {
                        switch (h.getSlots()) {
                            case 2:
                                if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id")))
                                        instanceof ItemBeyDisc
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id")))
                                        .getItem() instanceof ItemBeyDriver) {
                                    isValid[0] = true;
                                }
                                break;
                            case 4:
                                if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id")))
                                        instanceof ItemBeyDisc
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id")))
                                        .getItem() instanceof ItemBeyDriver
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("chip")).getString("id")))
                                        .getItem() instanceof ItemBeyGTChip) {
                                    isValid[0] = true;
                                }
                                break;
                        }
                    }
                });
            }
        } else {
            if (stack.getItem() instanceof ItemBeyLayerDual || stack.getItem() instanceof ItemBeyLayerGTDual
                    || stack.getItem() instanceof ItemBeyLayerGTDualNoWeight) {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    if (stack.hasTag()) {
                        switch (h.getSlots()) {
                            case 2:
                                if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id"))) instanceof ItemBeyDisc
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id"))) instanceof ItemBeyDriver) {
                                    isValid[0] = true;
                                }
                                break;
                            case 3:
                            case 4:
                                if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("disc")).getString("id")))
                                        .getItem() instanceof ItemBeyDisc
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("driver")).getString("id")))
                                        .getItem() instanceof ItemBeyDriver
                                        && ForgeRegistries.ITEMS.getValue(new ResourceLocation(((CompoundNBT) stack.getTag().get("chip")).getString("id")))
                                        .getItem() instanceof ItemBeyGTChip) {
                                    isValid[0] = true;
                                }
                                break;
                        }
                    }
                });
            }
        }
        return isValid[0];
    }

}
