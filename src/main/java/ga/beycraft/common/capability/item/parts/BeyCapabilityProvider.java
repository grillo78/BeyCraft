package ga.beycraft.common.capability.item.parts;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BeyCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable {

    private final LazyOptional<IItemHandler> inventory;

    public BeyCapabilityProvider(IItemHandler inventory) {
        this.inventory = LazyOptional.of(() -> inventory);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        INBT nbt = inventory.map(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(items, null))
                .orElseGet(CompoundNBT::new);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        inventory.ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, nbt));
    }
}
