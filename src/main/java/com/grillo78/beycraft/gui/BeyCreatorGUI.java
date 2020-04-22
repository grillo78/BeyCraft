package com.grillo78.beycraft.gui;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.inventory.BeyCreatorContainer;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageBeyCreatorUpdate;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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
    private RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
    private BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getPos();

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
                            partCount = BeyRegistry.ITEMSLAYER.size() - 1;
                        }
                    }
                    break;
                case 1:
                    if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSDISCLIST.size() - 1;
                        }
                    }
                    break;
                case 2:
                    if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSDRIVER.size() - 1;
                        }
                    }
                    break;

                case 3:
                    if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSFRAME.size() - 1;
                        }
                    }
                    break;
                case 4:
                    if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSGTCHIP.size() - 1;
                        }
                    }
                    break;
                case 5:
                    if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
                        partCount--;
                        if (partCount == -1) {
                            partCount = BeyRegistry.ITEMSGTWEIGHT.size() - 1;
                        }
                    }
                    break;
            }
        });
        nextPart = new Button(relX + 66, relY, 10, 20, ">", (Button) -> {
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
                    if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSFRAME.size()) {
                            partCount = 0;
                        }
                    }
                    break;

                case 4:
                    if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSGTCHIP.size()) {
                            partCount = 0;
                        }
                    }
                    break;
                case 5:
                    if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
                        partCount++;
                        if (partCount == BeyRegistry.ITEMSGTWEIGHT.size()) {
                            partCount = 0;
                        }
                    }
                    break;
            }
        });
        nextPartType = new Button(relX + 10, relY, 56, 20, "Layer", (Button) -> {
            partType++;
            partCount = 0;
            if (partType == 6) {
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
                case 4:
                    nextPartType.setMessage("GT Chips");
                    break;
                case 5:
                    nextPartType.setMessage("GT Weights");
                    break;
            }
        });
        this.addButton(new Button(relX, relY + 25, 54, 20, "Accept", (Button) -> {
            ItemStack stack = null;
            switch (partType) {
                case 0:
                    if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSLAYER.get(partCount));
                    }
                    break;
                case 1:
                    if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSDISCLIST.get(partCount));
                    }
                    break;
                case 2:
                    if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSDRIVER.get(partCount));
                    }
                    break;

                case 3:
                    if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSFRAME.get(partCount));
                    }
                    break;

                case 4:
                    if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSGTCHIP.get(partCount));
                    }
                    break;

                case 5:
                    if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
                        stack = new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(partCount));
                    }
                    break;
            }
            if (stack != null) PacketHandler.instance.sendToServer(new MessageBeyCreatorUpdate(stack, pos));
        }));
        this.addButton(nextPartType);
        this.addButton(prevPart);
        this.addButton(nextPart);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
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
                switch (partType) {
                    case 0:
                        if (!BeyRegistry.ITEMSLAYER.isEmpty()) {
                            return BeyRegistry.ITEMSLAYER.get(partCount).getName().getString();
                        }
                        return "";
                    case 1:
                        if (!BeyRegistry.ITEMSDISCLIST.isEmpty()) {
                            return BeyRegistry.ITEMSDISCLIST.get(partCount).getName().getString();
                        }
                        return "";
                    case 2:
                        if (!BeyRegistry.ITEMSDRIVER.isEmpty()) {
                            return BeyRegistry.ITEMSDRIVER.get(partCount).getName().getString();
                        }
                        return "";
                    case 3:
                        if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                            return BeyRegistry.ITEMSFRAME.get(partCount).getName().getString();
                        }
                        return "";
                    case 4:
                        if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
                            return BeyRegistry.ITEMSGTCHIP.get(partCount).getName().getString();
                        }
                        return "";
                    case 5:
                        if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
                            return BeyRegistry.ITEMSGTWEIGHT.get(partCount).getName().getString();
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
                if (!BeyRegistry.ITEMSFRAME.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSFRAME.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
            case 4:
                if (!BeyRegistry.ITEMSGTCHIP.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTCHIP.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
            case 5:
                if (!BeyRegistry.ITEMSGTWEIGHT.isEmpty()) {
                    ItemStack stack = new ItemStack(BeyRegistry.ITEMSGTWEIGHT.get(partCount));
                    renderItemIntoGUI(stack, relX + xSize / 2, relY + ySize / 2 - 15, Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, (World) null, (LivingEntity) null));
                }
                break;
        }
        this.font.drawString(partName.getFormattedText(), relX, relY + 25, 50);
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
