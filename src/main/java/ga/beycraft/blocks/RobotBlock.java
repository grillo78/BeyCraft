package ga.beycraft.blocks;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.Reference;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.items.*;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageOpenRobotGUI;
import ga.beycraft.tileentity.RobotTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class RobotBlock extends Block {

	public static final EnumProperty<RobotBlock.EnumPartType> PART = EnumProperty.<RobotBlock.EnumPartType>create(
			"part", RobotBlock.EnumPartType.class);
	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public RobotBlock(Material materialIn, String name) {
		super(AbstractBlock.Properties.of(materialIn).strength(0.6F).noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(PART, EnumPartType.BOTTOM));
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));

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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
			Hand hand, BlockRayTraceResult hit) {
		TileEntity tileentity;
		if (state.getValue(PART).equals(EnumPartType.TOP)) {
			tileentity = worldIn.getBlockEntity(pos);
		} else {
			tileentity = worldIn.getBlockEntity(pos.above());
		}
		if (!worldIn.isClientSide) {
			if (tileentity instanceof RobotTileEntity) {
				if (!playerIn.isCrouching()) {
					((RobotTileEntity) tileentity).getInventory().ifPresent(h -> {
						if (h.getStackInSlot(0).getItem() instanceof ItemBeyLayer) {
							EntityBey entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, worldIn,
									h.getStackInSlot(0).copy(),
									(int) ((ItemBeyLayer) h.getStackInSlot(0).getItem()).getRotationDirection(),
									"Training Robot", ((RobotTileEntity) tileentity).getBladerLevel(), false);
							switch (state.getValue(FACING)) {
							case NORTH:
								entity.moveTo(tileentity.getBlockPos().getX() + 0.5,
										tileentity.getBlockPos().getY() + 0.025, tileentity.getBlockPos().getZ() - 0.2, 180, 0);
								break;
							case SOUTH:
								entity.moveTo(tileentity.getBlockPos().getX() + 0.5,
										tileentity.getBlockPos().getY() + 0.025, tileentity.getBlockPos().getZ() + 0.2 + 1, 0, 0);
								break;
							case EAST:
								entity.moveTo(tileentity.getBlockPos().getX() + 0.2 + 1,
										tileentity.getBlockPos().getY() + 0.025, tileentity.getBlockPos().getZ() + 0.5, -90, 0);
								break;
							case WEST:
								entity.moveTo(tileentity.getBlockPos().getX() - 0.2,
										tileentity.getBlockPos().getY() + 0.025, tileentity.getBlockPos().getZ() + 0.5, 90, 0);
								break;
							}

							entity.yHeadRot = entity.yRot;
							entity.yBodyRot = entity.yRot;
							worldIn.addFreshEntity(entity);
							h.getStackInSlot(0).shrink(1);
						}
						if (playerIn.getItemInHand(hand).getItem() instanceof ItemBeyLayer) {
							playerIn.getItemInHand(hand).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
									.ifPresent(i -> {
										switch (i.getSlots()) {
										case 2:
											if (i.getStackInSlot(0).getItem() instanceof ItemBeyDisc
													&& i.getStackInSlot(1).getItem() instanceof ItemBeyDriver) {
												h.insertItem(0, playerIn.getItemInHand(hand).copy(), false);
												playerIn.getItemInHand(hand).shrink(1);
											}
											break;
										case 4:
											if (i.getStackInSlot(0).getItem() instanceof ItemBeyDisc
													&& i.getStackInSlot(1).getItem() instanceof ItemBeyDriver
													&& i.getStackInSlot(2).getItem() instanceof ItemBeyGTChip) {
												h.insertItem(0, playerIn.getItemInHand(hand).copy(), false);
												playerIn.getItemInHand(hand).shrink(1);
											}
											break;
										}
									});
						}
					});
				} else {
					PacketHandler.instance.sendTo(new MessageOpenRobotGUI(true),
							((ServerPlayerEntity) playerIn).connection.getConnection(),
							NetworkDirection.PLAY_TO_CLIENT);
				}
				if (state.getValue(PART).equals(EnumPartType.TOP)) {
					worldIn.sendBlockUpdated(pos, state, worldIn.getBlockState(pos), 0);
				} else {
					worldIn.sendBlockUpdated(pos.above(), state, worldIn.getBlockState(pos.above()), 0);
				}
			}
		}
		if (playerIn.getItemInHand(hand).getItem() instanceof ItemLauncher) {
			return ActionResultType.PASS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.playerWillDestroy(worldIn, pos, state, player);
		if (worldIn.getBlockEntity(pos) instanceof RobotTileEntity) {
			RobotTileEntity tileEntity = (RobotTileEntity) worldIn.getBlockEntity(pos);
			tileEntity.getInventory().ifPresent(h -> {
				worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0)));
			});
		}
		worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockAndUpdate(pos.above(), defaultBlockState().setValue(PART, EnumPartType.TOP).setValue(FACING, state.getValue(FACING)));
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		super.onRemove(state, worldIn, pos, newState, isMoving);
		if (state.getValue(PART).equals(EnumPartType.BOTTOM)) {
			worldIn.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
		} else {
			worldIn.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (state.getValue(PART).equals(EnumPartType.TOP)) {
			return new RobotTileEntity();
		}
		return super.createTileEntity(state, world);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canReplace(worldIn, pos) && canReplace(worldIn, pos.above());
	}

	private boolean canReplace(IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.WATER
				|| world.getBlockState(pos).getBlock() == Blocks.LAVA;
	}

	public static enum EnumPartType implements IStringSerializable {
		TOP("top", 0), BOTTOM("bottom", 1);

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
