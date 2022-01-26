package ga.beycraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NotSupportedVersionScreen extends Screen {

	private ITextComponent text = new TranslationTextComponent("not_supported_version.message");
	private String[] textArray = text.getString().split("\n");
	private static ResourceLocation BEYCRAFT_LOGO = new ResourceLocation(Reference.MOD_ID, "textures/logo.png");

	private Button acceptButton;

	public NotSupportedVersionScreen(ITextComponent titleIn) {
		super(titleIn);
	}

	@Override
	protected void init() {
		super.init();
		acceptButton = new Button(width / 2 - 15, height / 2 + 60, 30, 20, new TranslationTextComponent("gui.ok"),
				(Button) -> {
					Minecraft.getInstance().close();
				});
		this.addButton(acceptButton);
	}

	@Override
	public void removed() {
		super.removed();
		Minecraft.getInstance().close();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		minecraft.getTextureManager().bind(BEYCRAFT_LOGO);
		int logoWidth = 100;
		int logoHeight = 40;
		this.blit(matrixStack, width / 2 - logoWidth / 2, 30, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);

		for (int i = 0; i < textArray.length; i++) {
			drawCenteredString(matrixStack, font, new StringTextComponent(textArray[i]), this.width / 2,
					15 * i + height / 2, 16777215);
		}
		acceptButton.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

}
