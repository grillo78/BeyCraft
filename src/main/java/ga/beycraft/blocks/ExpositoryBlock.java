package ga.beycraft.blocks;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.items.ItemBeyDisc;
import ga.beycraft.items.ItemBeyDriver;
import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.tileentity.ExpositoryTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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

    private VoxelShape collisionBox = VoxelShapes.box(0,0,0,1,0.5,1);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ExpositoryBlock(Material materialIn, String name) {
        super(AbstractBlock.Properties.of(materialIn).strength(0.6F).noOcclusion());
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));

        BeyCraftRegistry.BLOCKS.add(this);
        BeyCraftRegistry.ITEMS.put(name,new BlockItem(this, new Item.Properties().tab(BeyCraft.BEYCRAFTTAB))
                .setRegistryName(this.getRegistryName()));
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
        worldIn.addFreshEntity(new ItemEntity(worldIn,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(this)));
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
                    if (playerIn.getItemInHand(hand).getItem() instanceof ItemBeyLayer || playerIn.getItemInHand(hand).getItem() instanceof ItemBeyDisc || playerIn.getItemInHand(hand).getItem() instanceof ItemBeyDriver) {
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
