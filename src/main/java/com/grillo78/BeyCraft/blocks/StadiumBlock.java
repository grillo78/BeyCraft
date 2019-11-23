package com.grillo78.BeyCraft.blocks;

import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ModelLoader;

public class StadiumBlock extends Block implements IHasModel {

	public static final PropertyDirection FACING = BlockDirectional.FACING;

	public StadiumBlock(Material materialIn, String name) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(1);
		setHarvestLevel("pickaxe", 0);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

		BeyRegistry.BLOCKS.add(this);
		BeyRegistry.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

}
