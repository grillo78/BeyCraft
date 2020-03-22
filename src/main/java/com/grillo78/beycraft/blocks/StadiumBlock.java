package com.grillo78.beycraft.blocks;

import java.util.stream.Stream;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StadiumBlock extends Block implements IWaterLoggable {

    public static final EnumProperty<StadiumBlock.EnumPartType> PART = EnumProperty.<StadiumBlock.EnumPartType>create(
            "part", StadiumBlock.EnumPartType.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private VoxelShape voxelShape1;
    private VoxelShape voxelShape2;
    private VoxelShape voxelShape3;
    private VoxelShape voxelShape4;
    private VoxelShape voxelShape5;
    private VoxelShape voxelShape6;
    private VoxelShape voxelShape7;
    private VoxelShape voxelShape8;
    private VoxelShape voxelShape9;

    public StadiumBlock(Material materialIn, String name) {
        super(Block.Properties.create(materialIn));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        setVoxelShapes();
        BeyRegistry.BLOCKS.add(this);
        BeyRegistry.ITEMS.put(name,new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB))
                .setRegistryName(getRegistryName()));
        setDefaultState(this.stateContainer.getBaseState().with(PART, EnumPartType.MIDDLECENTER));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(PART).ID) {
            case 0:
                return voxelShape1;
            case 1:
                return voxelShape2;
            case 2:
                return voxelShape3;
            case 3:
                return voxelShape4;
            case 4:
                return voxelShape5;
            case 5:
                return voxelShape6;
            case 6:
                return voxelShape7;
            case 7:
                return voxelShape8;
            case 8:
                return voxelShape9;
            default:
                return VoxelShapes.fullCube();
        }

    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return canReplace(worldIn, pos.north().west()) && canReplace(worldIn, pos.north())
                && canReplace(worldIn, pos.north().east()) && canReplace(worldIn, pos.west())
                && canReplace(worldIn, pos) && canReplace(worldIn, pos.east())
                && canReplace(worldIn, pos.south().west()) && canReplace(worldIn, pos.south())
                && canReplace(worldIn, pos.south().east());
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (worldIn.getBlockState(pos.east().north()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.east().north()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.east().north(),
                    getDefaultState().with(PART, EnumPartType.values()[0]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.east().north(),
                    getDefaultState().with(PART, EnumPartType.values()[0]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.east()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.east()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.east(),
                    getDefaultState().with(PART, EnumPartType.values()[1]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.east(),
                    getDefaultState().with(PART, EnumPartType.values()[1]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.east().south()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.east().south()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.east().south(),
                    getDefaultState().with(PART, EnumPartType.values()[2]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.east().south(),
                    getDefaultState().with(PART, EnumPartType.values()[2]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.north()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.north()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.north(),
                    getDefaultState().with(PART, EnumPartType.values()[3]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.north(),
                    getDefaultState().with(PART, EnumPartType.values()[3]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
//        worldIn.setBlockState(pos,
//                worldIn.getBlockState(pos).with(PART, EnumPartType.values()[4]));
        if (worldIn.getBlockState(pos.south()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.south()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.south(),
                    getDefaultState().with(PART, EnumPartType.values()[5]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.south(),
                    getDefaultState().with(PART, EnumPartType.values()[5]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.west().north()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.west().north()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.west().north(),
                    getDefaultState().with(PART, EnumPartType.values()[6]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.west().north(),
                    getDefaultState().with(PART, EnumPartType.values()[6]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.west()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.west()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.west(),
                    getDefaultState().with(PART, EnumPartType.values()[7]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.west(),
                    getDefaultState().with(PART, EnumPartType.values()[7]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (worldIn.getBlockState(pos.west().south()).getBlock() == Blocks.WATER && worldIn.getBlockState(pos.west().south()).getFluidState().isSource()) {
            worldIn.setBlockState(pos.west().south(),
                    getDefaultState().with(PART, EnumPartType.values()[8]).with(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            worldIn.setBlockState(pos.west().south(),
                    getDefaultState().with(PART, EnumPartType.values()[8]).with(WATERLOGGED, Boolean.valueOf(false)));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(WATERLOGGED, Boolean.valueOf(context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART);
        builder.add(WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() != this) {
            if (worldIn.getBlockState(pos.north()).getBlock() instanceof StadiumBlock) {
                if (worldIn.getBlockState(pos.north()).getFluidState().isEmpty()) {
                    worldIn.setBlockState(pos.north(), Blocks.AIR.getDefaultState());
                } else {
                    worldIn.setBlockState(pos.north(), Blocks.WATER.getDefaultState());
                }
            }
            if (worldIn.getBlockState(pos.south()).getBlock() instanceof StadiumBlock) {
                if (worldIn.getBlockState(pos.south()).getFluidState().isEmpty()) {
                    worldIn.setBlockState(pos.south(), Blocks.AIR.getDefaultState());
                } else {
                    worldIn.setBlockState(pos.south(), Blocks.WATER.getDefaultState());
                }
            }
            if (worldIn.getBlockState(pos.west()).getBlock() instanceof StadiumBlock) {
                if (worldIn.getBlockState(pos.west()).getFluidState().isEmpty()) {
                    worldIn.setBlockState(pos.west(), Blocks.AIR.getDefaultState());
                } else {
                    worldIn.setBlockState(pos.west(), Blocks.WATER.getDefaultState());
                }
            }
            if (worldIn.getBlockState(pos.east()).getBlock() instanceof StadiumBlock) {
                if (worldIn.getBlockState(pos.east()).getFluidState().isEmpty()) {
                    worldIn.setBlockState(pos.east(), Blocks.AIR.getDefaultState());
                } else {
                    worldIn.setBlockState(pos.east(), Blocks.WATER.getDefaultState());
                }
            }
        }
    }

    private void setVoxelShapes() {
        voxelShape1 = Stream
                .of(Block.makeCuboidShape(1, 0, 14, 15, 6, 15), Block.makeCuboidShape(1, 0, 13, 14, 6, 14),
                        Block.makeCuboidShape(1, 0, 12, 13, 6, 13), Block.makeCuboidShape(1, 0, 11, 12, 6, 12),
                        Block.makeCuboidShape(1, 0, 10, 11, 6, 11), Block.makeCuboidShape(1, 0, 8, 9, 6, 9),
                        Block.makeCuboidShape(1, 0, 7, 8, 6, 8), Block.makeCuboidShape(1, 0, 6, 7, 6, 7),
                        Block.makeCuboidShape(1, 0, 5, 6, 6, 6), Block.makeCuboidShape(1, 0, 4, 5, 6, 5),
                        Block.makeCuboidShape(1, 0, 3, 4, 6, 4), Block.makeCuboidShape(1, 0, 2, 3, 6, 3),
                        Block.makeCuboidShape(1, 0, 1, 2, 6, 2), Block.makeCuboidShape(1, 6, 1, 2, 8, 2),
                        Block.makeCuboidShape(11, 6, 11, 12, 8, 12), Block.makeCuboidShape(2, 6, 2, 3, 8, 3),
                        Block.makeCuboidShape(12, 6, 12, 13, 8, 13), Block.makeCuboidShape(3, 6, 3, 4, 8, 4),
                        Block.makeCuboidShape(13, 6, 13, 14, 8, 14), Block.makeCuboidShape(4, 6, 4, 5, 8, 5),
                        Block.makeCuboidShape(14, 6, 14, 15, 8, 15), Block.makeCuboidShape(1, 0, 9, 10, 6, 10))
                .reduce((v1, v2) -> {
                    return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
                }).get();
        voxelShape2 = VoxelShapes.combineAndSimplify(
                Block.makeCuboidShape(-2.5500000000000007, 0, -1.5, 14.936, 6, 17.5),
                Block.makeCuboidShape(13.4357, 6.01, -1.29369, 14.4357, 8.01, 17.29231), IBooleanFunction.OR);
        voxelShape3 = Stream
                .of(Block.makeCuboidShape(1, 0, 1, 2, 6, 15), Block.makeCuboidShape(2, 0, 1, 3, 6, 14),
                        Block.makeCuboidShape(3, 0, 1, 4, 6, 13), Block.makeCuboidShape(4, 0, 1, 5, 6, 12),
                        Block.makeCuboidShape(5, 0, 1, 6, 6, 11), Block.makeCuboidShape(7, 0, 1, 8, 6, 9),
                        Block.makeCuboidShape(8, 0, 1, 9, 6, 8), Block.makeCuboidShape(9, 0, 1, 10, 6, 7),
                        Block.makeCuboidShape(10, 0, 1, 11, 6, 6), Block.makeCuboidShape(11, 0, 1, 12, 6, 5),
                        Block.makeCuboidShape(12, 0, 1, 13, 6, 4), Block.makeCuboidShape(13, 0, 1, 14, 6, 3),
                        Block.makeCuboidShape(14, 0, 1, 15, 6, 2), Block.makeCuboidShape(14, 6, 1, 15, 8, 2),
                        Block.makeCuboidShape(4, 6, 11, 5, 8, 12), Block.makeCuboidShape(13, 6, 2, 14, 8, 3),
                        Block.makeCuboidShape(3, 6, 12, 4, 8, 13), Block.makeCuboidShape(12, 6, 3, 13, 8, 4),
                        Block.makeCuboidShape(2, 6, 13, 3, 8, 14), Block.makeCuboidShape(11, 6, 4, 12, 8, 5),
                        Block.makeCuboidShape(1, 6, 14, 2, 8, 15), Block.makeCuboidShape(6, 0, 1, 7, 6, 10))
                .reduce((v1, v2) -> {
                    return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
                }).get();
        voxelShape4 = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(-1.5, 0, 1.064, 17.5, 6, 18.55),
                Block.makeCuboidShape(-1.29369, 6.01, 1.5642999999999994, 17.29231, 8.01, 2.5642999999999994),
                IBooleanFunction.OR);
        voxelShape5 = Block.makeCuboidShape(1.5, 0.01, 1.5, 14.5, 6, 14.5);
        voxelShape6 = VoxelShapes.combineAndSimplify(
                Block.makeCuboidShape(-1.5, 0, -2.5500000000000007, 17.5, 6, 14.936),
                Block.makeCuboidShape(-1.29231, 6.01, 13.4357, 17.29369, 8.01, 14.4357), IBooleanFunction.OR);
        voxelShape7 = Stream
                .of(Block.makeCuboidShape(14, 0, 1, 15, 6, 15), Block.makeCuboidShape(13, 0, 2, 14, 6, 15),
                        Block.makeCuboidShape(12, 0, 3, 13, 6, 15), Block.makeCuboidShape(11, 0, 4, 12, 6, 15),
                        Block.makeCuboidShape(10, 0, 5, 11, 6, 15), Block.makeCuboidShape(8, 0, 7, 9, 6, 15),
                        Block.makeCuboidShape(7, 0, 8, 8, 6, 15), Block.makeCuboidShape(6, 0, 9, 7, 6, 15),
                        Block.makeCuboidShape(5, 0, 10, 6, 6, 15), Block.makeCuboidShape(4, 0, 11, 5, 6, 15),
                        Block.makeCuboidShape(3, 0, 12, 4, 6, 15), Block.makeCuboidShape(2, 0, 13, 3, 6, 15),
                        Block.makeCuboidShape(1, 0, 14, 2, 6, 15), Block.makeCuboidShape(1, 6, 14, 2, 8, 15),
                        Block.makeCuboidShape(11, 6, 4, 12, 8, 5), Block.makeCuboidShape(2, 6, 13, 3, 8, 14),
                        Block.makeCuboidShape(12, 6, 3, 13, 8, 4), Block.makeCuboidShape(3, 6, 12, 4, 8, 13),
                        Block.makeCuboidShape(13, 6, 2, 14, 8, 3), Block.makeCuboidShape(4, 6, 11, 5, 8, 12),
                        Block.makeCuboidShape(14, 6, 1, 15, 8, 2), Block.makeCuboidShape(9, 0, 6, 10, 6, 15))
                .reduce((v1, v2) -> {
                    return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
                }).get();
        voxelShape8 = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(1.064, 0, -1.5, 18.55, 6, 17.5),
                Block.makeCuboidShape(1.5642999999999994, 6.01, -1.29231, 2.5642999999999994, 8.01, 17.29369),
                IBooleanFunction.OR);
        voxelShape9 = Stream
                .of(Block.makeCuboidShape(1, 0, 1, 15, 6, 2), Block.makeCuboidShape(2, 0, 2, 15, 6, 3),
                        Block.makeCuboidShape(3, 0, 3, 15, 6, 4), Block.makeCuboidShape(4, 0, 4, 15, 6, 5),
                        Block.makeCuboidShape(5, 0, 5, 15, 6, 6), Block.makeCuboidShape(7, 0, 7, 15, 6, 8),
                        Block.makeCuboidShape(8, 0, 8, 15, 6, 9), Block.makeCuboidShape(9, 0, 9, 15, 6, 10),
                        Block.makeCuboidShape(10, 0, 10, 15, 6, 11), Block.makeCuboidShape(11, 0, 11, 15, 6, 12),
                        Block.makeCuboidShape(12, 0, 12, 15, 6, 13), Block.makeCuboidShape(13, 0, 13, 15, 6, 14),
                        Block.makeCuboidShape(14, 0, 14, 15, 6, 15), Block.makeCuboidShape(14, 6, 14, 15, 8, 15),
                        Block.makeCuboidShape(4, 6, 4, 5, 8, 5), Block.makeCuboidShape(13, 6, 13, 14, 8, 14),
                        Block.makeCuboidShape(3, 6, 3, 4, 8, 4), Block.makeCuboidShape(12, 6, 12, 13, 8, 13),
                        Block.makeCuboidShape(2, 6, 2, 3, 8, 3), Block.makeCuboidShape(11, 6, 11, 12, 8, 12),
                        Block.makeCuboidShape(1, 6, 1, 2, 8, 2), Block.makeCuboidShape(6, 0, 6, 15, 6, 7))
                .reduce((v1, v2) -> {
                    return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
                }).get();
    }

    private boolean canReplace(IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.WATER || world.getBlockState(pos).getBlock() == Blocks.LAVA;
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
