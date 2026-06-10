package grillo78.beycraft.client.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class XBladeClientExtensions implements IClientItemExtensions {

    private final XBladeBEWLR xBladeBEWLR = new XBladeBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return xBladeBEWLR;
    }
}
