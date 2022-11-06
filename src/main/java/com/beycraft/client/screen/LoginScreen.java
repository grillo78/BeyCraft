package com.beycraft.client.screen;

import com.beycraft.Beycraft;
import com.beycraft.client.screen.widget.PasswordTextFieldWidget;
import com.beycraft.utils.ClientUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class LoginScreen extends Screen {

    private TextFieldWidget usernameField;
    private TextFieldWidget passwordField;
    private Button loginButton;
    private static ResourceLocation BEYCRAFT_LOGO = new ResourceLocation(Beycraft.MOD_ID, "textures/logo.png");

    public LoginScreen(ITextComponent name) {
        super(name);
    }

    @Override
    protected void init() {
        super.init();

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
            if (ClientUtils.RankingUtils.getToken(usernameField.getValue(), passwordField.getValue()))
                Minecraft.getInstance().setScreen(null);
        });
        this.addButton(loginButton);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        minecraft.getTextureManager().bind(BEYCRAFT_LOGO);
        int logoWidth = 1256/6;
        int logoHeight = 501/6;
        this.blit(matrixStack, width / 2 - logoWidth / 2, 30, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int relX = this.width / 2;
        int relY = this.height / 2;
        font.drawShadow(matrixStack, "Username:", relX - 101, relY + 15, 16777215);
        font.drawShadow(matrixStack, "Password:", relX - 101, relY + 36, 16777215);
        usernameField.render(matrixStack, mouseX, mouseY, partialTicks);
        passwordField.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
