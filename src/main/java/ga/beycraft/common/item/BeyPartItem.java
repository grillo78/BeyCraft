package ga.beycraft.common.item;

import ga.beycraft.BeyTypes;
import ga.beycraft.abilities.AbilityType;
import ga.beycraft.client.item.LayerItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BeyPartItem extends Item {

    private static final List<BeyPartItem> PARTS = new ArrayList<>();

    private final AbilityType primaryAbilityType;
    private final AbilityType secondaryAbilityType;
    private BeyTypes type;
    private String name;
    private String displayName;

    public static List<BeyPartItem> getParts() {
        return PARTS;
    }

    public BeyPartItem(String name, String displayName, BeyTypes type, AbilityType primaryAbility, AbilityType secondaryAbility, CreativeModeTab tab, Properties properties) {
        super(properties.tab(tab).stacksTo(1));
        primaryAbilityType = primaryAbility;
        secondaryAbilityType = secondaryAbility;
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        PARTS.add(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer layerItemRenderer = new LayerItemRenderer();
//            private final BlockEntityWithoutLevelRenderer renderer = new LayerItemRenderer();
//            private final BlockEntityWithoutLevelRenderer renderer = new LayerItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return layerItemRenderer;
            }
        });
    }
}
