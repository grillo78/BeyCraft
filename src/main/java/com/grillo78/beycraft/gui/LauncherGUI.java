package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.LauncherContainer;
import com.grillo78.beycraft.items.ItemDualLauncher;
import com.grillo78.beycraft.items.ItemLauncher;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageUpdateColorLauncher;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

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

/**
 * @author a19guillermong
 *
 */
public class LauncherGUI extends ContainerScreen<LauncherContainer>{

	private ItemStack launcher;
	private TextFieldWidget redText;
	private TextFieldWidget greenText;
	private TextFieldWidget blueText;
	private Button setColorBtn;

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public LauncherGUI(LauncherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void tick() {
		super.tick();
		redText.tick();
		greenText.tick();
		blueText.tick();
	}

	@Override
	protected void init() {
		super.init();
		if (Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemLauncher) {
			launcher = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
		} else {
			launcher = Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND);
		}
		if (!launcher.hasTag()) {
			CompoundNBT compound = new CompoundNBT();
			launcher.setTag(compound);
		}

		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;

		setColorBtn = new Button(relX+5, relY+53, 50, 20,new StringTextComponent("Set color"), (Button) ->{
			try{
				PacketHandler.instance.sendToServer(new MessageUpdateColorLauncher(Float.valueOf(redText.getText())/255f,Float.valueOf(greenText.getText())/255f,Float.valueOf(blueText.getText())/255f));
			}catch (NumberFormatException e){

			}
		});
		this.addButton(setColorBtn);

		redText = new TextFieldWidget(font,relX+100,relY+10,50,10, new StringTextComponent(""));
		children.add(redText);
		greenText = new TextFieldWidget(font,relX+100,relY+25,50,10, new StringTextComponent(""));
		children.add(greenText);
		blueText = new TextFieldWidget(font,relX+100,relY+40,50,10, new StringTextComponent(""));
		if(!launcher.getTag().contains("color")) {
			redText.setText("255.0");
			greenText.setText("255.0");
			blueText.setText("255.0");
		}else{
			redText.setText(String.valueOf(launcher.getTag().getCompound("color").getFloat("red")*255));
			greenText.setText(String.valueOf(launcher.getTag().getCompound("color").getFloat("green")*255));
			blueText.setText(String.valueOf(launcher.getTag().getCompound("color").getFloat("blue")*255));
		}
		children.add(blueText);
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
		redText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		greenText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		blueText.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	//	@Override
//	public void render(int mouseX, int mouseY, float partialTicks) {
//		this.renderBackground();
//		super.render(mouseX, mouseY, partialTicks);
//		this.redText.render(mouseX, mouseY, partialTicks);
//		this.greenText.render(mouseX, mouseY, partialTicks);
//		this.blueText.render(mouseX, mouseY, partialTicks);
//		int relX = (this.width - this.xSize) / 2;
//		int relY = (this.height - this.ySize) / 2;
//		this.font.drawString("R:", relX+85.0F, relY+10.0F, 4210752);
//		this.font.drawString("G:", relX+85.0F, relY+25.0F, 4210752);
//		this.font.drawString("B:", relX+85.0F, relY+40.0F, 4210752);
//		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), relX+5,relY+74, 4210752);
//		this.renderHoveredToolTip(mouseX, mouseY);
//	}

	@Override
	protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
		this.font.func_238422_b_(p_230451_1_, this.playerInventory.getDisplayName(), (float)this.field_238744_r_, (float)this.field_238745_s_, 4210752);
		this.font.func_238422_b_(p_230451_1_, new StringTextComponent("R:"), xSize/2, ySize-155.0F, 4210752);
		this.font.func_238422_b_(p_230451_1_, new StringTextComponent("G:"), xSize/2, ySize-140.0F, 4210752);
		this.font.func_238422_b_(p_230451_1_, new StringTextComponent("B:"), xSize/2, ySize-125.0F, 4210752);
	}

	@Override
	protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/launcher.png"));
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

	}
}
