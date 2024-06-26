package grillo78.beycraft.network.message;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.common.capability.Animation;
import grillo78.beycraft.client.entity.animator.BasicLaunchAnimator;
import grillo78.beycraft.client.entity.animator.NightmareShootAnimator;
import grillo78.beycraft.common.capability.entity.Blader;
import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageApplyAnimation implements IMessage<MessageApplyAnimation> {

    private UUID playerID;
    private Hand hand;

    public MessageApplyAnimation() {
    }

    public MessageApplyAnimation(UUID playerID, Hand hand) {
        this.playerID = playerID;
        this.hand = hand;
    }

    @Override
    public void encode(MessageApplyAnimation message, PacketBuffer buffer) {
        buffer.writeUUID(message.playerID);
        buffer.writeEnum(message.hand);
    }

    @Override
    public MessageApplyAnimation decode(PacketBuffer buffer) {
        return new MessageApplyAnimation(buffer.readUUID(), buffer.readEnum(Hand.class));
    }

    @Override
    public void handle(MessageApplyAnimation message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(message.playerID);
            if(player == Minecraft.getInstance().player)
                player.yBodyRot = player.yHeadRot;
            Blader blader = player.getCapability(BladerCapabilityProvider.BLADER_CAP).orElse(null);

            Animation.get(player).setAnimator(getAnimator(blader.getAnimatorID(), player, message));
        });
        supplier.get().setPacketHandled(true);
    }

    private Animator getAnimator(int id, PlayerEntity player, MessageApplyAnimation message) {
        Animator animator = null;
        HandSide hand = message.hand == Hand.MAIN_HAND? player.getMainArm():player.getMainArm().getOpposite();
        switch (id){
            case 0:
                animator = new BasicLaunchAnimator(hand);
                break;
            case 1:
                animator = new NightmareShootAnimator(hand);
                break;
            case 2:
                animator = new NightmareShootAnimator(hand);
                break;
        }
        System.out.println("Animator id: " + animator);
        return animator;
    }
}
