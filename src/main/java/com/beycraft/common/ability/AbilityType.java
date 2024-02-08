package com.beycraft.common.ability;

import com.beycraft.Beycraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class AbilityType extends ForgeRegistryEntry<AbilityType> {
    public static IForgeRegistry<AbilityType> ABILITY_TYPES;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterNewRegistries(RegistryEvent.NewRegistry e) {
        ABILITY_TYPES = new RegistryBuilder<AbilityType>().setName(new ResourceLocation(Beycraft.MOD_ID, "abilitiy_types")).setType(AbilityType.class).setIDRange(0, 2048).create();
    }
    private final Supplier<Ability> abilitySupplier;

    public AbilityType(Supplier<Ability> abilitySupplier) {
        this.abilitySupplier = abilitySupplier;
    }

    public Ability getAbility(){
        return abilitySupplier.get();
    }

}
