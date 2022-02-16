package ga.beycraft.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCloseGUI implements IMessage<MessageCloseGUI> {

    public MessageCloseGUI() {
    }

    @Override
    public void encode(MessageCloseGUI message, PacketBuffer buffer) {
    }

    @Override
    public MessageCloseGUI decode(PacketBuffer buffer) {
        return new MessageCloseGUI();
    }

    @Override
    public void handle(MessageCloseGUI message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Minecraft.getInstance().screen = null;
        });
        supplier.get().setPacketHandled(true);
    }
}
