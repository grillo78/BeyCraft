package ga.beycraft.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.common.capability.entity.BladerCapabilityProvider;
import ga.beycraft.common.launch.LaunchType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class LaunchScreen extends Screen {

    private LaunchType selected;

    public LaunchScreen(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
        Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElseThrow(NullPointerException::new);
    }

    @Override
    protected void init() {
        super.init();
        for (LaunchType launch : LaunchType.LAUNCH_TYPES.getValues()) {
            this.addButton(new Button(launch.getX(), launch.getY(), 20, 20, new TranslationTextComponent("launch."+launch.getRegistryName().getPath()), (p_238896_1_) -> {
                selected = launch;
            }));
        }
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_,p_230430_2_,p_230430_3_,p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);

    }

    @Override
    public boolean mouseDragged(double x, double y, int mouseButton, double width, double height) {
        boolean dragging = super.mouseDragged(x, y, mouseButton, width, height);
        if (dragging){
            for (Widget button : buttons) {
                button.x+=x;
            }
        }
        return dragging;
    }
}
