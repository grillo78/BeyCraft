package com.grillo78.beycraft.blocks;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeyContainer;
import com.grillo78.beycraft.inventory.BeyCreatorContainer;
import com.grillo78.beycraft.items.ItemBeyDisc;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.grillo78.beycraft.items.ItemBeyLayer;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
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
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.stream.Stream;


public class BeyCreatorBlock extends Block {

    private final VoxelShape shape;

    public BeyCreatorBlock(Material materialIn, String name) {
        super(Block.Properties.create(materialIn).hardnessAndResistance(0.6F).harvestTool(ToolType.PICKAXE));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        shape = Stream.of(
                Block.makeCuboidShape(0, 0, 0, 15.75, 0.5, 0.25),
                Block.makeCuboidShape(0.25, 0, 15.75, 15.75, 0.5, 16),
                Block.makeCuboidShape(0, 0, 0.25, 0.25, 0.5, 16),
                Block.makeCuboidShape(15.75, 0, 0, 16, 0.5, 16),
                Block.makeCuboidShape(0, 15.75, 0, 15.75, 16.25, 0.25),
                Block.makeCuboidShape(0.25, 15.75, 15.75, 15.75, 16.25, 16),
                Block.makeCuboidShape(15.75, 15.75, 0, 16, 16.25, 16),
                Block.makeCuboidShape(0, 15.75, 0.25, 0.25, 16.25, 16),
                Block.makeCuboidShape(0.25, 0.5, 15.75, 15.75, 15.75, 16),
                Block.makeCuboidShape(15.75, 0.5, 8, 16, 15.75, 16),
                Block.makeCuboidShape(0, 0.5, 8.25, 0.25, 15.75, 16),
                Block.makeCuboidShape(0, 10.5, 0.25, 0.25, 15.75, 8.25),
                Block.makeCuboidShape(15.75, 10.5, 0, 16, 15.75, 8),
                Block.makeCuboidShape(0, 13.25, 0, 15.75, 15.75, 0.25),
                Block.makeCuboidShape(0.25, 16, 0.25, 15.75, 16.25, 15.75),
                Block.makeCuboidShape(0.25, 0, 0.25, 15.75, 0.25, 15.75)
        ).reduce((v1, v2) -> {
            return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
        }).get();
        BeyRegistry.BLOCKS.add(this);
        BeyRegistry.ITEMS.put(name, new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB))
                .setRegistryName(this.getRegistryName()));
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
        world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof BeyCreatorTileEntity) {
            ((BeyCreatorTileEntity) tileentity).getInventory().ifPresent(h -> {
                if (h.getStackInSlot(0) != ItemStack.EMPTY) {
                    world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                }
            });
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return shape;
//        return super.getShape(p_220053_1_,p_220053_2_,p_220053_3_,p_220053_4_);
    }


    /**
     *
     */
    private void setItemStack(BeyCreatorTileEntity tileEntity, int index, ItemStack stack) {
        tileEntity.getInventory().ifPresent(h -> {
            if (stack != ItemStack.EMPTY) {
                h.insertItem(index, stack.copy(), false);
                stack.shrink(1);
            } else {

            }
        });
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                             Hand hand, BlockRayTraceResult p_225533_6_) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof BeyCreatorTileEntity) {
            if(!playerIn.isCrouching()) {
                ((BeyCreatorTileEntity) tileentity).getInventory().ifPresent(h -> {
                    if (h.getStackInSlot(0) != ItemStack.EMPTY) {
                        worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0).copy()));
                        h.extractItem(0, 1, false);
                    }
                    if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer) {
                        playerIn.getHeldItem(hand).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(c -> {
                            if (c.getStackInSlot(1).isEmpty() && c.getStackInSlot(0).isEmpty()) {
                                h.insertItem(0, playerIn.getHeldItem(hand).copy(), false);
                                playerIn.getHeldItem(hand).shrink(1);
                            }
                        });
                    } else {
                        if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDisc || playerIn.getHeldItem(hand).getItem() instanceof ItemBeyDriver) {
                            h.insertItem(0, playerIn.getHeldItem(hand).copy(), false);
                            playerIn.getHeldItem(hand).shrink(1);
                        }
                    }
                });
            } else {
                if (!worldIn.isRemote) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    NetworkHooks.openGui((ServerPlayerEntity) playerIn,
                            new SimpleNamedContainerProvider(
                                    (id, playerInventory, playerEntity) -> new BeyCreatorContainer(BeyRegistry.BEY_CREATOR_CONTAINER,id,(BeyCreatorTileEntity) tileentity),
                                    new StringTextComponent(getTranslationKey())));
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
