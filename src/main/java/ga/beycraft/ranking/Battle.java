package ga.beycraft.ranking;

import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageLoseCombat;
import ga.beycraft.network.message.MessageSyncBladerLevel;
import ga.beycraft.network.message.MessageWinCombat;
import ga.beycraft.tileentity.StadiumTileEntity;
import ga.beycraft.util.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Battle {

    private HashMap<PlayerEntity, Integer> points = new HashMap<>();
    private List<EntityBey> beys = new ArrayList<>();
    private int startCount = 0;
    private final ServerWorld level;
    private BattleTypes type = BattleTypes.NORMAL_MATCH;
    private StadiumTileEntity stadium;

    public Battle(ServerWorld level, StadiumTileEntity stadium) {
        this.level = level;
        this.stadium = stadium;
    }

    public void tick(List<EntityBey> beys) {
        if (startCount < 20) {
            for (EntityBey bey : beys) {
                PlayerEntity player = PlayerUtils.getPlayerByName(bey.getPlayerName(), level);
                if (player != null) {
                    if (!points.containsKey(player)) {
                        points.put(player, 0);
                        if (!this.beys.contains(bey)) {
                            this.beys.add(bey);
                        }
                    }
                }
            }
            if (points.size() > 2) {
                type = BattleTypes.BATTLE_ROYAL;
            }
            startCount++;
        } else {
            if (beys.size() > points.size()) {
                stadium.invalidateBattle();
            }
        }

        int beysSpinning = 0;
        int lastSpinningBeyIndex = 0;
        for (int i = 0; i < this.beys.size(); i++) {
            if (!this.beys.get(i).isStoped()) {
                beysSpinning++;
                lastSpinningBeyIndex = i;
            }
        }

        if (beysSpinning == 1) {
            switch (type) {
                case NORMAL_MATCH:
                    PlayerEntity winner = PlayerUtils.getPlayerByName(this.beys.get(lastSpinningBeyIndex).getPlayerName(), level);
                    if(winner != null && points.containsKey(winner)){
                        points.put(winner, points.get(winner) + 1);
                        if (points.get(winner) >= stadium.getPointsToWin())
                            setWinner((ServerPlayerEntity) winner);
                    }
                    stadium.setAllowBattle(false);
                    break;
                case BATTLE_ROYAL:
                    setWinner((ServerPlayerEntity) PlayerUtils.getPlayerByName(this.beys.get(lastSpinningBeyIndex).getPlayerName(), level));
                    break;
            }
        }
    }

    private void setWinner(ServerPlayerEntity player) {
        player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(h -> {
            Random rand = new Random();
                h.increaseExperience(round(
                        rand.nextInt(h.getBladerLevel() * beys.size())/5 + rand.nextFloat(), 2));
            PacketHandler.instance.sendTo(new MessageWinCombat(), player.connection.getConnection(),
                    NetworkDirection.PLAY_TO_CLIENT);
            PacketHandler.instance.sendTo(new MessageSyncBladerLevel(h.getExperience()), player.connection.getConnection(),
                    NetworkDirection.PLAY_TO_CLIENT);
        });
        points.forEach((auxPlayer,points)->{
            if(auxPlayer != player){
                PacketHandler.instance.sendTo(new MessageLoseCombat(), ((ServerPlayerEntity)auxPlayer).connection.getConnection(),
                        NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        stadium.invalidateBattle();
    }

    public float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public enum BattleTypes {
        NORMAL_MATCH, BATTLE_ROYAL
    }
}
