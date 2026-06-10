package grillo78.beycraft.client.item;

import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class LauncherClientExtensions implements IClientItemExtensions {

    private final LauncherBEWLR launcherBEWLR = new LauncherBEWLR();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return launcherBEWLR;
    }

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return hand ==InteractionHand.MAIN_HAND && itemStack.has(ModDataComponents.LAUNCHER_BEY)? HumanoidModel.ArmPose.CROSSBOW_CHARGE : null;
    }
}
