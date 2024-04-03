package grillo78.beycraft.network.message;

import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import grillo78.beycraft.common.launch.LaunchTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHandSpinning implements IMessage<MessageHandSpinning>{
    @Override
    public void encode(MessageHandSpinning message, PacketBuffer buffer) {

    }

    @Override
    public MessageHandSpinning decode(PacketBuffer buffer) {
        return new MessageHandSpinning();
    }

    @Override
    public void handle(MessageHandSpinning message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            supplier.get().getSender().getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader -> {
                if(!blader.isLaunching()){
                    LaunchTypes.HAND_LAUNCH_TYPE.generateLaunch().launchBeyblade(supplier.get().getSender().getMainHandItem(), supplier.get().getSender().level, supplier.get().getSender(), Hand.MAIN_HAND);
                }
            });
        });
        supplier.get().setPacketHandled(true);
    }
}
