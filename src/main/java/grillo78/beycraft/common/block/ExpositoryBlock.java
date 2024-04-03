package grillo78.beycraft.common.block;

import grillo78.beycraft.common.block_entity.ExpositoryTileEntity;
import grillo78.beycraft.common.item.BeyPartItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class ExpositoryBlock extends Block {

    private VoxelShape collisionBox = VoxelShapes.box(0.3,0,0.3,0.7,0.5,0.7);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ExpositoryBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
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
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (worldIn.getBlockEntity(pos) instanceof ExpositoryTileEntity) {
            ExpositoryTileEntity tileEntity = (ExpositoryTileEntity) worldIn.getBlockEntity(pos);
            tileEntity.getInventory().ifPresent(h -> {
                worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0)));
            });
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                Hand hand, BlockRayTraceResult p_225533_6_) {
        if(playerIn.getItemInHand(hand).getItem() == Items.WATER_BUCKET||playerIn.getItemInHand(hand).getItem() == Items.BUCKET){
            return super.use(state,worldIn,pos,playerIn,hand,p_225533_6_);
        }
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if(!worldIn.isClientSide){
            if (tileentity instanceof ExpositoryTileEntity) {
                ((ExpositoryTileEntity) tileentity).getInventory().ifPresent(h -> {
                    if(h.getStackInSlot(0)!=ItemStack.EMPTY){
                        worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                        h.extractItem(0,1,false);
                    }
                    if (playerIn.getItemInHand(hand).getItem() instanceof BeyPartItem) {
                        h.insertItem(0,playerIn.getItemInHand(hand).copy(),false);
                        playerIn.getItemInHand(hand).shrink(1);
                    }
                });
                worldIn.sendBlockUpdated(pos, state, state, 0);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return collisionBox;
    }
}
