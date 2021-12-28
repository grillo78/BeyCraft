package ga.beycraft.abilities;

import ga.beycraft.entity.EntityBey;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageSpawnSpark;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkDirection;

public class CounterStrike extends Ability {

    private static int ANGLE = -45;

    @Override
    public void executeAbility(EntityBey entity) {
        if (entity.getRadius() != 0 && entity.getTickCount() % 30 == 0 && (entity.getRadius() / entity.getMaxRadius())>0.1) {
            entity.yRot += entity.getRotationDirection() * ANGLE;
            for (int i = 0; i < 30; i++) {
                entity.level.players().forEach(playerIn -> PacketHandler.instance.sendTo(new MessageSpawnSpark(entity.getId(), (random.nextBoolean()?1:-1)*random.nextFloat()/50, random.nextFloat()/50, (random.nextBoolean()?1:-1)*random.nextFloat()/50),
                        ((ServerPlayerEntity) playerIn).connection.getConnection(),
                        NetworkDirection.PLAY_TO_CLIENT));

            }
        }
    }

    @Override
    public void executeAbility(ItemStack piece) {
    }

}
