package grillo78.beycraft;

import grillo78.beycraft.blocks.ModBlocks;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.data.parts.LaunchersReloadListener;
import grillo78.beycraft.items.ModItems;
import grillo78.beycraft.items.components.ModDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Beycraft.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LAUNCHERS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_launchers", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_launchers")).icon(() -> {
        ItemStack stack = ModItems.BURST_LAYER.get().getDefaultInstance();
        if (!BeypartsReloadListener.BURST_LAYERS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.BURST_LAYERS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        LaunchersReloadListener.LAUNCHERS.forEach(((id, launcher) -> {
            ItemStack stack = ModItems.LAUNCHER.get().getDefaultInstance();
            stack.set(ModDataComponents.LAUNCHER, id);
            output.accept(stack);
        }));
    }).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_blocks", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_blocks")).icon(() -> {
        return new ItemStack(ModBlocks.STADIUM.get());
    }).displayItems((parameters, output) -> {
            output.accept(ModBlocks.STADIUM.get());
    }).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURST_LAYERS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_layers", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_layers")).icon(() -> {
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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURST_DISCS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_discs", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_discs")).icon(() -> {
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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURST_DRIVERS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_burst_drivers", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_burst_drivers")).icon(() -> {
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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> METAL_FACEBOLTS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_metal_facebolts", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_metal_facebolts")).icon(() -> {
        ItemStack stack = ModItems.METAL_FACEBOLT.get().getDefaultInstance();
        if (!BeypartsReloadListener.METAL_FACEBOLTS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.METAL_FACEBOLTS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.METAL_FACEBOLTS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> METAL_ENERGY_RINGS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_metal_energy_rings", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_metal_energy_rings")).icon(() -> {
        ItemStack stack = ModItems.METAL_ENERGY_RING.get().getDefaultInstance();
        if (!BeypartsReloadListener.METAL_ENERGY_RINGS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.METAL_ENERGY_RINGS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.METAL_ENERGY_RINGS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> METAL_FUSION_WHEELS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_metal_fusion_wheels", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_metal_fusion_wheels")).icon(() -> {
        ItemStack stack = ModItems.METAL_FUSION_WHEEL.get().getDefaultInstance();
        if (!BeypartsReloadListener.METAL_FUSION_WHEELS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.METAL_FUSION_WHEELS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.METAL_FUSION_WHEELS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> METAL_SPIN_TRACKS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_metal_spin_tracks", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_metal_spin_tracks")).icon(() -> {
        ItemStack stack = ModItems.METAL_SPIN_TRACK.get().getDefaultInstance();
        if (!BeypartsReloadListener.METAL_SPIN_TRACKS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.METAL_SPIN_TRACKS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.METAL_SPIN_TRACKS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> METAL_PERFORMANCE_TIPS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_metal_performance_tips", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_metal_performance_tips")).icon(() -> {
        ItemStack stack = ModItems.METAL_PERFORMANCE_TIP.get().getDefaultInstance();
        if (!BeypartsReloadListener.METAL_PERFORMANCE_TIPS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.METAL_PERFORMANCE_TIPS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.METAL_PERFORMANCE_TIPS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> X_BLADES = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_x_blades", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_x_blades")).icon(() -> {
        ItemStack stack = ModItems.X_BLADE.get().getDefaultInstance();
        if (!BeypartsReloadListener.X_BLADES.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.X_BLADES.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.X_BLADES.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> X_RATCHETS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_x_ratchets", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_x_ratchets")).icon(() -> {
        ItemStack stack = ModItems.X_RATCHET.get().getDefaultInstance();
        if (!BeypartsReloadListener.X_RATCHETS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.X_RATCHETS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.X_RATCHETS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> X_BITS = CREATIVE_MODE_TABS.register(Beycraft.MOD_ID + "_x_bits", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Beycraft.MOD_ID + "_x_bits")).icon(() -> {
        ItemStack stack = ModItems.X_BIT.get().getDefaultInstance();
        if (!BeypartsReloadListener.X_BITS.isEmpty())
            stack.set(ModDataComponents.BEYPART, BeypartsReloadListener.X_BITS.get(0).getId());
        return stack;
    }).displayItems((parameters, output) -> {
        BeypartsReloadListener.X_BITS.forEach(((beypart) -> {
            ItemStack stack = beypart.getPartItem().getDefaultInstance();
            stack.set(ModDataComponents.BEYPART, beypart.getId());
            output.accept(stack);
        }));
    }).build());
}
