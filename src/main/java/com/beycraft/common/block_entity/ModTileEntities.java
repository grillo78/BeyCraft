package com.beycraft.common.block_entity;

import com.beycraft.Beycraft;
import com.beycraft.common.block.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Beycraft.MOD_ID);

    public static final TileEntityType<BeycreatorTileEntity> BEYCREATOR = register("beycreator",  TileEntityType.Builder.of(BeycreatorTileEntity::new, ModBlocks.BEYCREATOR).build(null));

    public static final TileEntityType<BeycreatorTileEntity> STADIUM = register("stadium",  TileEntityType.Builder.of(BeycreatorTileEntity::new, ModBlocks.STADIUM).build(null));

    public static final TileEntityType<BattleInformerTileEntity> BATTLE_INFORMER = register("battle_informer",  TileEntityType.Builder.of(BattleInformerTileEntity::new, ModBlocks.BATTLE_INFORMER).build(null));
    public static final TileEntityType<ExpositoryTileEntity> EXPOSITORYTILEENTITYTYPE = register("expository",  TileEntityType.Builder.of(ExpositoryTileEntity::new, ModBlocks.EXPOSITORY).build(null));

    private static <T extends TileEntity> TileEntityType<T> register(String name, TileEntityType<T> type){
        TILE_ENTITIES.register(name, ()->type);
        return type;
    }

}
