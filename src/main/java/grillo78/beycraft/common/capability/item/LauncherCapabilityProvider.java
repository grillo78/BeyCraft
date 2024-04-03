package grillo78.beycraft.common.capability.item;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class LauncherCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable {

    @CapabilityInject(ILauncher.class)
    public static final Capability<ILauncher> LAUNCHER_CAPABILITY = null;
    private final LazyOptional<ILauncher> inventory;

    public LauncherCapabilityProvider() {
        this.inventory = LazyOptional.of(() -> new Launcher());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == LAUNCHER_CAPABILITY) {
            return inventory.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return inventory.orElse(null).serializeNBT();
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        try{
            inventory.orElse(null).deserializeNBT((CompoundNBT) nbt);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
