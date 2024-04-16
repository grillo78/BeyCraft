package grillo78.beycraft.common.ranking;

import grillo78.beycraft.common.block_entity.BattleInformerTileEntity;
import grillo78.beycraft.common.block_entity.StadiumTileEntity;
import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import grillo78.beycraft.common.entity.BeybladeEntity;
import grillo78.beycraft.common.item.ModItems;
import grillo78.beycraft.utils.CommonUtils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
            if (points.size() > 2) {
                type = BattleTypes.BATTLE_ROYAL;
            }
            startCount++;
        } else {
            if (beysIn.size() > points.size()) {
                stadium.invalidateBattle();
            }
        }
        updateBattleInformersPoints();

        AtomicInteger beysSpinning = new AtomicInteger(0);
        AtomicReference<PlayerEntity> lastSpinningBeyPlayer = new AtomicReference<>();
        this.beys.forEach(((entity, beybladeEntity) -> {
            boolean isAlive = beybladeEntity.isAlive();
            boolean isStopped = beybladeEntity.isStopped();
            if (!isStopped && isAlive && beysIn.contains(beybladeEntity)) {
                if (beybladeEntity.isDropped())
                    cancelRound();
                else {
                    beysSpinning.getAndIncrement();
                    lastSpinningBeyPlayer.set(entity);
                }
            }
        }));

        if (beysSpinning.get() == 1 && this.beys.size() > 1 && stadium.isAllowBattle()) {
            switch (type) {
                case NORMAL_MATCH:
                    PlayerEntity winner = lastSpinningBeyPlayer.get();
                    if (winner != null && points.containsKey(winner)) {
                        AtomicInteger beysAlive = new AtomicInteger();
                        beys.forEach(((entity, beybladeEntity) -> {
                            increaseXP((ServerPlayerEntity) entity, entity == winner);
                            if (beybladeEntity.isAlive())
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
            if (beysSpinning.get() != beysIn.size()) {
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

    private void increaseXP(ServerPlayerEntity player, boolean winning) {
        player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
            AtomicInteger averageLevel = new AtomicInteger();
            beys.forEach((auxPlayer, bey) -> {
                if (auxPlayer != player)
                    auxPlayer.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(bladerLevel -> {
                        averageLevel.set(averageLevel.get() + bladerLevel.getBladerLevel().getLevel());
                    });
            });
            averageLevel.set(averageLevel.get() / beys.size());
            h.getBladerLevel().increaseExperience(round(
                    (averageLevel.get() * 0.025F) / (winning ? 1 : 25), 2));
            h.syncToAll();
        });
    }

    private void setWinner(ServerPlayerEntity player) {
        if (battleForRanking) {
            points.forEach((auxPlayer, points) -> {
//                auxPlayer.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
//                    h.getWallet().increaseCurrency(this.points.get(auxPlayer));
//                });
                ItemEntity coins = new ItemEntity(auxPlayer.level, auxPlayer.position().x, auxPlayer.position().y, auxPlayer.position().z, new ItemStack(ModItems.BEYCOIN, this.points.get(auxPlayer)));
                auxPlayer.level.addFreshEntity(coins);
                if (auxPlayer != player)
                    auxPlayer.displayClientMessage(new TranslationTextComponent("beycraft.lose_combat"), true);
                else
                    auxPlayer.displayClientMessage(new TranslationTextComponent("beycraft.win_combat"), true);
            });
        } else {
//            player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(h -> {
//                h.getWallet().increaseCurrency(this.points.get(player));
//            });
            ItemEntity coins = new ItemEntity(player.level, player.position().x, player.position().y, player.position().z, new ItemStack(ModItems.BEYCOIN, this.points.get(player)));
            player.level.addFreshEntity(coins);
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
