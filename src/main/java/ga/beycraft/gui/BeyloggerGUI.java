package ga.beycraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import ga.beycraft.Reference;
import ga.beycraft.inventory.slots.BeyLoggerContainer;
import ga.beycraft.items.ItemBeyLogger;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageUpdateUrlBeyLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author a19guillermong
 *
 */
public class BeyloggerGUI extends ContainerScreen<BeyLoggerContainer> {

	private ItemStack beyLogger;
	private TextFieldWidget urlText;
	private Button setUrlBtn;

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public BeyloggerGUI(BeyLoggerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		if (Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemBeyLogger) {
			beyLogger = Minecraft.getInstance().player.getItemInHand(Hand.MAIN_HAND);
		} else {
			beyLogger = Minecraft.getInstance().player.getItemInHand(Hand.OFF_HAND);
		}
		if (!beyLogger.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			beyLogger.setTag(compound);
		}

		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;

		setUrlBtn = new Button(relX + 9, relY + 40, 50, 20, new TranslationTextComponent("gui.done"), (Button) -> {
			beyLogger.getTag().putString("url", urlText.getValue());
			PacketHandler.instance.sendToServer(new MessageUpdateUrlBeyLogger(urlText.getValue()));
			Minecraft.getInstance().setScreen(null);
		});
		this.addButton(setUrlBtn);

		urlText = new TextFieldWidget(font, relX + 10, relY + 25, 155, 10, new StringTextComponent(""));
		urlText.setMaxLength(Integer.MAX_VALUE);
		children.add(urlText);
		if (beyLogger.getTag().contains("url")) {
			urlText.setValue(beyLogger.getTag().getString("url"));
		}
	}

	@Override
	protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
		this.font.draw(p_230451_1_, new StringTextComponent("URL:").getVisualOrderText(), imageWidth/2, imageHeight-155.0F, 4210752);
	}

	@Override
	public void tick() {
		super.tick();
		urlText.tick();
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
		urlText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager()
				.bind(new ResourceLocation(Reference.MOD_ID, "textures/gui/container/beylogger.png"));
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}
}
