package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class BurstCoreDiscClientExtensions implements IClientItemExtensions {

    private final BurstCoreDiscBEWLR burstCoreDiscBEWLR = new BurstCoreDiscBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return burstCoreDiscBEWLR;
    }
}
