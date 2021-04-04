package com.grillo78.beycraft.blocks;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.gui.BeyCreatorGUI;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;


import net.minecraft.block.AbstractBlock;

public class BeyCreatorBlock extends Block {

    //    private final VoxelShape shape;
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public BeyCreatorBlock(Material materialIn, String name) {
        super(AbstractBlock.Properties.of(materialIn).strength(0.6F).noOcclusion());
        setRegistryName(new ResourceLocation(Reference.MODID, name));


        BeyRegistry.BLOCKS.add(this);
        BeyRegistry.ITEMS.put(name, new BlockItem(this, new Item.Properties().tab(BeyCraft.BEYCRAFTTAB))
                .setRegistryName(this.getRegistryName()));
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.playerWillDestroy(world, pos, state, player);
        world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
        TileEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof BeyCreatorTileEntity) {
            ((BeyCreatorTileEntity) tileentity).getInventory().ifPresent(h -> {
                if (h.getStackInSlot(0) != ItemStack.EMPTY) {
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                }
            });
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape shape;
        switch (state.getValue(FACING)) {

            case NORTH:
                shape = Stream.of(
                        Block.box(0, 0, 0, 15.75, 0.5, 0.25),
                        Block.box(0.25, 0, 15.75, 15.75, 0.5, 16),
                        Block.box(0, 0, 0.25, 0.25, 0.5, 16),
                        Block.box(15.75, 0, 0, 16, 0.5, 16),
                        Block.box(0, 15.75, 0, 15.75, 16.25, 0.25),
                        Block.box(0.25, 15.75, 15.75, 15.75, 16.25, 16),
                        Block.box(15.75, 15.75, 0, 16, 16.25, 16),
                        Block.box(0, 15.75, 0.25, 0.25, 16.25, 16),
                        Block.box(0.25, 0.5, 15.75, 15.75, 15.75, 16),
                        Block.box(15.75, 0.5, 8, 16, 15.75, 16),
                        Block.box(0, 0.5, 8.25, 0.25, 15.75, 16),
                        Block.box(0, 10.5, 0.25, 0.25, 15.75, 8.25),
                        Block.box(15.75, 10.5, 0, 16, 15.75, 8),
                        Block.box(0, 13.25, 0, 15.75, 15.75, 0.25),
                        Block.box(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                        Block.box(0.25, 0, 0.25, 15.75, 0.25, 15.75)
                ).reduce((v1, v2) -> {
                    return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
                }).get();
                break;
            case SOUTH:
                shape = Stream.of(
                        Block.box(0.25, 0, 15.75, 16, 0.5, 16),
                        Block.box(0.25, 0, 0, 15.75, 0.5, 0.25),
                        Block.box(15.75, 0, 0, 16, 0.5, 15.75),
                        Block.box(0, 0, 0, 0.25, 0.5, 16),
                        Block.box(0.25, 15.75, 15.75, 16, 16.25, 16),
                        Block.box(0.25, 15.75, 0, 15.75, 16.25, 0.25),
                        Block.box(0, 15.75, 0, 0.25, 16.25, 16),
                        Block.box(15.75, 15.75, 0, 16, 16.25, 15.75),
                        Block.box(0.25, 0.5, 0, 15.75, 15.75, 0.25),
                        Block.box(0, 0.5, 0, 0.25, 15.75, 8),
                        Block.box(15.75, 0.5, 0, 16, 15.75, 7.75),
                        Block.box(15.75, 10.5, 7.75, 16, 15.75, 15.75),
                        Block.box(0, 10.5, 8, 0.25, 15.75, 16),
                        Block.box(0.25, 13.25, 15.75, 16, 15.75, 16),
                        Block.box(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                        Block.box(0.25, 0, 0.25, 15.75, 0.25, 15.75)
                ).reduce((v1, v2) -> {
                    return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
                }).get();
                break;

            case EAST:
                shape = Stream.of(
                        Block.box(15.75, 0, 0, 16, 0.5, 15.75),
                        Block.box(0, 0, 0.25, 0.25, 0.5, 15.75),
                        Block.box(0, 0, 0, 15.75, 0.5, 0.25),
                        Block.box(0, 0, 15.75, 16, 0.5, 16),
                        Block.box(15.75, 15.75, 0, 16, 16.25, 15.75),
                        Block.box(0, 15.75, 0.25, 0.25, 16.25, 15.75),
                        Block.box(0, 15.75, 15.75, 16, 16.25, 16),
                        Block.box(0, 15.75, 0, 15.75, 16.25, 0.25),
                        Block.box(0, 0.5, 0.25, 0.25, 15.75, 15.75),
                        Block.box(0, 0.5, 15.75, 8, 15.75, 16),
                        Block.box(0, 0.5, 0, 7.75, 15.75, 0.25),
                        Block.box(7.75, 10.5, 0, 15.75, 15.75, 0.25),
                        Block.box(8, 10.5, 15.75, 16, 15.75, 16),
                        Block.box(15.75, 13.25, 0, 16, 15.75, 15.75),
                        Block.box(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                        Block.box(0.25, 0, 0.25, 15.75, 0.25, 15.75)
                ).reduce((v1, v2) -> {
                    return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
                }).get();
                break;

            case WEST:
                shape = Stream.of(
                        Block.box(0, 0, 0.25, 0.25, 0.5, 16),
                        Block.box(15.75, 0, 0.25, 16, 0.5, 15.75),
                        Block.box(0.25, 0, 15.75, 16, 0.5, 16),
                        Block.box(0, 0, 0, 16, 0.5, 0.25),
                        Block.box(0, 15.75, 0.25, 0.25, 16.25, 16),
                        Block.box(15.75, 15.75, 0.25, 16, 16.25, 15.75),
                        Block.box(0, 15.75, 0, 16, 16.25, 0.25),
                        Block.box(0.25, 15.75, 15.75, 16, 16.25, 16),
                        Block.box(15.75, 0.5, 0.25, 16, 15.75, 15.75),
                        Block.box(8, 0.5, 0, 16, 15.75, 0.25),
                        Block.box(8.25, 0.5, 15.75, 16, 15.75, 16),
                        Block.box(0.25, 10.5, 15.75, 8.25, 15.75, 16),
                        Block.box(0, 10.5, 0, 8, 15.75, 0.25),
                        Block.box(0, 13.25, 0.25, 0.25, 15.75, 16),
                        Block.box(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                        Block.box(0.25, 0, 0.25, 15.75, 0.25, 15.75)
                ).reduce((v1, v2) -> {
                    return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
                }).get();
                break;

            default:
                shape = Stream.of(
                        Block.box(0, 0, 0, 15.75, 0.5, 0.25),
                        Block.box(0.25, 0, 15.75, 15.75, 0.5, 16),
                        Block.box(0, 0, 0.25, 0.25, 0.5, 16),
                        Block.box(15.75, 0, 0, 16, 0.5, 16),
                        Block.box(0, 15.75, 0, 15.75, 16.25, 0.25),
                        Block.box(0.25, 15.75, 15.75, 15.75, 16.25, 16),
                        Block.box(15.75, 15.75, 0, 16, 16.25, 16),
                        Block.box(0, 15.75, 0.25, 0.25, 16.25, 16),
                        Block.box(0.25, 0.5, 15.75, 15.75, 15.75, 16),
                        Block.box(15.75, 0.5, 8, 16, 15.75, 16),
                        Block.box(0, 0.5, 8.25, 0.25, 15.75, 16),
                        Block.box(0, 10.5, 0.25, 0.25, 15.75, 8.25),
                        Block.box(15.75, 10.5, 0, 16, 15.75, 8),
                        Block.box(0, 13.25, 0, 15.75, 15.75, 0.25),
                        Block.box(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                        Block.box(0.25, 0, 0.25, 15.75, 0.25, 15.75)
                ).reduce((v1, v2) -> {
                    return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
                }).get();
        }

        return shape;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                             Hand hand, BlockRayTraceResult p_225533_6_) {

            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BeyCreatorTileEntity) {
                if (!playerIn.isCrouching()) {
                    if (!worldIn.isClientSide) {
                        ((BeyCreatorTileEntity) tileentity).getInventory().ifPresent(h -> {
                            if (h.getStackInSlot(0) != ItemStack.EMPTY) {
                                worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                                h.extractItem(0, 1, false);
                            }
                            if (playerIn.getItemInHand(hand).getItem() == BeyRegistry.PLASTIC || playerIn.getItemInHand(hand).getItem() == Items.IRON_INGOT) {
                                ItemStack newStack = playerIn.getItemInHand(hand).copy();
                                newStack.setCount(1);
                                h.insertItem(0, newStack, false);
                                playerIn.getItemInHand(hand).shrink(1);
                            }
                            worldIn.sendBlockUpdated(pos, state, state, 0);
                        });
                    }
                } else {
                    if (worldIn.isClientSide) {
                        Minecraft.getInstance().setScreen(new BeyCreatorGUI(new StringTextComponent("")));
                    }
                }
            }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BeyCreatorTileEntity();
    }
}
