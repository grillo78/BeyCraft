package ga.beycraft.common.container;

import ga.beycraft.Beycraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Beycraft.MOD_ID);

    public static final ContainerType<LayerContainer> LAYER = register("layer", IForgeContainerType.create(((windowId, inv, data) -> new LayerContainer(ModContainers.LAYER, windowId, ItemStack.EMPTY, inv))));

    private static <T extends ContainerType<?>> T register(String name, T containerType) {
        CONTAINERS.register(name, () -> containerType);
        return containerType;
    }
}
