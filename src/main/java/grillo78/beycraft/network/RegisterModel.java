package grillo78.beycraft.network;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.render.MeshRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RegisterModel() implements CustomPacketPayload {

    public static final Type<RegisterModel> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Beycraft.MOD_ID, "register_models"));
    public static final StreamCodec<ByteBuf, RegisterModel> STREAM_CODEC = StreamCodec.of(((buffer, value) -> {}), (byteBuffer)->new RegisterModel());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RegisterModel data, final IPayloadContext context) {
        MeshRegistry.registerAll();
    }
}
