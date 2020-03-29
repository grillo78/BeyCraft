package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.events.ClientEvents;
import com.grillo78.beycraft.inventory.BeltContainer;
import com.grillo78.beycraft.inventory.BeyCreatorContainer;
import com.grillo78.beycraft.items.ItemBeyDriver;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author a19guillermong
 */
public class BeyCreatorGUI extends ContainerScreen<BeyCreatorContainer> {

    private int partCount = 0;
    private int partType = 0;
    private Button prevPart;
    private Button nextPart;
    private Button nextPartType;

    /**
     * @param screenContainer
     * @param inv
     * @param titleIn
     */
    public BeyCreatorGUI(BeyCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        prevPart = new Button(relX, relY, 10, 20, "<", (Button) -> {
            switch (partType) {
                case 0:
                    if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSLAYER.size()-1;
                        }
                    }
                    break;
                case 1:
                    if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSDISCLIST.size()-1;
                        }
                    }
                    break;
                case 2:
                    if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSDRIVER.size()-1;
                        }
                    }
                    break;

                case 3:
                    if (!BeyRegistry.ITEMSFRAMELIST.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSFRAMELIST.size()-1;
                        }
                    }
                    break;
            }
        });
        nextPart = new Button(relX + 65, relY, 10, 20, ">", (Button) -> {
            switch (partType) {
                case 0:
                    if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSLAYER.size()) {
                            partCount = 0;
                        }
                    }
                    break;
                case 1:
                    if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSDISCLIST.size()) {
                            partCount = 0;
                        }
                    }
                    break;
                case 2:
                    if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSDRIVER.size()) {
                            partCount = 0;
                        }
                    }
                    break;

                case 3:
                    if (!BeyRegistry.ITEMSFRAMELIST.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSFRAMELIST.size()) {
                            partCount = 0;
                        }
                    }
                    break;
            }
        });
        nextPartType = new Button(relX + 15, relY, 45, 20, "Layer", (Button) -> {
            partType++;
            partCount = 0;
            if (partType == 4) {
                partType = 0;
            }
            switch (partType) {
                case 0:
                    nextPartType.setMessage("Layers");
                    break;
                case 1:
                    nextPartType.setMessage("Discs");
                    break;
                case 2:
                    nextPartType.setMessage("Drivers");
                    break;
                case 3:
                    nextPartType.setMessage("Frames");
                    break;
            }
        });
        this.addButton(nextPartType);
        this.addButton(prevPart);
        this.addButton(nextPart);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/container/beycreator.png"));
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
        ITextComponent partName = TextComponentUtils.toTextComponent(new Message() {

            @Override
            public String getString() {
                switch (partType){
                    case 0:
                        if(!BeyRegistry.ITEMSLAYER.isEmpty()){
                            return BeyRegistry.ITEMSLAYER.get(partCount).getName().getString();
                        }
                        return "";
                    case 1:
                        if(!BeyRegistry.ITEMSDISCLIST.isEmpty()){
                            return BeyRegistry.ITEMSDISCLIST.get(partCount).getName().getString();
                        }
                        return "";
                    case 2:
                        if(!BeyRegistry.ITEMSDRIVER.isEmpty()){
                            return BeyRegistry.ITEMSDRIVER.get(partCount).getName().getString();
                        }
                        return "";
                    case 3:
                        if(!BeyRegistry.ITEMSFRAMELIST.isEmpty()){
                            return BeyRegistry.ITEMSFRAMELIST.get(partCount).getName().getString();
                        }
                        return "";
                }
                return "";
            }
        });
        Style sPartName = new Style();
        sPartName.setColor(TextFormatting.WHITE);
        partName.setStyle(sPartName);
        switch (partType) {
            case 0:
                if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSLAYER.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
            case 1:
                if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSDISCLIST.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
            case 2:
                if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSDRIVER.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
            case 3:
                if (!BeyRegistry.ITEMSFRAMELIST.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSFRAMELIST.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
        }
        this.font.drawString( partName.getFormattedText(), relX, relY + 25, 50);
    }

    private void renderItemIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        RenderSystem.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float) x, (float) y, 100.0F + Minecraft.getInstance().getItemRenderer().zLevel);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(128, 128, 128);
        MatrixStack matrixstack = new MatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        RenderHelper.setupGuiFlatDiffuseLighting();

        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        RenderHelper.setupGui3DDiffuseLighting();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }
}
