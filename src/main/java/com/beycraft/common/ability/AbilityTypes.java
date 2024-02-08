package com.beycraft.common.ability;

import com.beycraft.Beycraft;
import net.minecraftforge.registries.DeferredRegister;

public class AbilityTypes {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(AbilityType.class, Beycraft.MOD_ID);

    public static AbilityType ABSORB = register("absorb", new AbilityType(AbsorbAbility::new));

    private static <T extends AbilityType> T register(String name, T abilityType) {
        ABILITY_TYPES.register(name, () -> abilityType);
        return abilityType;
    }
}
