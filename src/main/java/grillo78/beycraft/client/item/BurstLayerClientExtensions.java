package grillo78.beycraft.client.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class BurstLayerClientExtensions implements IClientItemExtensions {

    private final BurstLayerBEWLR burstLayerBEWLR = new BurstLayerBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return burstLayerBEWLR;
    }
}
