package com.grillo78.BeyCraft.blocks;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.BeyRegistry;
import com.grillo78.BeyCraft.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author a19guillermong
 *
 */
public class ExpositoryBlock extends Block implements IWaterLoggable {

	protected static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

	public ExpositoryBlock(Material materialIn, String name) {
		super(Block.Properties.create(Material.CLAY));
		setRegistryName(new ResourceLocation(Reference.MODID,name));

		BeyRegistry.BLOCKS.add(this);
		BeyRegistry.ITEMS.add(new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB)).setRegistryName(this.getRegistryName()));
	}
	
//	@Override
//	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//		if(worldIn.getTileEntity(pos) instanceof ExpositoryTileEntity) {
//			ExpositoryTileEntity tileentity = (ExpositoryTileEntity)worldIn.getTileEntity(pos);
//			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.getStackInSlot(0)));
//			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.getStackInSlot(1)));
//			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.getStackInSlot(2)));
//		}
//		super.breakBlock(worldIn, pos, state);
//	}
//	
//	@Override
//	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
//			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//		TileEntity tileentity = worldIn.getTileEntity(pos);
//
//		if (tileentity instanceof ExpositoryTileEntity) {
//			if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer
//					&& ((ExpositoryTileEntity) tileentity).getStackInSlot(0).getCount() == 0) {
//				((ExpositoryTileEntity) tileentity).setInventorySlotContents(0, playerIn.getHeldItem(hand).copy());
//				playerIn.getHeldItem(hand).shrink(1);
//				return true;
//			} else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDisk
//					&& ((ExpositoryTileEntity) tileentity).getStackInSlot(1).getCount() == 0) {
//				((ExpositoryTileEntity) tileentity).setInventorySlotContents(1, playerIn.getHeldItem(hand).copy());
//				playerIn.getHeldItem(hand).shrink(1);
//				return true;
//			} else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDriver
//					&& ((ExpositoryTileEntity) tileentity).getStackInSlot(2).getCount() == 0) {
//				((ExpositoryTileEntity) tileentity).setInventorySlotContents(2, playerIn.getHeldItem(hand).copy());
//				playerIn.getHeldItem(hand).shrink(1);
//				return true;
//			}else if (playerIn.getHeldItem(hand).getItem() instanceof ItemLauncher) {
//				for (int i = 0; i < 3; i++) {
//					if(playerIn.getHeldItem(hand)
//							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//							.getStackInSlot(i).getCount()==0 && playerIn.getHeldItem(hand).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isItemValid(i, ((ExpositoryTileEntity) tileentity).getStackInSlot(i))) {
//						playerIn.getHeldItem(hand)
//						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)
//						.insertItem(i, ((ExpositoryTileEntity) tileentity).getStackInSlot(i).copy(), false);
//				((ExpositoryTileEntity) tileentity).getStackInSlot(i).shrink(1);
//					}
//				}
//				return true;
//			} else {
//				for (int i = 0; i < 3; i++) {
//					if (((ExpositoryTileEntity) tileentity).getStackInSlot(i) != ItemStack.EMPTY) {
//						if (!worldIn.isRemote) {
//							worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(),
//									((ExpositoryTileEntity) tileentity).getStackInSlot(i).copy()));
//						}
//						((ExpositoryTileEntity) tileentity).getStackInSlot(i).shrink(1);
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//
//	}
//
//	@Override
//	public boolean hasTileEntity() {
//		return true;
//	}
//
//	@Override
//	public boolean isFullCube(IBlockState state) {
//		return false;
//	}
//
//	@Override
//	public boolean isOpaqueCube(IBlockState state) {
//		return false;
//	}
//
//	@Override
//	public boolean isTranslucent(IBlockState state) {
//		return true;
//	}
//
//	@Override
//	public BlockRenderLayer getBlockLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
//
//	@Override
//	public EnumBlockRenderType getRenderType(IBlockState state) {
//		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
//	}
//
//	@Override
//	public TileEntity createNewTileEntity(IBlockReader worldIn) {
//		// TODO Auto-generated method stub
//		return new ExpositoryTileEntity(new TileEntityType<>);
//	}
}
