package com.beycraft.common.launch;

import com.beycraft.Beycraft;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import xyz.heroesunited.heroesunited.mixin.client.AccessorModelBakery;

public class LaunchTypes {
    public static final DeferredRegister<LaunchType> LAUNCH_TYPES = DeferredRegister.create(LaunchType.class, Beycraft.MOD_ID);

    public static final LaunchType BASIC_LAUNCH_TYPE = register("basic_launch_type", new LaunchType(Launch::new, 1,50, null,3));
    public static final LaunchType RUSH_LAUNCH_TYPE = register("rush_launch_type", new LaunchType(RushLaunch::new, 25,50, new ResourceLocation(Beycraft.MOD_ID, "basic_launch_type"), 1));
    public static final LaunchType FLASH_LAUNCH_TYPE = register("flash_launch_type", new LaunchType(FlashLaunch::new, 25,75, new ResourceLocation(Beycraft.MOD_ID, "rush_launch_type"), 1));

    private static <T extends LaunchType> T register(String name, T launchType) {
        LAUNCH_TYPES.register(name, () -> launchType);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ()->{
            RenderMaterial renderMaterial = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, new ResourceLocation(Beycraft.MOD_ID, "textures/gui/launch/" + name));
            launchType.setRenderMaterial(renderMaterial);
            AccessorModelBakery.getUnreferencedTex().add(renderMaterial);
        });
        return launchType;
    }
}
