package ga.beycraft.network.message;

import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.items.ItemBeyLayer;
import ga.beycraft.items.ItemBeyLayerDual;
import ga.beycraft.items.ItemBeyLayerGTDual;
import ga.beycraft.items.ItemBeyLayerGTDualNoWeight;
import ga.beycraft.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHandSpin implements IMessage<MessageHandSpin> {

    private static float HAND_SPIN_SPEED = 1.5F;

    public MessageHandSpin() {
    }

    @Override
    public void encode(MessageHandSpin message, PacketBuffer buffer) {
    }

    @Override
    public MessageHandSpin decode(PacketBuffer buffer) {
        return new MessageHandSpin();
    }

    @Override
    public void handle(MessageHandSpin message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity player = supplier.get().getSender();
            ItemStack bey;
            if (player.getMainHandItem().getItem() instanceof ItemBeyLayer) {
                bey = player.getMainHandItem();
                if (((ItemBeyLayer) bey.getItem()).isBeyAssembled(bey)) {
                    spawnBey(player, bey.copy(), player.getMainArm());
                    bey.shrink(1);
                }
            }

            if (player.getOffhandItem().getItem() instanceof ItemBeyLayer) {
                bey = player.getOffhandItem();
                if (((ItemBeyLayer) bey.getItem()).isBeyAssembled(bey)) {
                    spawnBey(player, bey.copy(), player.getMainArm().getOpposite());
                    bey.shrink(1);
                }
            }
            PacketHandler.instance.sendTo(new MessageCloseGUI(),player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        });
        supplier.get().setPacketHandled(true);
    }

    private void spawnBey(ServerPlayerEntity player, ItemStack bey, HandSide handSide) {
        World world = player.level;
        player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i -> {
            bey.getTag().putBoolean("isEntity", true);
            ItemBeyLayer layerItem = (ItemBeyLayer) bey.getItem();
            EntityBey entity = new EntityBey(BeyCraftRegistry.BEY_ENTITY_TYPE, world,
                    bey,
                    layerItem instanceof ItemBeyLayerGTDual || layerItem instanceof ItemBeyLayerDual || layerItem instanceof ItemBeyLayerGTDualNoWeight ? (handSide == HandSide.RIGHT ? 1 : -1) : layerItem.getRotationDirection(), player.getName().getString(), i.getBladerLevel());
            entity.moveTo(player.position().x + player.getLookAngle().x,
                    player.position().y + 1 + player.getLookAngle().y,
                    player.position().z + player.getLookAngle().z, player.yRot, 0);
            entity.setRotationSpeed(HAND_SPIN_SPEED);

            float radius = 1.6F * HAND_SPIN_SPEED / 15;
            entity.setRadius(radius);
            entity.yHeadRot = entity.yRot;
            entity.yBodyRot = entity.yRot;
            world.addFreshEntity(entity);
        });
    }
}
