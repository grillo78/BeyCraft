package com.grillo78.beycraft.ranking;

import com.grillo78.beycraft.gui.RankingScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;

public class RankingBlader extends ExtendedList.AbstractListEntry<RankingBlader> {

    private final String username;
    private final int beyPoints;
    private final RankingScreen screen;

    public RankingBlader(String username, int beyPoints, RankingScreen screen) {
        this.username = username;
        this.beyPoints = beyPoints;
        this.screen = screen;
    }

    @Override
    public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
        String s = username + ": " + beyPoints;
        Minecraft.getInstance().font.drawShadow(p_230432_1_, s, (float)(screen.width / 2 - Minecraft.getInstance().font.width(s) / 2), (float)(p_230432_3_ + 1), 16777215, true);
    }
}
