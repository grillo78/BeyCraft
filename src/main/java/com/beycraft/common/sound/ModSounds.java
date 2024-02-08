package com.beycraft.common.sound;

import com.beycraft.Beycraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Beycraft.MOD_ID);

    public static final SoundEvent HIT = register("hit", new SoundEvent(new ResourceLocation(Beycraft.MOD_ID, "hit")));
    public static final SoundEvent FLOOR_FRICTION = register("floor_friction", new SoundEvent(new ResourceLocation(Beycraft.MOD_ID, "floor_friction")));
    public static final SoundEvent TABLET_WINK = register("tablet_wink", new SoundEvent(new ResourceLocation(Beycraft.MOD_ID, "tablet_wink")));

    private static <T extends SoundEvent> T register(String name, T sound) {
        SOUNDS.register(name, () -> sound);
        return sound;
    }
}
