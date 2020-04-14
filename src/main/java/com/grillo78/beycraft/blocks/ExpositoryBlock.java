package com.grillo78.beycraft.blocks;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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

import javax.annotation.Nullable;

/**
 * @author grillo78
 */
public class ExpositoryBlock extends Block implements IWaterLoggable {

    private VoxelShape collisionBox = VoxelShapes.create(0,0,0,1,0.5,1);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ExpositoryBlock(Material materialIn, String name) {
        super(Block.Properties.create(materialIn).hardnessAndResistance(0.6F).harvestTool(ToolType.PICKAXE).notSolid());
        setRegistryName(new ResourceLocation(Reference.MODID, name));

        BeyRegistry.BLOCKS.add(this);
        BeyRegistry.ITEMS.put(name,new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB))
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
        if(playerIn.getHeldItem(hand).getItem() == Items.WATER_BUCKET||playerIn.getHeldItem(hand).getItem() == Items.BUCKET){
            return super.onBlockActivated(state,worldIn,pos,playerIn,hand,p_225533_6_);
        }
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if(!worldIn.isRemote){
            if (tileentity instanceof ExpositoryTileEntity) {
                ((ExpositoryTileEntity) tileentity).getInventory().ifPresent(h -> {
                    if(h.getStackInSlot(0)!=ItemStack.EMPTY){
                        worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                        h.extractItem(0,1,false);
                    }
                    if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer || playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDisc || playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDriver) {
                        h.insertItem(0,playerIn.getHeldItem(hand).copy(),false);
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                });
                worldIn.notifyBlockUpdate(pos, state, state, 0);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(WATERLOGGED, Boolean.valueOf(context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return collisionBox;
    }
}
