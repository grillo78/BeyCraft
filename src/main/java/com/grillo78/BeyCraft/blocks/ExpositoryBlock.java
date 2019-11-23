package com.grillo78.BeyCraft.blocks;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.items.ItemBeyDisk;
import com.grillo78.BeyCraft.items.ItemBeyDriver;
import com.grillo78.BeyCraft.items.ItemBeyLayer;
import com.grillo78.BeyCraft.items.ItemLauncher;
import com.grillo78.BeyCraft.tileentity.ExpositoryTileEntity;
import com.grillo78.BeyCraft.util.IHasModel;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author a19guillermong
 *
 */
public class ExpositoryBlock extends BlockContainer implements IHasModel {

    protected static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    
	public ExpositoryBlock(Material materialIn, String name) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(1);
		setHarvestLevel("pickaxe", 0);
		this.setCreativeTab(BeyCraft.beyCraftTab);

		BeyRegistry.BLOCKS.add(this);
		BeyRegistry.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
			return COLLISION_BOX;
		}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof ExpositoryTileEntity) {
			if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer && ((ExpositoryTileEntity)tileentity).getStackInSlot(0).getCount()==0) {
				((ExpositoryTileEntity) tileentity).setInventorySlotContents(0, playerIn.getHeldItem(hand).copy());
				playerIn.getHeldItem(hand).shrink(1);
				return true;
			} else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDisk && ((ExpositoryTileEntity)tileentity).getStackInSlot(1).getCount()==0) {
				((ExpositoryTileEntity) tileentity).setInventorySlotContents(1, playerIn.getHeldItem(hand).copy());
				playerIn.getHeldItem(hand).shrink(1);
				return true;
			} else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDriver && ((ExpositoryTileEntity)tileentity).getStackInSlot(2).getCount()==0) {
				((ExpositoryTileEntity) tileentity).setInventorySlotContents(2, playerIn.getHeldItem(hand).copy());
				playerIn.getHeldItem(hand).shrink(1);
				return true;
			}if(playerIn.getHeldItem(hand).getItem() instanceof ItemLauncher && ((ExpositoryTileEntity)tileentity).getStackInSlot(0).getCount()==1 && ((ExpositoryTileEntity)tileentity).getStackInSlot(1).getCount()==1 && ((ExpositoryTileEntity)tileentity).getStackInSlot(2).getCount()==1) {
				playerIn.getHeldItem(hand)
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
				.insertItem(0, ((ExpositoryTileEntity)tileentity).getStackInSlot(0).copy(), false);
				((ExpositoryTileEntity)tileentity).getStackInSlot(0).shrink(1);
				playerIn.getHeldItem(hand)
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
				.insertItem(1, ((ExpositoryTileEntity)tileentity).getStackInSlot(1).copy(), false);
				((ExpositoryTileEntity)tileentity).getStackInSlot(1).shrink(1);
				playerIn.getHeldItem(hand)
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
				.insertItem(2, ((ExpositoryTileEntity)tileentity).getStackInSlot(2).copy(), false);
				((ExpositoryTileEntity)tileentity).getStackInSlot(2).shrink(1);
				return true;
			}else {
				for (int i = 0; i < 3; i++) {
					if (((ExpositoryTileEntity) tileentity).getStackInSlot(i) != ItemStack.EMPTY) {
						if (!worldIn.isRemote) {
							worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
									((ExpositoryTileEntity) tileentity).getStackInSlot(i).copy()));
						}
						((ExpositoryTileEntity) tileentity).getStackInSlot(i).shrink(1);
					}
				}
			}
		}
		return false;
	}

	
	
	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
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

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ExpositoryTileEntity();
	}
}
