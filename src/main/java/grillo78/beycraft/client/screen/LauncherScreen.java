package grillo78.beycraft.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.common.capability.item.LauncherCapabilityProvider;
import grillo78.beycraft.common.container.LauncherContainer;
import grillo78.beycraft.common.item.LauncherItem;
import grillo78.beycraft.network.PacketHandler;
import grillo78.beycraft.network.message.UpdateLauncherColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LauncherScreen extends ContainerScreen<LauncherContainer> {
    private TextFieldWidget redText;
    private TextFieldWidget greenText;
    private TextFieldWidget blueText;
    private Button setColorBtn;

    public LauncherScreen(LauncherContainer launcherContainer, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(launcherContainer, playerInventory, textComponent);
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        setColorBtn = new Button(relX + 5, relY + 53, 50, 20, new StringTextComponent("Set color"), (Button) -> {
            try {
                PacketHandler.INSTANCE.sendToServer(new UpdateLauncherColor(Integer.valueOf(redText.getValue()), Integer.valueOf(greenText.getValue()), Integer.valueOf(blueText.getValue())));
            } catch (NumberFormatException e) {

            }
        });
        this.addButton(setColorBtn);

        redText = new TextFieldWidget(font, relX + 100, relY + 10, 50, 10, new StringTextComponent(""));
        children.add(redText);
        greenText = new TextFieldWidget(font, relX + 100, relY + 25, 50, 10, new StringTextComponent(""));
        children.add(greenText);
        blueText = new TextFieldWidget(font, relX + 100, relY + 40, 50, 10, new StringTextComponent(""));
        children.add(blueText);
        if (Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof LauncherItem) {
            Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND).getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> {
                redText.setValue(String.valueOf(cap.getRed()));
                greenText.setValue(String.valueOf(cap.getGreen()));
                blueText.setValue(String.valueOf(cap.getBlue()));
            });
        } else {
            Minecraft.getInstance().player.getItemInHand(Hand.OFF_HAND).getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> {
                redText.setValue(String.valueOf(cap.getRed()));
                greenText.setValue(String.valueOf(cap.getGreen()));
                blueText.setValue(String.valueOf(cap.getBlue()));
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        redText.tick();
        greenText.tick();
        blueText.tick();
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.draw(p_230451_1_, this.inventory.getDisplayName().getVisualOrderText(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        this.font.draw(p_230451_1_, new StringTextComponent("R:").getVisualOrderText(), imageWidth / 2, imageHeight - 155.0F, 4210752);
        this.font.draw(p_230451_1_, new StringTextComponent("G:").getVisualOrderText(), imageWidth / 2, imageHeight - 140.0F, 4210752);
        this.font.draw(p_230451_1_, new StringTextComponent("B:").getVisualOrderText(), imageWidth / 2, imageHeight - 125.0F, 4210752);

    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);

        redText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        greenText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        blueText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.getMinecraft().getTextureManager().bind(new ResourceLocation(Beycraft.MOD_ID, "textures/gui/container/3_slots.png"));
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
