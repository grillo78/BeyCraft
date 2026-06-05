package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class BurstDriverClientExtensions implements IClientItemExtensions {

    private final BurstDriverBEWLR burstDriverBEWLR = new BurstDriverBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return burstDriverBEWLR;
    }
}
