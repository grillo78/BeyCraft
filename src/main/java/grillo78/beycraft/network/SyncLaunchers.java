package grillo78.beycraft.network;

import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.data.parts.BeypartsReloadListener;
import grillo78.beycraft.data.parts.LaunchersReloadListener;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SyncLaunchers(List<CompoundTag> parts) implements CustomPacketPayload {

    public static final Type<SyncLaunchers> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Beycraft.MOD_ID, "sync_launchers"));
    public static final StreamCodec<ByteBuf, SyncLaunchers> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG.apply(ByteBufCodecs.list()),
            SyncLaunchers::parts,
            SyncLaunchers::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncLaunchers data, final IPayloadContext context) {
        LaunchersReloadListener.loadFromCompoundList(data.parts);
        CreativeModeTabs.tryRebuildTabContents(FeatureFlags.DEFAULT_FLAGS, true, Minecraft.getInstance().player.registryAccess());
        MeshRegistry.registerAll();
    }
}
