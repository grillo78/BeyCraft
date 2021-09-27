package ga.beycraft.inventory.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class BeyLoggerContainer extends Container {

    /**
     * @param type
     * @param id
     */
    public BeyLoggerContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

}
