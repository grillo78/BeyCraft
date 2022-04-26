package ga.beycraft.common.item;

import ga.beycraft.Beycraft;
import ga.beycraft.common.tab.BeycraftItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;
import xyz.heroesunited.heroesunited.common.objects.items.IAccessory;
import xyz.heroesunited.heroesunited.util.HUPlayerUtil;

public class AccessoryItem extends Item implements IAccessory {

    private ResourceLocation texture;
    private ResourceLocation textureSlim;
    private EquipmentAccessoriesSlot slot;

    public AccessoryItem(EquipmentAccessoriesSlot slot) {
        super(new Item.Properties().tab(BeycraftItemGroup.INSTANCE).stacksTo(1));
        this.slot = slot;
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, PlayerEntity entity, EquipmentAccessoriesSlot slot) {
        if (texture == null) {
            texture = new ResourceLocation(this.getRegistryName().getNamespace(), "textures/accessories/" + this.getRegistryName().getPath() + ".png");
            if(slot == EquipmentAccessoriesSlot.TSHIRT || slot == EquipmentAccessoriesSlot.JACKET || slot == EquipmentAccessoriesSlot.GLOVES)
                textureSlim = new ResourceLocation(this.getRegistryName().getNamespace(), "textures/accessories/" + this.getRegistryName().getPath() +  "_slim.png");
        }
        return HUPlayerUtil.haveSmallArms(entity) && (slot == EquipmentAccessoriesSlot.TSHIRT || slot == EquipmentAccessoriesSlot.JACKET || slot == EquipmentAccessoriesSlot.GLOVES)? this.textureSlim: this.texture;
    }

    @Override
    public EquipmentAccessoriesSlot getSlot() {
        return this.slot;
    }


    @Override
    public float getScale(ItemStack stack) {
        if (this.slot == EquipmentAccessoriesSlot.JACKET) {
            return 0.33F;
        }
        if (this.slot == EquipmentAccessoriesSlot.GLOVES) {
            return 0.09F;
        }
        return IAccessory.super.getScale(stack);
    }
}