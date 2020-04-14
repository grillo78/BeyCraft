package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.LauncherContainer;
import com.grillo78.beycraft.items.ItemDualLauncher;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageBeyCreatorUpdate;
import com.grillo78.beycraft.network.message.MessageUpdateDualLauncher;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author a19guillermong
 *
 */
public class LauncherDualGUI extends ContainerScreen<LauncherContainer>{

	private Button rotationButton;
	private ItemStack launcher;

	/**
	 * @param screenContainer
	 * @param inv
	 * @param titleIn
	 */
	public LauncherDualGUI(LauncherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init() {
		super.init();
		if(Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ItemDualLauncher){
			launcher = Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND);
		}else{
			launcher = Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND);
		}
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		if(!launcher.hasTag() || !launcher.getTag().contains("rotation")){
			PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
		}
		if(!launcher.hasTag()){
			CompoundNBT compound = new CompoundNBT();
			compound.putInt("rotation", 1);
			launcher.setTag(compound);
		}
		if(!launcher.getTag().contains("rotation")){
			CompoundNBT compound = launcher.getTag();
			compound.putInt("rotation", 1);
			launcher.setTag(compound);
		}
		if(launcher.getTag().getInt("rotation")==1){
			rotationButton = new Button(relX+5, relY+30, 30, 20, "Right", (Button) -> {
				switch (launcher.getTag().getInt("rotation")){
					case 1:
						PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
						launcher.getTag().putInt("rotation",-1);
						rotationButton.setMessage("Left");
						break;
					case -1:
						PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(1));
						launcher.getTag().putInt("rotation",1);
						rotationButton.setMessage("Right");
						break;
				}
			});
		} else {

			rotationButton = new Button(relX+5, relY+30, 30, 20, "Left", (Button) -> {
				switch (launcher.getTag().getInt("rotation")){
					case 1:
						PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(-1));
						launcher.getTag().putInt("rotation",-1);
						rotationButton.setMessage("Left");
						break;
					case -1:
						PacketHandler.instance.sendToServer(new MessageUpdateDualLauncher(1));
						launcher.getTag().putInt("rotation",1);
						rotationButton.setMessage("Right");
						break;
				}
			});
		}
		this.addButton(rotationButton);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/launcher.png"));
		int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
