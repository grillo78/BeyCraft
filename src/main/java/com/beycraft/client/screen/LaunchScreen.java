package com.beycraft.client.screen;

import com.beycraft.Beycraft;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.beycraft.common.launch.LaunchType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            LaunchButton button = new LaunchButton((int) xCoord, (int) yCoord - 25, 50, 50, new TranslationTextComponent("launch." + launch.getRegistryName().getPath()), this, launch);
            this.addButton(button);
            buttonsMap.put(launch, button);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack, -1);
        for (Widget widget : buttons) {
            if (widget instanceof LaunchButton) {
                LaunchButton button = (LaunchButton) widget;
                if (button.launch.getRequisite() != null) {
                    int color = !button.isHovered() ? -16777216 : -1;
                    LaunchButton requisiteButton = buttonsMap.get(LaunchType.LAUNCH_TYPES.getValue(button.launch.getRequisite()));
                    if (button.y != requisiteButton.y)
                        vLine(matrixStack, requisiteButton.x + 25, requisiteButton.y + 25, button.y+25, color);
                    hLine(matrixStack, button.x - 1, requisiteButton.x + 25, button.y + 25, color);
                }
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
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

    private static class LaunchButton extends Button {

        private static final ResourceLocation TEXTURE = new ResourceLocation(Beycraft.MOD_ID, "textures/gui/launch_button.png");
        private static final int iconWidth = 42;
        private static final int iconHeight = 42;
        private final ResourceLocation launchTexture;
        private LaunchType launch;
        private boolean selected = false;

        public LaunchButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, LaunchScreen screen, LaunchType launch) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, (button) -> {
                for (LaunchButton buttonAux : screen.buttonsMap.values()) {
                    buttonAux.selected = false;
                }
                ((LaunchButton) button).selected = true;
                Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader->{
                    blader.setLaunchType(((LaunchButton) button).launch);
                    blader.syncToAll();
                });
            }, (p_238659_1_, p_238659_2_, p_238659_3_, p_238659_4_) -> {
                if (p_238659_1_.active) {
                    List<IReorderingProcessor> lines = new ArrayList<>();
                    lines.addAll(screen.minecraft.font.split(new TranslationTextComponent("button." + launch.getRegistryName().getPath() + ".name"), Math.max(screen.width / 2 - 43, 170)));
                    lines.addAll(screen.minecraft.font.split(new TranslationTextComponent("button."+launch.getRegistryName().getPath()+".description"), Math.max(screen.width / 2 - 43, 170)));
                    lines.addAll(screen.minecraft.font.split(new TranslationTextComponent("button."+launch.getRegistryName().getPath()+".requirements"), Math.max(screen.width / 2 - 43, 170)));
                    screen.renderTooltip(p_238659_2_, lines, p_238659_3_, p_238659_4_);
                }

            });
            this.launch = launch;
            launchTexture = new ResourceLocation(launch.getRegistryName().getNamespace(), "textures/gui/launch/" + launch.getRegistryName().getPath() + ".png");
            Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader->{
                selected = launch == blader.getLaunchType();
            });
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
            minecraft.getTextureManager().bind(launchTexture);
            TextureAtlasSprite sprite = launch.getRenderMaterial().sprite();
            this.blit(p_230431_1_, this.x + 4, this.y + 4, 0, iconHeight*2, iconWidth, iconHeight, iconWidth, iconHeight*3);
            this.renderBg(p_230431_1_, minecraft, p_230431_2_, p_230431_3_);
            if (this.isHovered)
                this.renderToolTip(p_230431_1_, p_230431_2_, p_230431_3_);
        }

        @Override
        protected int getYImage(boolean hovered) {
            return selected ? 100 : hovered ? 50 : 0;
        }
    }
}
