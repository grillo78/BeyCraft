package com.beycraft.common.block;

import com.beycraft.common.block_entity.BeycreatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class BeycreatorBlock extends HorizontalBlock {

    private VoxelShape shapeNorth = Stream.of(
            Block.box(13, 1, 1.75, 15, 3, 3.75),
            Block.box(11.25, 0.25, 0.25, 12.25, 1.25, 1.25),
            Block.box(13.5, 2.1, 2.25, 14.5, 3.1, 3.25),
            Block.box(14.25, 0.25, 0.25, 15.25, 1.25, 1.25),
            Block.box(12.75, 0.25, 0.25, 13.75, 1.25, 1.25),
            Block.box(0, 1, 15, 16, 16, 16),
            Block.box(0, 1, 10, 1, 10, 15),
            Block.box(15, 1, 10, 16, 10, 15),
            Block.box(0, 10, 0, 1, 16, 15),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(1, 15, 0, 15, 16, 15),
            Block.box(15, 10, 0, 16, 16, 15)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    private VoxelShape shapeEast = Stream.of(
            Block.box(12.25, 1, 13, 14.25, 3, 15),
            Block.box(12.75, 2.1, 13.5, 13.75, 3.1, 14.5),
            Block.box(14.75, 0.25, 14.25, 15.75, 1.25, 15.25),
            Block.box(14.75, 0.25, 12.75, 15.75, 1.25, 13.75),
            Block.box(14.75, 0.25, 11.25, 15.75, 1.25, 12.25),
            Block.box(1, 10, 15, 16, 16, 16),
            Block.box(1, 15, 1, 16, 16, 15),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(1, 10, 0, 16, 16, 1),
            Block.box(1, 1, 15, 6, 10, 16),
            Block.box(1, 1, 0, 6, 10, 1),
            Block.box(0, 1, 0, 1, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    private VoxelShape shapeWest = Stream.of(
            Block.box(1.75, 1, 1, 3.75, 3, 3),
            Block.box(2.25, 2.1, 1.5, 3.25, 3.1, 2.5),
            Block.box(0.25, 0.25, 0.75, 1.25, 1.25, 1.75),
            Block.box(0.25, 0.25, 2.25, 1.25, 1.25, 3.25),
            Block.box(0.25, 0.25, 3.75, 1.25, 1.25, 4.75),
            Block.box(0, 10, 0, 15, 16, 1),
            Block.box(0, 15, 1, 15, 16, 15),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 10, 15, 15, 16, 16),
            Block.box(10, 1, 0, 15, 10, 1),
            Block.box(10, 1, 15, 15, 10, 16),
            Block.box(15, 1, 0, 16, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    private VoxelShape shapeSouth = Stream.of(
            Block.box(1, 1, 12.25, 3, 3, 14.25),
            Block.box(1.5, 2.1, 12.75, 2.5, 3.1, 13.75),
            Block.box(0.75, 0.25, 14.75, 1.75, 1.25, 15.75),
            Block.box(2.25, 0.25, 14.75, 3.25, 1.25, 15.75),
            Block.box(3.75, 0.25, 14.75, 4.75, 1.25, 15.75),
            Block.box(0, 10, 1, 1, 16, 16),
            Block.box(1, 15, 1, 15, 16, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(15, 10, 1, 16, 16, 16),
            Block.box(0, 1, 1, 1, 10, 6),
            Block.box(15, 1, 1, 16, 10, 6),
            Block.box(0, 1, 0, 16, 16, 1)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    protected BeycreatorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        VoxelShape shape = null;
        switch (p_220053_1_.getValue(FACING)) {
            case NORTH:
                shape = shapeNorth;
                break;
            case EAST:
                shape = shapeEast;
                break;
            case WEST:
                shape = shapeWest;
                break;
            case SOUTH:
                shape = shapeSouth;
                break;
        }
        return shape;
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!level.isClientSide) {
            Vector3d hitOffset = result.getLocation().add(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());

            AxisAlignedBB buttonLeft;
            AxisAlignedBB buttonRight;
            AxisAlignedBB buttonAccept;
            switch (state.getValue(FACING)) {
                case NORTH:

                    buttonLeft = new AxisAlignedBB(0.890625, 0.01525, 0.01525, 0.953125, 0.08125, 0.078125);
                    buttonRight = new AxisAlignedBB(0.796875, 0.01525, 0.01525, 0.859375, 0.08125, 0.078125);
                    buttonAccept = new AxisAlignedBB(0.703125, 0.01525, 0.01525, 0.765625, 0.08125, 0.078125);
                    break;
                case EAST:
                    buttonLeft = new AxisAlignedBB(0.921875, 0.01525, 0.890625, 0.984375, 0.08125, 0.953125);
                    buttonRight = new AxisAlignedBB(0.921875, 0.01525, 0.796875, 0.984375, 0.08125, 0.859375);
                    buttonAccept = new AxisAlignedBB(0.921875, 0.01525, 0.703125, 0.984375, 0.08125, 0.765625);
                    break;
                case WEST:
                    buttonLeft = new AxisAlignedBB(0.01525, 0.01525, 0.046875, 0.08125, 0.08125, 0.109375);
                    buttonRight = new AxisAlignedBB(0.01525, 0.01525, 0.140625, 0.08125, 0.08125, 0.203125);
                    buttonAccept = new AxisAlignedBB(0.01525, 0.01525, 0.234375, 0.08125, 0.08125, 0.296875);
                    Block.box(0.25, 0.25, 0.75, 1.25, 1.25, 1.75);
                    Block.box(0.25, 0.25, 2.25, 1.25, 1.25, 3.25);
                    Block.box(0.25, 0.25, 3.75, 1.25, 1.25, 4.75);
                    break;
                case SOUTH:
                    buttonLeft = new AxisAlignedBB(0.046875, 0.01525, 0.921875, 0.109375, 0.08125, 0.984375);
                    buttonRight = new AxisAlignedBB(0.140625, 0.01525, 0.921875, 0.203125, 0.08125, 0.984375);
                    buttonAccept = new AxisAlignedBB(0.234375, 0.01525, 0.921875, 0.296875, 0.08125, 0.984375);
                    Block.box(0.75, 0.25, 14.75, 1.75, 1.25, 15.75);
                    Block.box(2.25, 0.25, 14.75, 3.25, 1.25, 15.75);
                    Block.box(3.75, 0.25, 14.75, 4.75, 1.25, 15.75);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + state.getValue(FACING));
            }
            System.out.println("Hit offset: " + hitOffset);
            System.out.println("In button 1: " + buttonLeft.contains(hitOffset));
            System.out.println("In button 2: " + buttonRight.contains(hitOffset));
            System.out.println("In button 3: " + buttonAccept.contains(hitOffset));
            if (buttonLeft.contains(hitOffset))
                ((BeycreatorTileEntity) level.getBlockEntity(blockPos)).increaseIndex(-1);
            else if (buttonRight.contains(hitOffset))
                ((BeycreatorTileEntity) level.getBlockEntity(blockPos)).increaseIndex(1);
            else if (buttonAccept.contains(hitOffset))
                ((BeycreatorTileEntity) level.getBlockEntity(blockPos)).select();
            else {
                if(((BeycreatorTileEntity) level.getBlockEntity(blockPos)).getSlot().getItem() != Items.AIR){
                    level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((BeycreatorTileEntity) level.getBlockEntity(blockPos)).getSlot()));

                }
                ((BeycreatorTileEntity) level.getBlockEntity(blockPos)).setSlot(player.getItemInHand(hand).copy());
                ((BeycreatorTileEntity)level.getBlockEntity(blockPos)).getSlot().setCount(1);
                player.getItemInHand(hand).shrink(1);
            }
            level.sendBlockUpdated(blockPos, state, state, 0);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BeycreatorTileEntity();
    }
}
