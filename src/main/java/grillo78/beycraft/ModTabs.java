package grillo78.beycraft;

import grillo78.beycraft.data.parts.Beypart;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Beycraft.MOD_ID);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LAYERS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_layers", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_layers")).icon(() -> {
        ItemStack stack = ModItems.BURST_LAYER.get().getDefaultInstance();
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.BURST_LAYERS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

}
