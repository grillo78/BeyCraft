package com.beycraft.network.message;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.common.capability.Animation;
import com.beycraft.client.entity.animator.BasicLaunchAnimator;
import com.beycraft.common.capability.entity.Blader;
import com.beycraft.common.capability.entity.BladerCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageApplyAnimation implements IMessage<MessageApplyAnimation> {

    private UUID playerID;

    public MessageApplyAnimation() {
    }

    public MessageApplyAnimation(UUID playerID) {
        this.playerID = playerID;
    }

    @Override
    public void encode(MessageApplyAnimation message, PacketBuffer buffer) {
        buffer.writeUUID(message.playerID);
    }

    @Override
    public MessageApplyAnimation decode(PacketBuffer buffer) {
        return new MessageApplyAnimation(buffer.readUUID());
    }

    @Override
    public void handle(MessageApplyAnimation message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(message.playerID);
            Blader blader = player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);

            Animation.get(player).setAnimator(getAnimator(blader.getAnimatorID()));
        });
        supplier.get().setPacketHandled(true);
    }

    private Animator getAnimator(int id) {
        Animator animator = null;
        switch (id){
            case 0:
                animator = new BasicLaunchAnimator();
                break;
        }
        return animator;
    }
}
