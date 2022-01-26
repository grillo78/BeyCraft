package ga.beycraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PlayerUtils {

    public static PlayerEntity getPlayerByName(String playerName, World level){
        PlayerEntity playerEntity = null;
        for (PlayerEntity player : level.players()) {
            if(player.getName().getString().equals(playerName))
                playerEntity = player;
        }
        return playerEntity;
    }
}
