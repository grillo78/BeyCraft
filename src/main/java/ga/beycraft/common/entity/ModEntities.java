package ga.beycraft.common.entity;

import ga.beycraft.Beycraft;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Beycraft.MOD_ID);

    public static final EntityType<BeybladeEntity> BEYBLADE = register("beyblade", EntityType.Builder
            .<BeybladeEntity>of(BeybladeEntity::new, EntityClassification.MONSTER).sized(0.1F, 0.1F)
            .build(Beycraft.MOD_ID + ":beyblade"));

    private static <T extends EntityType<?>> T register(String name, T containerType) {
        ENTITIES.register(name, () -> containerType);
        return containerType;
    }
}
