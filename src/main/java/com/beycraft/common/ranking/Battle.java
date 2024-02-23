package com.beycraft.common.ranking;

import com.beycraft.common.block_entity.BattleInformerTileEntity;
import com.beycraft.common.block_entity.StadiumTileEntity;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.beycraft.common.entity.BeybladeEntity;
import com.beycraft.network.PacketHandler;
import com.beycraft.utils.CommonUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Battle {
    private HashMap<PlayerEntity, Integer> points = new HashMap<>();
    private HashMap<PlayerEntity, BeybladeEntity> beys = new HashMap<>();
    private int startCount = 0;
    private int round = 1;
    private ServerWorld level;
    private BattleTypes type = BattleTypes.NORMAL_MATCH;
    private StadiumTileEntity stadium;
    private boolean battleForRanking;

    public Battle(ServerWorld level, StadiumTileEntity stadium, boolean battleForRanking) {
        this.level = level;
        this.stadium = stadium;
        this.battleForRanking = battleForRanking;
    }

    public HashMap<PlayerEntity, Integer> getPoints() {
        return points;
    }

    public void tick(List<BeybladeEntity> beysIn) {
        if (startCount < 60) {
            for (BeybladeEntity bey : beysIn) {
                PlayerEntity player = CommonUtils.PlayerUtils.getPlayerByName(bey.getPlayerName(), level);
                if (player != null) {
                    if (round == 1) {
                        if (!points.containsKey(player)) {
                            points.put(player, 0);
                            this.beys.put(player, bey);
                        }
                    } else {
                        if (points.containsKey(player)) {
                            this.beys.put(player, bey);
                        } else {
                            stadium.invalidateBattle();
                        }
                    }
                }
            }
            updateBattleInformersPoints();
            if (points.size() > 2) {
                type = BattleTypes.BATTLE_ROYAL;
            }
            startCount++;
        } else {
            if (beysIn.size() > points.size()) {
                stadium.invalidateBattle();
            }
        }

        AtomicInteger beysSpinning = new AtomicInteger(0);
        AtomicReference<PlayerEntity> lastSpinningBeyPlayer = new AtomicReference<>();
        this.beys.forEach(((entity, beybladeEntity) -> {
            boolean isAlive = beybladeEntity.isAlive();
            boolean isStopped = beybladeEntity.isStopped();
            if (!isStopped && isAlive && beysIn.contains(beybladeEntity)) {
                if (beybladeEntity.isDropped())
                    cancelRound();
                beysSpinning.getAndIncrement();
                lastSpinningBeyPlayer.set(entity);
            }
        }));

        if (beysSpinning.get() == 1 && this.beys.size() > 1 && stadium.isAllowBattle()) {
            switch (type) {
                case NORMAL_MATCH:
                    PlayerEntity winner = lastSpinningBeyPlayer.get();
                    if (winner != null && points.containsKey(winner)) {
                        AtomicInteger beysAlive = new AtomicInteger();
                        beys.forEach(((entity, BeybladeEntity) -> {
                            increaseXP((ServerPlayerEntity) entity, entity == winner);
                            if (BeybladeEntity.isAlive())
                                beysAlive.getAndIncrement();
                        }));
                        points.put(winner, points.get(winner) + (beysAlive.get() == 1 ? 2 : 1));
                        if (points.get(winner) >= stadium.getPointsToWin())
                            setWinner((ServerPlayerEntity) winner);
                    }
                    updateBattleInformersPoints();
                    round++;
                    stadium.setAllowBattle(false);
                    startCount = 0;
                    this.beys.clear();
                    break;
                case BATTLE_ROYAL:
                    setWinner((ServerPlayerEntity) lastSpinningBeyPlayer.get());
                    break;
            }
        } else {
            if (beysSpinning.get() == 0 && beysIn.size() != 0) {
                cancelRound();
            }
        }
    }

    private void cancelRound() {
        startCount = 0;
        stadium.setAllowBattle(false);
        this.beys.clear();
    }

    private void updateBattleInformersPoints() {
        BlockPos.betweenClosedStream(stadium.getBlockPos().above().north(2).east(2), stadium.getBlockPos().above().south(2).west(2)).forEach(blockPos -> {
            if (level.getBlockEntity(blockPos) instanceof BattleInformerTileEntity) {
                stadium.updatePoints((BattleInformerTileEntity) level.getBlockEntity(blockPos));
            }
        });
    }

    private void increaseXP(ServerPlayerEntity player, boolean winning){
        player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
            Random rand = new Random();
            AtomicInteger averageLevel = new AtomicInteger();
            beys.forEach((auxPlayer, bey) -> {
                if (auxPlayer != player)
                    auxPlayer.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(bladerLevel -> {
                        averageLevel.set(averageLevel.get() + bladerLevel.getBladerLevel().getLevel());
                    });
            });
            averageLevel.set(averageLevel.get()/beys.size());
            h.getBladerLevel().increaseExperience(round(
                    (averageLevel.get() * 0.025F)/(winning?1:25), 2));
            h.syncToAll();
        });
    }

    private void setWinner(ServerPlayerEntity player) {
        if(battleForRanking){
            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
                h.getWallet().increaseCurrency(points.get(player));
            });

            //TO-DO notify win combat
            points.forEach((auxPlayer, points) -> {
                if (auxPlayer != player) {
                    //TO-DO notify lose combat

                    auxPlayer.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
                        h.getWallet().increaseCurrency(this.points.get(auxPlayer));
                    });
                }
            });
        }
        stadium.invalidateBattle();
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public enum BattleTypes {
        NORMAL_MATCH, BATTLE_ROYAL
    }
}
