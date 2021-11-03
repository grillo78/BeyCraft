package ga.beycraft.api.currency;

import ga.beycraft.capabilities.BladerCapProvider;
import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.atomic.AtomicReference;

public class CurrencyUtils {

    public static void decreaseMoney(PlayerEntity player, float amount) {
        player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
            h.increaseCurrency(-amount);
        });
    }

    public static float getMoney(PlayerEntity player) {
        AtomicReference<Float> money = new AtomicReference<>((float) 0);
        player.getCapability(BladerCapProvider.BLADERCURRENCY_CAP).ifPresent(h -> {
            money.set(h.getCurrency());
        });
        return money.get();
    }
}
