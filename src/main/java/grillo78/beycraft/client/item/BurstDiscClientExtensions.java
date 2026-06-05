package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class BurstDiscClientExtensions implements IClientItemExtensions {

    private final BurstDiscBEWLR burstDiscBEWLR = new BurstDiscBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return burstDiscBEWLR;
    }
}
