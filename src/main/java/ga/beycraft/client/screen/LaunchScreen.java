package ga.beycraft.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import ga.beycraft.Beycraft;
import ga.beycraft.common.capability.entity.BladerCapabilityProvider;
import ga.beycraft.common.launch.LaunchType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;

public class LaunchScreen extends Screen {

    private LaunchType selected;
    private HashMap<LaunchType, LaunchButton> buttonsMap = new HashMap<>();

    public LaunchScreen(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
        Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElseThrow(NullPointerException::new);
    }

    @Override
    protected void init() {
        super.init();
        for (LaunchType launch : LaunchType.LAUNCH_TYPES.getValues()) {
            float xCoord = this.width * ((float) launch.getX()) / 100;
            float yCoord = this.height * ((float) launch.getY()) / 100;
            LaunchButton button = new LaunchButton((int) xCoord, (int) yCoord-25, 50, 50, new TranslationTextComponent("launch." + launch.getRegistryName().getPath()), launch);
            this.addButton(button);
            buttonsMap.put(launch, button);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        for (Widget widget : buttons) {
            if(widget instanceof LaunchButton) {
                LaunchButton button = (LaunchButton) widget;
//                int j1 = p_238692_4_ ? -16777216 : -1;
                if(button.launch.getRequisite() != null)
                    hLine(matrixStack, button.x-1, buttonsMap.get(LaunchType.LAUNCH_TYPES.getValue(button.launch.getRequisite())).x+50,button.y+25,-16777216);
            }
        }
    }

    @Override
    public boolean mouseDragged(double x, double y, int mouseButton, double deltaX, double deltaY) {
        setDragging(mouseButton == 0);
        if (isDragging()) {
            for (Widget button : this.buttons) {
                button.x += deltaX * (double) this.minecraft.getWindow().getScreenWidth() / (double) this.minecraft.getWindow().getGuiScaledWidth();
                button.y += deltaY * (double) this.minecraft.getWindow().getScreenHeight() / (double) this.minecraft.getWindow().getGuiScaledHeight();
            }
        }
        return isDragging();
    }

    private static class LaunchButton extends Button{

        private static final ResourceLocation TEXTURE = new ResourceLocation(Beycraft.MOD_ID, "textures/gui/launch_button.png");
        private LaunchType launch;

        public LaunchButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, LaunchType launch) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, (button)->{});
            this.launch = launch;
        }

        @Override
        public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bind(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            int i = this.getYImage(this.isHovered());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(p_230431_1_, this.x, this.y, 0, 0 + i, this.width, this.height);
            this.renderBg(p_230431_1_, minecraft, p_230431_2_, p_230431_3_);
            if (this.isHovered)
                this.renderToolTip(p_230431_1_,p_230431_2_,p_230431_3_);
        }

        @Override
        protected int getYImage(boolean hovered) {
            return !hovered ? 0:50;
        }
    }
}
