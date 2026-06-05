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
        if (!BeypartsReloadListener.BURST_LAYERS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.BURST_LAYERS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.BURST_LAYERS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DISCS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_discs", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_discs")).icon(() -> {
        ItemStack stack = ModItems.BURST_DISC.get().getDefaultInstance();
        if (!BeypartsReloadListener.BURST_DISCS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.BURST_DISCS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.BURST_DISCS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DRIVERS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_drivers", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_drivers")).icon(() -> {
        ItemStack stack = ModItems.BURST_DRIVER.get().getDefaultInstance();
        if (!BeypartsReloadListener.BURST_DRIVERS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.BURST_DRIVERS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.BURST_DRIVERS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

}
