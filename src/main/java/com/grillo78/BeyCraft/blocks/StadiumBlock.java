package com.grillo78.BeyCraft.blocks;

import java.util.List;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;
import com.grillo78.BeyCraft.entity.EntityBey;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StadiumBlock extends Block implements IHasModel {

	public static final PropertyEnum<StadiumBlock.EnumPartType> PART = PropertyEnum.<StadiumBlock.EnumPartType>create(
			"part", StadiumBlock.EnumPartType.class);
	protected static final AxisAlignedBB MIDDLECENTER_BOX = new AxisAlignedBB(-1.0D + 0.0625, 0.0D, -1.0D + 0.0625,
			2.0D - 0.0625D, 0.0625 * 6D, 2.0D - 0.0625D);
	protected static final AxisAlignedBB MIDDLELEFT_BOX = new AxisAlignedBB(2 - 0.0625, 0.0D, 3 - 0.0625, -1 + 0.0625D,
			0.375D, 0.0625D);
	protected static final AxisAlignedBB MIDDLERIGHT_BOX = new AxisAlignedBB(2 - 0.0625, 0.0D, 1 - 0.0625, -1 + 0.0625D,
			0.375D, -2 + 0.0625D);
	protected static final AxisAlignedBB TOPLEFT_BOX = new AxisAlignedBB(-2 + 0.0625, 0.0D, 0.0625, 1 - 0.0625D,
			0.0625 * 6D, 3 - 0.0625D);
	protected static final AxisAlignedBB TOPCENTER_BOX = new AxisAlignedBB(-2 + 0.0625, 0.0D, -1.0625, 1 - 0.0625D,
			0.375D, 2 - 0.0625D);
	protected static final AxisAlignedBB TOPRIGHT_BOX = new AxisAlignedBB(-2 + 0.0625, 0.0D, -2.0625, 1 - 0.0625D,
			0.375D, 1 - 0.0625D);
	protected static final AxisAlignedBB BOTTOMLEFT_BOX = new AxisAlignedBB(3 - 0.0625, 0.0D, 3 - 0.0625, 0.0625D,
			0.375D, 0.0625D);
	protected static final AxisAlignedBB BOTTOMCENTER_BOX = new AxisAlignedBB(0.0625, 0.0D, -1.0D + 0.0625,
			3.0D - 0.0625D, 0.0625 * 6D, 2.0D - 0.0625D);
	protected static final AxisAlignedBB BOTTOMRIGHT_BOX = new AxisAlignedBB(0.0625, 0.0D, 1 - 0.0625, 3 - 0.0625D,
			0.375D, -2 + 0.0625D);

	public StadiumBlock(Material materialIn, String name) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Reference.MODID,name));
		setHardness(1);
		setHarvestLevel("pickaxe", 0);
		setCreativeTab(BeyCraft.BEYCRAFTTAB);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, EnumPartType.MIDDLECENTER));

		BeyRegistry.BLOCKS.add(this);
		BeyRegistry.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumPartType part = (EnumPartType) state.getValue(PART);
		return part.getID();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PART, EnumPartType.values()[meta]);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (getMetaFromState(state)) {
		case 0:
			return TOPLEFT_BOX;
		case 1:
			return TOPCENTER_BOX;
		case 2:
			return TOPRIGHT_BOX;
		case 3:
			return MIDDLELEFT_BOX;
		case 4:
			return MIDDLECENTER_BOX;
		case 5:
			return MIDDLERIGHT_BOX;
		case 6:
			return BOTTOMLEFT_BOX;
		case 7:
			return BOTTOMCENTER_BOX;
		case 8:
			return BOTTOMRIGHT_BOX;
		default:
			return new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityBey) {

		}
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {

		if (getMetaFromState(state) == 0) {
			// floor
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 2, 0, 1, 0, 0.0625 * 6, 0.0625));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625, 0.0625 * 6, 0.0625 * 2));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 4, 0, 1, 0.0625 * 2, 0.0625 * 6, 0.0625 * 3));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 5, 0, 1 - 0.0625 * 4, 0.0625 * 3, 0.0625 * 6, 0.0625 * 4));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 5, 0.0625 * 6, 0.0625 * 5));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 6, 0.0625 * 6, 0.0625 * 6));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 7, 0.0625 * 6, 0.0625 * 7));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 9, 0.0625 * 6, 0.0625 * 9));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 10, 0.0625 * 6, 0.0625 * 10));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 11, 0.0625 * 6, 0.0625 * 11));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 12, 0.0625 * 6, 0.0625 * 12));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 13, 0.0625 * 6, 0.0625 * 13));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 14, 0.0625 * 6, 0.0625 * 14));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 15, 0.0625 * 6, 0.0625 * 15));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 16, 0.0625 * 6, 0.0625 * 16));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 17, 0.0625 * 6, 0.0625 * 17));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 18, 0.0625 * 6, 0.0625 * 18));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625 * 19, 0.0625 * 6, 0.0625 * 19));

			if (!(entityIn instanceof EntityPlayer)) {
				// walls
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 2, 0, 0.0625, 1 - 0.0625 * 16, 1, 0.0625 * 2));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 4, 0, 0.0625 * 3, 1 - 0.0625 * 14, 1, 0.0625 * 3));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 5, 0, 0.0625 * 4, 1 - 0.0625 * 14, 1, 0.0625 * 4));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 14, 0, 0.0625 * 15, 1 - 0.0625, 1, 0.0625 * 13));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 13, 0, 0.0625 * 14, 1 - 0.0625 * 2, 1, 0.0625 * 12));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 12, 0, 0.0625 * 13, 1 - 0.0625 * 3, 1, 0.0625 * 11));
			}
		}
		if (getMetaFromState(state) == 1) {

			if (!(entityIn instanceof EntityPlayer)) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(1 - 0.0625, 0, 1, 1 - 0.0625 * 3, 1, 0));
			}
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1));
		}
		if (getMetaFromState(state) == 2) {
			// floor
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625, 0.0625 * 6, 0.0625 * 2));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 2, 0.0625 * 6, 0.0625 * 3));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 3, 0.0625 * 6, 0.0625 * 4));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 4, 0.0625 * 6, 0.0625 * 5));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 5, 0.0625 * 6, 0.0625 * 6));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 6, 0.0625 * 6, 0.0625 * 7));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 7, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 1, 0.0625 * 6, 0.0625 * 9));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 9, 0.0625 * 6, 0.0625 * 10));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 10, 0.0625 * 6, 0.0625 * 11));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 11, 0.0625 * 6, 0.0625 * 12));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 12, 0.0625 * 6, 0.0625 * 13));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 13, 0.0625 * 6, 0.0625 * 14));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 14, 0.0625 * 6, 0.0625 * 15));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 15, 0.0625 * 6, 0.0625 * 16));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 16, 0.0625 * 6, 0.0625 * 17));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 17, 0.0625 * 6, 0.0625 * 18));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 18, 0.0625 * 6, 0.0625 * 19));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0, 0, 0, 1 - 0.0625 * 19, 0.0625 * 6, 0.0625 * 20));

			if (!(entityIn instanceof EntityPlayer)) {
				// walls
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 13, 0, 0.0625, 1 - 0.0625, 1, 0.0625 * 2));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 12, 0, 0.0625, 1 - 0.0625 * 2, 1, 0.0625 * 3));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 11, 0, 0.0625, 1 - 0.0625 * 3, 1, 0.0625 * 4));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625, 0, 0.0625 * 15, 1 - 0.0625 * 14, 1, 0.0625 * 13));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 2, 0, 0.0625 * 14, 1 - 0.0625 * 13, 1, 0.0625 * 12));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 3, 0, 0.0625 * 13, 1 - 0.0625 * 12, 1, 0.0625 * 11));
			}
		}
		if (getMetaFromState(state) == 3) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1));

			if (!(entityIn instanceof EntityPlayer)) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(1, 0, 0.0625 * 3, 0, 1, 0.0625));
			}
		}
		if (getMetaFromState(state) == 4) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1));
		}
		if (getMetaFromState(state) == 5) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1));

			if (!(entityIn instanceof EntityPlayer)) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(1, 0, 1 - 0.0625, 0, 1, 1 - 0.0625 * 3));
			}
		}
		if (getMetaFromState(state) == 6) {
			// floor
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 14, 0, 0.0625, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 13, 0, 0.0625 * 2, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 12, 0, 0.0625 * 3, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 11, 0, 0.0625 * 4, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 10, 0, 0.0625 * 5, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 9, 0, 0.0625 * 6, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(1, 0, 0.0625 * 7, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 7, 0, 1, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 6, 0, 0.0625 * 9, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 5, 0, 0.0625 * 10, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 4, 0, 0.0625 * 11, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 0.0625 * 12, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 2, 0, 0.0625 * 13, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 1, 0, 0.0625 * 14, 1, 0.0625 * 6, 1));

			if (!(entityIn instanceof EntityPlayer)) {
				// walls
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 13, 0, 0.0625, 1 - 0.0625, 1, 0.0625 * 2));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 12, 0, 0.0625, 1 - 0.0625 * 2, 1, 0.0625 * 3));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 11, 0, 0.0625, 1 - 0.0625 * 3, 1, 0.0625 * 4));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625, 0, 0.0625 * 15, 1 - 0.0625 * 14, 1, 0.0625 * 13));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 2, 0, 0.0625 * 14, 1 - 0.0625 * 13, 1, 0.0625 * 12));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 3, 0, 0.0625 * 13, 1 - 0.0625 * 12, 1, 0.0625 * 11));
			}
		}
		if (getMetaFromState(state) == 7) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 6, 1));

			if (!(entityIn instanceof EntityPlayer)) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 3, 0, 1, 0.0625, 1, 0));
			}
		}
		if (getMetaFromState(state) == 8) {
			// floor
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625, 0, 0, 1, 0.0625 * 6, 0.0625 * 2));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 2, 0, 0, 1, 0.0625 * 6, 0.0625 * 3));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 3, 0, 0, 1, 0.0625 * 6, 0.0625 * 4));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 4, 0, 0, 1, 0.0625 * 6, 0.0625 * 5));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 5, 0, 0, 1, 0.0625 * 6, 0.0625 * 6));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 6, 0, 0, 1, 0.0625 * 6, 0.0625 * 7));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 7, 0, 0, 1, 0.0625 * 6, 1));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(1, 0, 0, 1, 0.0625 * 6, 0.0625 * 9));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 9, 0, 0, 1, 0.0625 * 6, 0.0625 * 10));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 10, 0, 0, 1, 0.0625 * 6, 0.0625 * 11));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 11, 0, 0, 1, 0.0625 * 6, 0.0625 * 12));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 12, 0, 0, 1, 0.0625 * 6, 0.0625 * 13));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 13, 0, 0, 1, 0.0625 * 6, 0.0625 * 14));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 14, 0, 0, 1, 0.0625 * 6, 0.0625 * 15));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 15, 0, 0, 1, 0.0625 * 6, 0.0625 * 16));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 16, 0, 0, 1, 0.0625 * 6, 0.0625 * 17));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 17, 0, 0, 1, 0.0625 * 6, 0.0625 * 18));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 18, 0, 0, 1, 0.0625 * 6, 0.0625 * 19));
			addCollisionBoxToList(pos, entityBox, collidingBoxes,
					new AxisAlignedBB(0.0625 * 19, 0, 0, 1, 0.0625 * 6, 0.0625 * 20));

			if (!(entityIn instanceof EntityPlayer)) {
				// walls
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 2, 0, 0.0625, 1 - 0.0625 * 16, 1, 0.0625 * 2));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 4, 0, 0.0625 * 3, 1 - 0.0625 * 14, 1, 0.0625 * 3));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 5, 0, 0.0625 * 4, 1 - 0.0625 * 14, 1, 0.0625 * 4));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 14, 0, 0.0625 * 15, 1 - 0.0625, 1, 0.0625 * 13));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 13, 0, 0.0625 * 14, 1 - 0.0625 * 2, 1, 0.0625 * 12));
				addCollisionBoxToList(pos, entityBox, collidingBoxes,
						new AxisAlignedBB(0.0625 * 12, 0, 0.0625 * 13, 1 - 0.0625 * 3, 1, 0.0625 * 11));
			}
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return isReplaceable(worldIn, pos.north().west()) && isReplaceable(worldIn, pos.north())
				&& isReplaceable(worldIn, pos.north().east()) && isReplaceable(worldIn, pos.west())
				&& isReplaceable(worldIn, pos) && isReplaceable(worldIn, pos.east())
				&& isReplaceable(worldIn, pos.south().west()) && isReplaceable(worldIn, pos.south())
				&& isReplaceable(worldIn, pos.south().east());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos.east().north(), BeyRegistry.STADIUM.getStateFromMeta(0));
		worldIn.setBlockState(pos.east(), BeyRegistry.STADIUM.getStateFromMeta(1));
		worldIn.setBlockState(pos.east().south(), BeyRegistry.STADIUM.getStateFromMeta(2));
		worldIn.setBlockState(pos.north(), BeyRegistry.STADIUM.getStateFromMeta(3));
		worldIn.setBlockState(pos, BeyRegistry.STADIUM.getStateFromMeta(4));
		worldIn.setBlockState(pos.south(), BeyRegistry.STADIUM.getStateFromMeta(5));
		worldIn.setBlockState(pos.west().north(), BeyRegistry.STADIUM.getStateFromMeta(6));
		worldIn.setBlockState(pos.west(), BeyRegistry.STADIUM.getStateFromMeta(7));
		worldIn.setBlockState(pos.west().south(), BeyRegistry.STADIUM.getStateFromMeta(8));
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getBlockState(pos.north()).getBlock() instanceof StadiumBlock) {
			worldIn.setBlockToAir(pos.north());
		}
		if (worldIn.getBlockState(pos.south()).getBlock() instanceof StadiumBlock) {
			worldIn.setBlockToAir(pos.south());
		}
		if (worldIn.getBlockState(pos.west()).getBlock() instanceof StadiumBlock) {
			worldIn.setBlockToAir(pos.west());
		}
		if (worldIn.getBlockState(pos.east()).getBlock() instanceof StadiumBlock) {
			worldIn.setBlockToAir(pos.east());
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PART });
	}

	public static enum EnumPartType implements IStringSerializable {
		TOPLEFT("topleft", 0), TOPCENTER("topcenter", 1), TOPRIGHT("topright", 2), MIDDLELEFT("middleleft", 3),
		MIDDLECENTER("middlecenter", 4), MIDDLERIGHT("middleright", 5), BOTTOMLEFT("bottomleft", 6),
		BOTTOMCENTER("bottomcenter", 7), BOTTOMRIGHT("bottomright", 8);

		private final String NAME;
		private final int ID;

		private EnumPartType(String name, int id) {
			this.NAME = name;
			this.ID = id;
		}

		public String toString() {
			return this.NAME;
		}

		public String getName() {
			return this.NAME;
		}

		public int getID() {
			return ID;
		}
	}
}
