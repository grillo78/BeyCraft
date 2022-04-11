package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class BeyPartItem extends Item {

    private static final List<BeyPartItem> PARTS = new ArrayList<>();

    private AbilityType primaryAbilityType;
    private AbilityType secondaryAbilityType;
    private BeyTypes type;
    private String name;
    private String displayName;

    public static List<BeyPartItem> getParts() {
        return PARTS;
    }

    public BeyPartItem(String name, String displayName, BeyTypes type, AbilityType primaryAbility, AbilityType secondaryAbility, ItemGroup tab, Properties properties) {
        super(properties.tab(tab).stacksTo(1));
        this.primaryAbilityType = primaryAbility;
        this.secondaryAbilityType = secondaryAbility;
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        PARTS.add(this);
    }

    public String getName() {
        return name;
    }
}
