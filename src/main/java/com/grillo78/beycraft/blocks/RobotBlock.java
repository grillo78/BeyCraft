package com.grillo78.beycraft.blocks;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.items.*;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import com.grillo78.beycraft.tileentity.ExpositoryTileEntity;
import com.grillo78.beycraft.tileentity.RobotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class RobotBlock extends Block {

	public static final EnumProperty<RobotBlock.EnumPartType> PART = EnumProperty.<RobotBlock.EnumPartType>create(
			"part", RobotBlock.EnumPartType.class);
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public RobotBlock(Material materialIn, String name) {
		super(Block.Properties.create(materialIn).hardnessAndResistance(0.6F).notSolid());
		setDefaultState(this.stateContainer.getBaseState().with(PART, EnumPartType.BOTTOM));
		setRegistryName(new ResourceLocation(Reference.MODID, name));

		BeyRegistry.BLOCKS.add(this);
		BeyRegistry.ITEMS.put(name, new BlockItem(this, new Item.Properties().group(BeyCraft.BEYCRAFTTAB))
				.setRegistryName(this.getRegistryName()));
	}

	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
			Hand hand, BlockRayTraceResult hit) {
		TileEntity tileentity;
		if (state.get(PART).equals(EnumPartType.TOP)) {
			tileentity = worldIn.getTileEntity(pos);
		} else {
			tileentity = worldIn.getTileEntity(pos.up());
		}
		if (!worldIn.isRemote) {
			if (tileentity instanceof RobotTileEntity) {
				((RobotTileEntity) tileentity).getInventory().ifPresent(h -> {
					if (h.getStackInSlot(0).getItem() instanceof ItemBeyLayer) {
						EntityBey entity;
						switch (state.get(FACING)) {
						case NORTH:
							entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, worldIn, h.getStackInSlot(0).copy(),
									(int) ((ItemBeyLayer) h.getStackInSlot(0).getItem()).getRotationDirection(),
									playerIn);
							entity.setLocationAndAngles(tileentity.getPos().getX() + 0.5,
									tileentity.getPos().getY() + 0.025, tileentity.getPos().getZ() - 0.2, 180, 0);
							entity.rotationYawHead = entity.rotationYaw;
							entity.renderYawOffset = entity.rotationYaw;
							worldIn.addEntity(entity);
							h.getStackInSlot(0).shrink(1);
							break;
						case SOUTH:
							entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, worldIn, h.getStackInSlot(0).copy(),
									(int) ((ItemBeyLayer) h.getStackInSlot(0).getItem()).getRotationDirection(),
									playerIn);
							entity.setLocationAndAngles(tileentity.getPos().getX() + 0.5,
									tileentity.getPos().getY() + 0.025, tileentity.getPos().getZ() + 0.2 + 1, 0, 0);
							entity.rotationYawHead = entity.rotationYaw;
							entity.renderYawOffset = entity.rotationYaw;
							worldIn.addEntity(entity);
							h.getStackInSlot(0).shrink(1);
							break;
						case EAST:
							entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, worldIn, h.getStackInSlot(0).copy(),
									(int) ((ItemBeyLayer) h.getStackInSlot(0).getItem()).getRotationDirection(),
									playerIn);
							entity.setLocationAndAngles(tileentity.getPos().getX() + 0.2 + 1,
									tileentity.getPos().getY() + 0.025, tileentity.getPos().getZ() + 0.5, -90, 0);
							entity.rotationYawHead = entity.rotationYaw;
							entity.renderYawOffset = entity.rotationYaw;
							worldIn.addEntity(entity);
							h.getStackInSlot(0).shrink(1);
							break;
						case WEST:
							entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, worldIn, h.getStackInSlot(0).copy(),
									(int) ((ItemBeyLayer) h.getStackInSlot(0).getItem()).getRotationDirection(),
									playerIn);
							entity.setLocationAndAngles(tileentity.getPos().getX() - 0.2,
									tileentity.getPos().getY() + 0.025, tileentity.getPos().getZ() + 0.5, 90, 0);
							entity.rotationYawHead = entity.rotationYaw;
							entity.renderYawOffset = entity.rotationYaw;
							worldIn.addEntity(entity);
							h.getStackInSlot(0).shrink(1);
							break;
						}
					}
					if (playerIn.getHeldItem(hand).getItem() instanceof ItemBeyLayer) {
						playerIn.getHeldItem(hand).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
								.ifPresent(i -> {
									switch (i.getSlots()) {
									case 2:
										if (i.getStackInSlot(0).getItem() instanceof ItemBeyDisc
												&& i.getStackInSlot(1).getItem() instanceof ItemBeyDriver) {
											h.insertItem(0, playerIn.getHeldItem(hand).copy(), false);
											playerIn.getHeldItem(hand).shrink(1);
										}
										break;
									case 4:
										if (i.getStackInSlot(0).getItem() instanceof ItemBeyDisc
												&& i.getStackInSlot(1).getItem() instanceof ItemBeyDriver
												&& i.getStackInSlot(2).getItem() instanceof ItemBeyGTChip
												&& i.getStackInSlot(3).getItem() instanceof ItemBeyGTWeight) {
											h.insertItem(0, playerIn.getHeldItem(hand).copy(), false);
											playerIn.getHeldItem(hand).shrink(1);
										}
										break;
									}
								});
					}
				});
				if (state.get(PART).equals(EnumPartType.TOP)) {
					worldIn.notifyBlockUpdate(pos, state, worldIn.getBlockState(pos), 0);
				} else {
					worldIn.notifyBlockUpdate(pos.up(), state, worldIn.getBlockState(pos.up()), 0);
				}
			}
		}
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemLauncher) {
			return ActionResultType.PASS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		if (worldIn.getTileEntity(pos) instanceof RobotTileEntity) {
			RobotTileEntity tileEntity = (RobotTileEntity) worldIn.getTileEntity(pos);
			tileEntity.getInventory().ifPresent(h -> {
				worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(0)));
			});
		}
		worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this)));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos.up(), getDefaultState().with(PART, EnumPartType.TOP).with(FACING, state.get(FACING)));
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		super.onReplaced(state, worldIn, pos, newState, isMoving);
		if (state.get(PART).equals(EnumPartType.BOTTOM)) {
			worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
		} else {
			worldIn.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (state.get(PART).equals(EnumPartType.TOP)) {
			return new RobotTileEntity();
		}
		return super.createTileEntity(state, world);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return canReplace(worldIn, pos) && canReplace(worldIn, pos.up());
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
		public String getString() {
			return this.NAME;
		}
	}
}
