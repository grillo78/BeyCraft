package grillo78.beycraft.network.message;

import grillo78.beycraft.common.capability.item.LauncherCapabilityProvider;
import grillo78.beycraft.common.item.LauncherItem;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateLauncherColor implements IMessage<UpdateLauncherColor> {

    private int red;
    private int green;
    private int blue;

    public UpdateLauncherColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public UpdateLauncherColor() {
    }

    @Override
    public void encode(UpdateLauncherColor message, PacketBuffer buffer) {
        buffer.writeInt(message.red);
        buffer.writeInt(message.green);
        buffer.writeInt(message.blue);
    }

    @Override
    public UpdateLauncherColor decode(PacketBuffer buffer) {
        return new UpdateLauncherColor(buffer.readInt(),buffer.readInt(),buffer.readInt());
    }

    @Override
    public void handle(UpdateLauncherColor message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            if(supplier.get().getSender().getMainHandItem().getItem() instanceof LauncherItem){
                supplier.get().getSender().getMainHandItem().getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap->{
                    cap.setColor(message.red, message.green, message.blue);
                });
            }else {
                supplier.get().getSender().getOffhandItem().getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap->{
                    cap.setColor(message.red, message.green, message.blue);
                });
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
