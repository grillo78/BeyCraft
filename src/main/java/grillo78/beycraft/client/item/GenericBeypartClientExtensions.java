package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class GenericBeypartClientExtensions implements IClientItemExtensions {

    private final GenericBeypartBEWLR genericBeypartBEWLR = new GenericBeypartBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return genericBeypartBEWLR;
    }
}
