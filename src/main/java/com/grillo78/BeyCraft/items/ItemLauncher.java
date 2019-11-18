package com.grillo78.BeyCraft.items;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.inventory.BeyBladeProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemLauncher extends Item{

	public ItemLauncher(String name) {
		setCreativeTab(BeyCraft.beyCraftTab);
		setRegistryName(name);
		setUnlocalizedName(name);
		setMaxStackSize(1);
		BeyRegistry.ITEMS.add(this);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if( stack.getItem() == this ) {
			return new BeyBladeProvider();
		}
		return null;
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                playerIn.openGui(BeyCraft.instance, 0, worldIn, 0, 0, 0);
            } else {
                EntityBey beyEntity = new EntityBey(worldIn, new ItemStack(BeyRegistry.ACHILLESA4),
                        new ItemStack(BeyRegistry.ELEVENDISK), new ItemStack(BeyRegistry.XTENDDRIVER));
                beyEntity.setLocationAndAngles(playerIn.posX+1, playerIn.posY, playerIn.posZ+1, 0, 0);
                worldIn.spawnEntity(beyEntity);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
