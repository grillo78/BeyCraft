package ga.beycraft.abilities;

import java.util.function.Supplier;

public class AbilityType {
    private final Supplier<Ability> abilitySupplier;

    public AbilityType(Supplier<Ability> abilitySupplier) {
        this.abilitySupplier = abilitySupplier;
    }


}
