package ga.beycraft.blocks;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.tileentity.StadiumTileEntity;
import ga.beycraft.util.VoxelShapesUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
		super(AbstractBlock.Properties.of(materialIn));
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		setVoxelShapes();
		BeyCraftRegistry.BLOCKS.add(this);
		BeyCraftRegistry.ITEMS.put(name, new BlockItem(this, new Item.Properties().tab(BeyCraft.BEYCRAFTTAB))
				.setRegistryName(getRegistryName()));
		registerDefaultState(this.stateDefinition.any().setValue(PART, EnumPartType.MIDDLECENTER));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape shape = VoxelShapes.block();
		switch (state.getValue(PART).ID) {
		case 0:
			if(voxelShape1 != null){
				shape = voxelShape1;
			}
			break;
		case 1:
			if(voxelShape2 != null){
				shape = voxelShape2;
			}
			break;
		case 2:
			if(voxelShape1 != null){
				shape = voxelShape3;
			}
			break;
		case 3:
			if(voxelShape4 != null){
				shape = voxelShape4;
			}
			break;
		case 4:
			if(voxelShape5 != null){
				shape = voxelShape5;
			}
			break;
		case 5:
			if(voxelShape6 != null){
				shape = voxelShape6;
			}
			break;
		case 6:
			if(voxelShape7 != null){
				shape = voxelShape7;
			}
			break;
		case 7:
			if(voxelShape8 != null){
				shape = voxelShape8;
			}
			break;
		case 8:
			if(voxelShape9 != null){
				shape = voxelShape9;
			}
			break;
		default:
			return VoxelShapes.block();
		}
		return shape;
	}

	@Override
	public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
//		setVoxelShapes();
		return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canReplace(worldIn, pos.north().west()) && canReplace(worldIn, pos.north())
				&& canReplace(worldIn, pos.north().east()) && canReplace(worldIn, pos.west())
				&& canReplace(worldIn, pos) && canReplace(worldIn, pos.east())
				&& canReplace(worldIn, pos.south().west()) && canReplace(worldIn, pos.south())
				&& canReplace(worldIn, pos.south().east());
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.playerWillDestroy(worldIn, pos, state, player);
		worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (worldIn.getBlockState(pos.east().north()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.east().north()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.east().north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[0]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.east().north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[0]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.east()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.east()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.east(),
					defaultBlockState().setValue(PART, EnumPartType.values()[1]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.east(),
					defaultBlockState().setValue(PART, EnumPartType.values()[1]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.east().south()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.east().south()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.east().south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[2]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.east().south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[2]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.north()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.north()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[3]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[3]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.south()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.south()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[5]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[5]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.west().north()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.west().north()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.west().north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[6]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.west().north(),
					defaultBlockState().setValue(PART, EnumPartType.values()[6]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.west()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.west()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.west(),
					defaultBlockState().setValue(PART, EnumPartType.values()[7]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.west(),
					defaultBlockState().setValue(PART, EnumPartType.values()[7]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
		if (worldIn.getBlockState(pos.west().south()).getBlock() == Blocks.WATER
				&& worldIn.getBlockState(pos.west().south()).getFluidState().isSource()) {
			worldIn.setBlockAndUpdate(pos.west().south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[8]).setValue(WATERLOGGED, Boolean.valueOf(true)));
		} else {
			worldIn.setBlockAndUpdate(pos.west().south(),
					defaultBlockState().setValue(PART, EnumPartType.values()[8]).setValue(WATERLOGGED, Boolean.valueOf(false)));
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return defaultBlockState().setValue(WATERLOGGED,
				Boolean.valueOf(context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PART);
		builder.add(WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	private void breackBlock(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos).getBlock() == this) {
			if (worldIn.getBlockState(pos).getFluidState().isEmpty()) {
				worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			} else {
				worldIn.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			}
		}
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (newState.getBlock() != this) {
			switch (state.getValue(PART).getSerializedName()) {
			case "topleft":
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.west());
				break;
			case "topcenter":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.west());
				break;
			case "topright":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.west());
				break;
			case "middleleft":
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.west());
				breackBlock(worldIn, pos.east());
				break;
			case "middlecenter":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.east());
				breackBlock(worldIn, pos.west());
				break;
			case "middleright":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.east());
				breackBlock(worldIn, pos.west());
				break;
			case "bottomleft":
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.east());
				break;
			case "bottomcenter":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.south());
				breackBlock(worldIn, pos.east());
				break;
			case "bottomright":
				breackBlock(worldIn, pos.north());
				breackBlock(worldIn, pos.east());
				break;
			}
		}
	}

	private void setVoxelShapes() {
		BeyCraft.logger.info("Starting to processing Stadium collisions");
		VoxelShapesUtil.init();
		voxelShape1 = VoxelShapesUtil.getStadiumShape(-1,1);
		voxelShape2 = VoxelShapesUtil.getStadiumShape(-1,0);
		voxelShape3 = VoxelShapesUtil.getStadiumShape(-1,-1);
		voxelShape4 = VoxelShapesUtil.getStadiumShape(0,1);
		voxelShape5 = VoxelShapesUtil.getStadiumShape(0,0);
		voxelShape6 = VoxelShapesUtil.getStadiumShape(0,-1);
		voxelShape7 = VoxelShapesUtil.getStadiumShape(1,1);
		voxelShape8 = VoxelShapesUtil.getStadiumShape(1,0);
		voxelShape9 = VoxelShapesUtil.getStadiumShape(1,-1);
		BeyCraft.logger.info("Finished processing of Stadium collisions");
	}

	private boolean canReplace(IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.GRASS
				|| world.getBlockState(pos).getBlock() == Blocks.TALL_GRASS
				|| world.getBlockState(pos).getBlock() == Blocks.TALL_SEAGRASS
				|| world.getBlockState(pos).getBlock() == Blocks.SEAGRASS
				|| world.getBlockState(pos).getBlock() == Blocks.WATER
				|| world.getBlockState(pos).getBlock() == Blocks.LAVA;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.getValue(PART) == EnumPartType.MIDDLECENTER;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {

		return new StadiumTileEntity();
	}

	public enum EnumPartType implements IStringSerializable {
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

		public int getID() {
			return ID;
		}

		@Override
		public String getSerializedName() {
			return this.NAME;
		}
	}
}
