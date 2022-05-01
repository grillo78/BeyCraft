package ga.beycraft.common.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ga.beycraft.Beycraft;
import ga.beycraft.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.heroesunited.heroesunited.hupacks.HUPacks;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StadiumBlock extends Block implements IWaterLoggable {

    public static final EnumProperty<EnumPartType> PART = EnumProperty.create(
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

    public StadiumBlock(Properties properties) {
        super(properties);
        setVoxelShapes();
        registerDefaultState(this.stateDefinition.any().setValue(PART, EnumPartType.MIDDLECENTER));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader levelIn, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = VoxelShapes.block();
        switch (state.getValue(PART).ID) {
            case 0:
                if (voxelShape1 != null) {
                    shape = voxelShape1;
                }
                break;
            case 1:
                if (voxelShape2 != null) {
                    shape = voxelShape2;
                }
                break;
            case 2:
                if (voxelShape1 != null) {
                    shape = voxelShape3;
                }
                break;
            case 3:
                if (voxelShape4 != null) {
                    shape = voxelShape4;
                }
                break;
            case 4:
                if (voxelShape5 != null) {
                    shape = voxelShape5;
                }
                break;
            case 5:
                if (voxelShape6 != null) {
                    shape = voxelShape6;
                }
                break;
            case 6:
                if (voxelShape7 != null) {
                    shape = voxelShape7;
                }
                break;
            case 7:
                if (voxelShape8 != null) {
                    shape = voxelShape8;
                }
                break;
            case 8:
                if (voxelShape9 != null) {
                    shape = voxelShape9;
                }
                break;
            default:
                return VoxelShapes.block();
        }
        return shape;
    }

    private boolean shouldUseAccurateCollisions(ISelectionContext context, BlockPos pos) {
        return context instanceof EntitySelectionContext && context.getEntity() != null && context.getEntity().getY() == pos.getY();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos pos, ISelectionContext p_220071_4_) {
        return shouldUseAccurateCollisions(p_220071_4_, pos)? VoxelShapes.block() : super.getCollisionShape(p_220071_1_, p_220071_2_, pos, p_220071_4_);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader levelIn, BlockPos pos) {
        return canReplace(levelIn, pos.north().west()) && canReplace(levelIn, pos.north())
                && canReplace(levelIn, pos.north().east()) && canReplace(levelIn, pos.west())
                && canReplace(levelIn, pos) && canReplace(levelIn, pos.east())
                && canReplace(levelIn, pos.south().west()) && canReplace(levelIn, pos.south())
                && canReplace(levelIn, pos.south().east());
    }

//    @Override
//    public InteractionResult use(BlockState p_225533_1_, Level world, BlockPos posIn, Player player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
//        InteractionResult result = InteractionResult.PASS;
//        if (!world.isClientSide && player.isCrouching()) {
//            BlockPos.betweenClosedStream(posIn.west().north(), posIn.south().east()).forEach((pos) -> {
//                BlockEntity blockEntity = world.getBlockEntity(pos);
//                if (blockEntity != null && blockEntity instanceof StadiumBlockEntity && ((StadiumBlockEntity) blockEntity).getBattle() != null) {
//                    ((StadiumBlockEntity) blockEntity).getBattle().getPoints().forEach((blader, points) -> {
//                        player.sendMessage(blader.getDisplayName(), Util.NIL_UUID);
//                        player.sendMessage(new StringTextComponent("Points: " + points), Util.NIL_UUID);
//                    });
//                }
//            });
//            result = InteractionResult.SUCCESS;
//        }
//        return result;
//    }

    @Override
    public void setPlacedBy(World levelIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (levelIn.getBlockState(pos.east().north()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.east().north()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.east().north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[0]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.east().north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[0]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.east()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.east()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.east(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[1]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.east(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[1]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.east().south()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.east().south()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.east().south(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[2]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.east().south(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[2]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.north()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.north()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[3]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[3]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.south()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.south()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.south(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[5]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.south(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[5]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.west().north()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.west().north()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.west().north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[6]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.west().north(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[6]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.west()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.west()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.west(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[7]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.west(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[7]).setValue(WATERLOGGED, Boolean.valueOf(false)));
        }
        if (levelIn.getBlockState(pos.west().south()).getBlock() == Blocks.WATER
                && levelIn.getBlockState(pos.west().south()).getFluidState().isSource()) {
            levelIn.setBlockAndUpdate(pos.west().south(),
                    defaultBlockState().setValue(PART, EnumPartType.values()[8]).setValue(WATERLOGGED, Boolean.valueOf(true)));
        } else {
            levelIn.setBlockAndUpdate(pos.west().south(),
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

    private void breakBlock(World levelIn, BlockPos pos) {
        if (levelIn.getBlockState(pos).getBlock() == this) {
            if (levelIn.getBlockState(pos).getFluidState().isEmpty()) {
                levelIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            } else {
                levelIn.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }
        }
    }

    @Override
    public void onRemove(BlockState state, World levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state,levelIn,pos,state,isMoving);
        if (newState.getBlock() != this) {
            switch (state.getValue(PART).getSerializedName()) {
                case "topleft":
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.west());
                    break;
                case "topcenter":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.west());
                    break;
                case "topright":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.west());
                    break;
                case "middleleft":
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.west());
                    breakBlock(levelIn, pos.east());
                    break;
                case "middlecenter":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.east());
                    breakBlock(levelIn, pos.west());
                    break;
                case "middleright":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.east());
                    breakBlock(levelIn, pos.west());
                    break;
                case "bottomleft":
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.east());
                    break;
                case "bottomcenter":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.south());
                    breakBlock(levelIn, pos.east());
                    break;
                case "bottomright":
                    breakBlock(levelIn, pos.north());
                    breakBlock(levelIn, pos.east());
                    break;
            }
        }
    }

    private void setVoxelShapes() {
        Beycraft.LOGGER.info("Starting to processing Stadium collisions");
        VoxelShapesUtil.init();
        voxelShape1 = VoxelShapesUtil.getStadiumShape(-1, 1);
        voxelShape2 = VoxelShapesUtil.getStadiumShape(-1, 0);
        voxelShape3 = VoxelShapesUtil.getStadiumShape(-1, -1);
        voxelShape4 = VoxelShapesUtil.getStadiumShape(0, 1);
        voxelShape5 = VoxelShapesUtil.getStadiumShape(0, 0);
        voxelShape6 = VoxelShapesUtil.getStadiumShape(0, -1);
        voxelShape7 = VoxelShapesUtil.getStadiumShape(1, 1);
        voxelShape8 = VoxelShapesUtil.getStadiumShape(1, 0);
        voxelShape9 = VoxelShapesUtil.getStadiumShape(1, -1);
        Beycraft.LOGGER.info("Finished processing of Stadium collisions");
    }

    private boolean canReplace(IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() == Blocks.GRASS
                || world.getBlockState(pos).getBlock() == Blocks.TALL_GRASS
                || world.getBlockState(pos).getBlock() == Blocks.TALL_SEAGRASS
                || world.getBlockState(pos).getBlock() == Blocks.SEAGRASS
                || world.getBlockState(pos).getBlock() == Blocks.WATER
                || world.getBlockState(pos).getBlock() == Blocks.LAVA;
    }

    public static class VoxelShapesUtil {
        private static VoxelShape shape = VoxelShapes.block();

        public static void init(){

            try {
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("hitboxes/stadium_hitbox.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                JsonObject jsonObject = JSONUtils.fromJson(HUPacks.GSON, reader, JsonObject.class);
                JsonArray elements = jsonObject.get("elements").getAsJsonArray();

                for (int i = 0; i < elements.size(); i++) {
                    JsonObject element = elements.get(i).getAsJsonObject();
                    JsonArray from = element.get("from").getAsJsonArray();
                    JsonArray to = element.get("to").getAsJsonArray();
                    if(shape == VoxelShapes.block()) {
                        shape = Block.box(from.get(0).getAsDouble(),from.get(1).getAsDouble(),from.get(2).getAsDouble(),to.get(0).getAsDouble(),to.get(1).getAsDouble(),to.get(2).getAsDouble());
                    } else {
                        shape = VoxelShapes.join(shape, Block.box(from.get(0).getAsDouble(),from.get(1).getAsDouble(),from.get(2).getAsDouble(),to.get(0).getAsDouble(),to.get(1).getAsDouble(),to.get(2).getAsDouble()), IBooleanFunction.OR).optimize();
                    }
                }
                reader.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static VoxelShape getStadiumShape(float offsetX, float offsetZ){
            return shape.move(offsetX,-0.05,offsetZ);
        }
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
