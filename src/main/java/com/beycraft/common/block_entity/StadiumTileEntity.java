package com.beycraft.common.block_entity;

import com.beycraft.common.entity.BeybladeEntity;
import com.beycraft.common.ranking.Battle;
import com.beycraft.utils.CommonUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StadiumTileEntity extends TileEntity implements ITickableTileEntity {

    private Battle battle;
    private boolean allowBattle = true;
    private int pointsToWin = 3;

    public StadiumTileEntity() {
        super(ModTileEntities.STADIUM);
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            List<BeybladeEntity> beys = level.getEntitiesOfClass(BeybladeEntity.class,
                    new AxisAlignedBB(getBlockPos().north().east(2), getBlockPos().south(2).west().above()));
            if (allowBattle) {
                int beysSpinning = 0;
                for (BeybladeEntity bey : beys) {
                    if (!bey.isStopped()) {
                        beysSpinning++;
                    }
                }
                List<PlayerEntity> bladers = new ArrayList<>();
                boolean isValidBattle = true;
                for (BeybladeEntity bey : beys) {
                    PlayerEntity player = CommonUtils.PlayerUtils.getPlayerByName(bey.getPlayerName(), (ServerWorld) level);
                    if (player != null) {
                        if (!bladers.contains(player)) {
                            bladers.add(player);
                        } else {
                            isValidBattle = false;
                        }
                    } else {
                        isValidBattle = false;
                    }
                }
                if (isValidBattle) {
                    if (battle == null && beysSpinning > 1 && bladers.size() == beysSpinning) {
                        AtomicBoolean battleForRanking = new AtomicBoolean(false);
                        BlockPos.betweenClosedStream(getBlockPos().above().north(2).east(2),getBlockPos().above().south(2).west(2)).forEach(blockPos -> {
                            if(level.getBlockEntity(blockPos) instanceof BattleInformerTileEntity){
                                battleForRanking.set(true);
                            }
                        });
                        battle = new Battle((ServerWorld) level, this, battleForRanking.get());
                    }
                } else {
                    battle = null;
                }
                if (battle != null && allowBattle && beysSpinning >= 1) {
                    battle.tick(beys);
                }
            } else {
                allowBattle = beys.size() == 0;
            }
        }
    }

    public int getPointsToWin() {
        return pointsToWin;
    }
    public void updatePoints(BattleInformerTileEntity battleInformer){
        battleInformer.setPoints(battle != null? battle.getPoints() : new HashMap<>());
        level.sendBlockUpdated(battleInformer.getBlockPos(),battleInformer.getBlockState(),battleInformer.getBlockState(),0);
    }

    public boolean isAllowBattle() {
        return allowBattle;
    }

    public void setAllowBattle(boolean allowBattle) {
        this.allowBattle = allowBattle;
    }

    public void invalidateBattle() {
        battle = null;
    }
}
