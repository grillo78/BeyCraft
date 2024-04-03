package grillo78.beycraft.common.container;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.utils.Direction;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Beycraft.MOD_ID);

    public static final ContainerType<LayerContainer> LAYER = register("layer", IForgeContainerType.create(((windowId, inv, data) -> new LayerContainer(ModContainers.LAYER, windowId, ItemStack.EMPTY, inv))));
    public static final ContainerType<GTLayerContainer> GT_LAYER = register("gt_layer", IForgeContainerType.create(((windowId, inv, data) -> new GTLayerContainer(ModContainers.GT_LAYER, windowId, ItemStack.EMPTY, inv))));
    public static final ContainerType<LayerContainer> CLEAR_WHEEL = register("clear_wheel", IForgeContainerType.create(((windowId, inv, data) -> new ClearWheelContainer(ModContainers.CLEAR_WHEEL, windowId, ItemStack.EMPTY, inv))));
    public static final ContainerType<LauncherContainer> RIGHT_LAUNCHER = register("right_launcher", IForgeContainerType.create(((windowId, inv, data) -> new LauncherContainer(ModContainers.RIGHT_LAUNCHER, windowId, ItemStack.EMPTY, inv, true, Direction.RIGHT))));
    public static final ContainerType<LauncherContainer> LEFT_LAUNCHER = register("left_launcher", IForgeContainerType.create(((windowId, inv, data) -> new LauncherContainer(ModContainers.LEFT_LAUNCHER, windowId, ItemStack.EMPTY, inv, true, Direction.LEFT))));
    public static final ContainerType<LauncherContainer> DUAL_LAUNCHER = register("dual_launcher", IForgeContainerType.create(((windowId, inv, data) -> new LauncherContainer(ModContainers.DUAL_LAUNCHER, windowId, ItemStack.EMPTY, true, inv))));
    public static final ContainerType<DiscFrameContainer> DISC_FRAME = register("disc_frame", IForgeContainerType.create(((windowId, inv, data) -> new DiscFrameContainer(ModContainers.DUAL_LAUNCHER, windowId, ItemStack.EMPTY, inv))));
    public static final ContainerType<BeltContainer> BELT_CONTAINER = register("belt", IForgeContainerType.create(((windowId, inv, data) -> new BeltContainer(ModContainers.BELT_CONTAINER, windowId, ItemStack.EMPTY, inv))));

    private static <T extends ContainerType<?>> T register(String name, T containerType) {
        CONTAINERS.register(name, () -> containerType);
        return containerType;
    }
}
