package com.grillo78.beycraft.blocks;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * @author grillo78
 */
public class ExpositoryBlock extends Block implements IWaterLoggable {

    private VoxelShape collisionBox = VoxelShapes.create(0,0,0,1,0.5,1);

    public ExpositoryBlock(Material materialIn, String name) {
        super(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.6F).harvestTool(ToolType.PICKAXE));
        setRegistryName(new ResourceLocation(Reference.MODID, name));

        BeyRegistry.BLOCKS.add(this);
        BeyRegistry.ITEMS.add(new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB))
                .setRegistryName(this.getRegistryName()));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExpositoryTileEntity();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (worldIn.getTileEntity(pos) instanceof ExpositoryTileEntity) {
            ExpositoryTileEntity tileEntity = (ExpositoryTileEntity) worldIn.getTileEntity(pos);
            tileEntity.getInventory().ifPresent(h -> {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0)));
            });
        }
        worldIn.addEntity(new ItemEntity(worldIn,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(this)));
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                             Hand hand, BlockRayTraceResult p_225533_6_) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ExpositoryTileEntity) {
            if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer) {
                setItemStack((ExpositoryTileEntity) tileentity, 0, playerIn.getHeldItem(hand));
            } else {
                ((ExpositoryTileEntity) tileentity).getInventory().ifPresent(h -> {
                    if(h.getStackInSlot(0)!=ItemStack.EMPTY){
                        worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                    }
                });
                setItemStack((ExpositoryTileEntity) tileentity, 0, ItemStack.EMPTY);
            }
        }
        return ActionResultType.SUCCESS;
    }

    /**
     *
     */
    private void setItemStack(ExpositoryTileEntity tileEntity, int index, ItemStack stack) {
        tileEntity.getInventory().ifPresent(h -> {
            if(stack!=ItemStack.EMPTY) {
                h.insertItem(index, stack.copy(), false);
                stack.shrink(1);
            } else {
                h.extractItem(0,1,false);
            }
        });
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return collisionBox;
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
