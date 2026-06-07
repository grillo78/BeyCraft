package grillo78.beycraft.entities;

import grillo78.beycraft.Beycraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Beycraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Beyblade>> BEYBLADE =
            ENTITY_TYPES.register("beyblade", () ->
                    EntityType.Builder.of(Beyblade::new, MobCategory.MISC).sized(0.25f, 0.2f).noSummon()
                            .build(ResourceLocation.fromNamespaceAndPath(Beycraft.MOD_ID, "beyblade").toString()));
}
