package com.beycraft.client.screen.widget;

import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.TranslationTextComponent;

public class AnimationsList extends ExtendedList<AnimationsList.AnimationsListEntry> {

    private TranslationTextComponent name = new TranslationTextComponent("title.launches");

    public AnimationsList() {
        super(Minecraft.getInstance(), Minecraft.getInstance().screen.width/2, Minecraft.getInstance().screen.height, 30, Minecraft.getInstance().screen.height - 30, 18);
        this.x0 = Minecraft.getInstance().screen.width/2;
        this.x1 = Minecraft.getInstance().screen.width;
        addEntry(new AnimationsListEntry(0,"basic_shoot"));
        addEntry(new AnimationsListEntry(1,"nightmare_shoot"));
        this.getEntry(Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null).getAnimatorID()).select();
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        Minecraft.getInstance().font.drawShadow(pMatrixStack, name, 3*Minecraft.getInstance().screen.width/4-Minecraft.getInstance().font.width(name)/2, 15, 16777215);
    }
    protected int getScrollbarPosition() {
        return x0 + this.width / 2 + 124;
    }

    public class AnimationsListEntry extends AbstractList.AbstractListEntry<AnimationsListEntry> {

        private int id;
        private TranslationTextComponent name;

        public AnimationsListEntry(int id, String name) {
            this.id = id;
            this.name = new TranslationTextComponent("animation." + name);
        }

        @Override
        public void render(MatrixStack pMatrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks) {
            Minecraft.getInstance().font.drawShadow(pMatrixStack, name, pLeft+1, pTop+3, 16777215);
        }

        public int getId() {
            return id;
        }

        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            if (pButton == 0 && pMouseX > x0 && pMouseX<x1) {
                this.select();
                return true;
            } else {
                return false;
            }
        }

        private void select() {
            AnimationsList.this.setSelected(this);
        }
    }
}
