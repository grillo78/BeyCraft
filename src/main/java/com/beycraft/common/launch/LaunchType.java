package com.beycraft.common.launch;

import com.beycraft.Beycraft;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Beycraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LaunchType extends ForgeRegistryEntry<LaunchType> {
    public static IForgeRegistry<LaunchType> LAUNCH_TYPES;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        LAUNCH_TYPES = new RegistryBuilder<LaunchType>().setName(new ResourceLocation(Beycraft.MOD_ID, "launch_types")).setType(LaunchType.class).setIDRange(0, 2048).create();
    }

    private final Supplier<Launch> launchSupplier;
    private int x;
    private int y;
    private int frames;
    private int actualFrame;
    private ResourceLocation requisite;

    @OnlyIn(Dist.CLIENT)
    private RenderMaterial renderMaterial;

    public LaunchType(Supplier<Launch> launchSupplier, int x, int y, ResourceLocation requisite, int frames) {
        this.launchSupplier = launchSupplier;
        this.x = x;
        this.y = y;
        this.requisite = requisite;
        this.frames = frames;
    }

    @OnlyIn(Dist.CLIENT)
    public RenderMaterial getRenderMaterial() {
        return renderMaterial;
    }

    public int getActualFrame() {
        renderMaterial.sprite().cycleFrames();
        return actualFrame;
    }

    public void increaseActualFrame() {
        if (actualFrame < frames-1)
            actualFrame++;
        else
            actualFrame = 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRenderMaterial(RenderMaterial renderMaterial) {
        this.renderMaterial = renderMaterial;
    }

    public ResourceLocation getRequisite() {
        return requisite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Launch generateLaunch() {
        return launchSupplier.get();
    }
}
