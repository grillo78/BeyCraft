package grillo78.beycraft.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import grillo78.beycraft.client.screen.widget.AnimationsList;
import grillo78.beycraft.client.screen.widget.LaunchesList;
import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class LaunchScreen extends Screen {

    private LaunchesList launches;
    private AnimationsList animations;

    public LaunchScreen(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
        Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElseThrow(NullPointerException::new);
    }

    @Override
    protected void init() {
        super.init();
        animations = new AnimationsList();
        this.children.add(animations);
        launches = new LaunchesList();
        this.children.add(launches);
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader ->{
            blader.setLaunchType(launches.getSelected().getLaunch());
            blader.setAnimatorID(animations.getSelected().getId());
            blader.syncToAll();
        });
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack, -1);
        launches.render(matrixStack, mouseX, mouseY, partialTicks);
        animations.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
