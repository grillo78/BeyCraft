package ga.beycraft.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.Reference;
import ga.beycraft.util.RankingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Properties;

public class LoginScreen extends Screen {

    private TextFieldWidget usernameField;
    private TextFieldWidget passwordField;
    private Properties config;
    private File config_file;
    private Button loginButton;
    private static ResourceLocation BEYCRAFT_LOGO = new ResourceLocation(Reference.MOD_ID, "textures/logo.png");

    public LoginScreen(ITextComponent p_i51108_1_, Properties config, File configFile) {
        super(p_i51108_1_);
        this.config = config;
        this.config_file = configFile;
    }

    @Override
    protected void init() {
        int relX = this.width / 2;
        int relY = this.height / 2;
        usernameField = new TextFieldWidget(font, relX - 50, relY + 15, 95, 10,
                new StringTextComponent(""));
        usernameField.setMaxLength(Integer.MAX_VALUE);
        passwordField = new PasswordTextFieldWidget(font, relX - 50, relY + 36, 95, 10,
                new StringTextComponent(""));
        passwordField.setMaxLength(Integer.MAX_VALUE);
        children.add(usernameField);
        children.add(passwordField);
        loginButton = new Button(relX + 55, relY + 15, 50, 20, new TranslationTextComponent("gui.done"), (Button) -> {
            String token = RankingUtil.getToken(usernameField.getValue(), passwordField.getValue());
            if (token != null) {
                RankingUtil.storeToken(token);
                Minecraft.getInstance().setScreen(null);
            }
        });
        this.addButton(loginButton);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        minecraft.getTextureManager().bind(BEYCRAFT_LOGO);
        int logoWidth = 256;
        int logoHeight = 50;
        this.blit(matrixStack, width / 2 - logoWidth / 2, 30, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int relX = this.width / 2;
        int relY = this.height / 2;
        font.drawShadow(matrixStack, "Username:", relX - 101, relY + 15, 16777215);
        font.drawShadow(matrixStack, "Password:", relX - 101, relY + 36, 16777215);
        usernameField.render(matrixStack, mouseX, mouseY, partialTicks);
        passwordField.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private class PasswordTextFieldWidget extends TextFieldWidget {

        public PasswordTextFieldWidget(FontRenderer p_i232260_1_, int p_i232260_2_, int p_i232260_3_, int p_i232260_4_, int p_i232260_5_, ITextComponent p_i232260_6_) {
            super(p_i232260_1_, p_i232260_2_, p_i232260_3_, p_i232260_4_, p_i232260_5_, p_i232260_6_);
        }

        @Override
        public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
            if (this.isVisible()) {
                if (this.bordered) {
                    int i = this.isFocused() ? -1 : -6250336;
                    fill(p_230431_1_, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, i);
                    fill(p_230431_1_, this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
                }

                int i2 = this.isEditable ? this.textColor : this.textColorUneditable;
                int j = this.cursorPos - this.displayPos;
                int k = this.highlightPos - this.displayPos;
                String s = this.font.plainSubstrByWidth(StringUtils.repeat("*", this.getValue().length()), this.getInnerWidth());
                boolean flag = j >= 0 && j <= s.length();
                boolean flag1 = this.isFocused() && this.frame / 6 % 2 == 0 && flag;
                int l = this.bordered ? this.x + 4 : this.x;
                int i1 = this.bordered ? this.y + (this.height - 8) / 2 : this.y;
                int j1 = l;
                if (k > s.length()) {
                    k = s.length();
                }

                if (!s.isEmpty()) {
                    String s1 = flag ? s.substring(0, j) : s;
                    j1 = this.font.drawShadow(p_230431_1_, this.formatter.apply(s1, this.displayPos), (float) l, (float) i1, i2);
                }

                boolean flag2 = this.cursorPos < this.getValue().length() || this.getValue().length() >= this.getMaxLength();
                int k1 = j1;
                if (!flag) {
                    k1 = j > 0 ? l + this.width : l;
                } else if (flag2) {
                    k1 = j1 - 1;
                    --j1;
                }

                if (!s.isEmpty() && flag && j < s.length()) {
                    this.font.drawShadow(p_230431_1_, this.formatter.apply(s.substring(j), this.cursorPos), (float) j1, (float) i1, i2);
                }

                if (!flag2 && this.suggestion != null) {
                    this.font.drawShadow(p_230431_1_, this.suggestion, (float) (k1 - 1), (float) i1, -8355712);
                }

                if (flag1) {
                    if (flag2) {
                        AbstractGui.fill(p_230431_1_, k1, i1 - 1, k1 + 1, i1 + 1 + 9, -3092272);
                    } else {
                        this.font.drawShadow(p_230431_1_, "_", (float) k1, (float) i1, i2);
                    }
                }

                if (k != j) {
                    int l1 = l + this.font.width(s.substring(0, k));
                    this.renderHighlight(j1, i1 - 1, l1 - 1, i1 + 1 + 9);
                }

            }
        }
    }
}
