package ga.beycraft.client.book_component;

import com.mojang.blaze3d.matrix.MatrixStack;
import ga.beycraft.Beycraft;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class BigItemComponent  implements ICustomComponent {
    private transient int x, y;
    private transient String item = "";

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
        Beycraft.LOGGER.debug("Custom Component Test built at ({}, {}) page {}", componentX, componentY, pageNum);
    }

    @Override
    public void render(MatrixStack matrixStack, IComponentRenderContext iComponentRenderContext, float v, int i, int i1) {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item))),x,y);
    }

    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        Beycraft.LOGGER.debug("Custom Component Test clicked at ({}, {}) button {}", mouseX, mouseY, mouseButton);
        return false;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        item = lookup.apply(IVariable.wrap("First we eat #spaghet#, then we drink #pop#")).asString();
    }
}
