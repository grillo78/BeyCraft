package com.beycraft.client.screen.widget;

import com.beycraft.common.capability.entity.Blader;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import com.beycraft.common.launch.LaunchType;
import com.beycraft.common.launch.LaunchTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.TranslationTextComponent;

public class LaunchesList extends ExtendedList<LaunchesList.LaunchesListEntry> {

    private TranslationTextComponent name = new TranslationTextComponent("title.launches");

    public LaunchesList() {
        super(Minecraft.getInstance(), Minecraft.getInstance().screen.width/2, Minecraft.getInstance().screen.height, 30, Minecraft.getInstance().screen.height - 30, 18);
        LaunchType.LAUNCH_TYPES.getEntries().forEach(type->{
            if(type != LaunchTypes.HAND_LAUNCH_TYPE){
                LaunchesListEntry entry = new LaunchesListEntry(type.getValue());
                addEntry(entry);
                if (Minecraft.getInstance().player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null).getLaunchType() == type.getValue())
                    entry.select();
            }
        });
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        Minecraft.getInstance().font.drawShadow(pMatrixStack, name, Minecraft.getInstance().screen.width/4-Minecraft.getInstance().font.width(name)/2, 15, 16777215);
    }

    public class LaunchesListEntry extends ExtendedList.AbstractListEntry<LaunchesListEntry> {

        private LaunchType launch;
        private TranslationTextComponent name;

        public LaunchesListEntry(LaunchType launch) {
            this.launch = launch;
            name = new TranslationTextComponent("launch." + launch.getRegistryName().getPath());
        }

        @Override
        public void render(MatrixStack pMatrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks) {
            Minecraft.getInstance().font.drawShadow(pMatrixStack, name, pLeft+1, pTop+3, 16777215);

        }

        public LaunchType getLaunch() {
            return launch;
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
            LaunchesList.this.setSelected(this);
        }
    }
}
