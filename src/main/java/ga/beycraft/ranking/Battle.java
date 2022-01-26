package ga.beycraft.ranking;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageLoseCombat;
import ga.beycraft.network.message.MessageSyncBladerLevel;
import ga.beycraft.network.message.MessageWinCombat;
import ga.beycraft.tileentity.BattleInformerTileEntity;
import ga.beycraft.tileentity.StadiumTileEntity;
import ga.beycraft.util.PlayerUtils;
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
    private HashMap<PlayerEntity, EntityBey> beys = new HashMap<>();
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

    public void tick(List<EntityBey> beysIn) {
        if (startCount < 60) {
            for (EntityBey bey : beysIn) {
                PlayerEntity player = PlayerUtils.getPlayerByName(bey.getPlayerName(), level);
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
        this.beys.forEach(((entity, entityBey) -> {
            boolean isAlive = entityBey.isAlive();
            boolean isStopped = entityBey.isStopped();
            if (!isStopped && isAlive && beysIn.contains(entityBey)) {
                if (entityBey.isPickedUp())
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
                        beys.forEach(((entity, entityBey) -> {
                            increaseXP((ServerPlayerEntity) entity, entity == winner);
                            if (entityBey.isAlive())
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
        player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
            Random rand = new Random();
            AtomicInteger averageLevel = new AtomicInteger();
            beys.forEach((auxPlayer, bey) -> {
                if (auxPlayer != player)
                    auxPlayer.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(bladerLevel -> {
                        averageLevel.addAndGet(averageLevel.get() + bladerLevel.getBladerLevel());
                    });
            });
            averageLevel.set(averageLevel.get()/beys.size());
            h.increaseExperience(round(
                    (averageLevel.get() * 0.025F)/(winning?1:25), 2));
            PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience(), h.isInResonance(), true, player.getId()), player.connection.getConnection(),
                    NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    private void setWinner(ServerPlayerEntity player) {
        if(battleForRanking){
            player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
                h.increaseCurrency(points.get(player));
            });

            PacketHandler.instance.sendTo(new MessageWinCombat(), player.connection.getConnection(),
                    NetworkDirection.PLAY_TO_CLIENT);
            points.forEach((auxPlayer, points) -> {
                if (auxPlayer != player) {
                    PacketHandler.instance.sendTo(new MessageLoseCombat(), ((ServerPlayerEntity) auxPlayer).connection.getConnection(),
                            NetworkDirection.PLAY_TO_CLIENT);

                    auxPlayer.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
                        h.increaseCurrency(this.points.get(auxPlayer));
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
