package ga.beycraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class PlayerUtils {

    public static PlayerEntity getPlayerByName(String playerName, ServerWorld level){
        PlayerEntity playerEntity = null;
        for (PlayerEntity player : level.getServer().getPlayerList().getPlayers()) {
            if(player.getName().getString().equals(playerName))
                playerEntity = player;
        }
        return playerEntity;
    }
}
