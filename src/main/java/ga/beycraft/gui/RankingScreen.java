package ga.beycraft.gui;

import com.google.gson.JsonArray;
import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.Reference;
import ga.beycraft.ranking.RankingBlader;
import ga.beycraft.util.RankingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RankingScreen extends Screen {

    private List ranking;
    private static ResourceLocation BEYCRAFT_LOGO = new ResourceLocation(Reference.MOD_ID, "textures/logo.png");

    public RankingScreen(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }

    @Override
    protected void init() {
        super.init();
        this.ranking = new List();
        this.children.add(ranking);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        minecraft.getTextureManager().bind(BEYCRAFT_LOGO);
        int logoWidth = 256;
        int logoHeight = 40;
        this.blit(matrixStack, width / 2 - logoWidth / 2, 10, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
        ranking.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private class List extends ExtendedList<RankingBlader>{

        public List() {
            super(Minecraft.getInstance(), RankingScreen.this.width, RankingScreen.this.height/3, 55, RankingScreen.this.height - 30, 18);
            getRanking();
            setRenderBackground(false);
            setRenderTopAndBottom(false);
        }

        protected boolean isFocused() {
            return RankingScreen.this.getFocused() == this;
        }

        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 20;
        }

        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        private void getRanking(){
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonArray rankingArray = RankingUtil.getRanking();
                    rankingArray.forEach(h->{
                        JsonArray blader = h.getAsJsonArray();
                        addEntry(new RankingBlader(blader.get(0).getAsString(), blader.get(1).getAsInt(),RankingScreen.this));
                    });
                }
            });
            t1.start();
        }
    }
}
