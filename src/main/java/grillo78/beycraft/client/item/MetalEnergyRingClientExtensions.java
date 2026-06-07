package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class MetalEnergyRingClientExtensions implements IClientItemExtensions {

    private final MetalEnergyRingBEWLR metalEnergyRingBEWLR = new MetalEnergyRingBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return metalEnergyRingBEWLR;
    }
}
