package com.grillo78.BeyCraft.util;

import com.grillo78.BeyCraft.BeyCraft;
import com.grillo78.BeyCraft.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler {

	public static SoundEvent BEY_HIT;
	
	public static void init() {
		BEY_HIT = register("bey.hit");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		SoundEvent e = new SoundEvent(location);
		
		e.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(e);
		BeyCraft.logger.info("Register sound: " + name);
		return e;
	}
	
}
