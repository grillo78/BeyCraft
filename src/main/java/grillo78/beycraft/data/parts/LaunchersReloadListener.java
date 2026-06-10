package grillo78.beycraft.data.parts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.data.parts.burst.*;
import grillo78.beycraft.data.parts.metal.*;
import grillo78.beycraft.data.parts.x.Bit;
import grillo78.beycraft.data.parts.x.Blade;
import grillo78.beycraft.data.parts.x.Ratchet;
import grillo78.beycraft.network.SyncBeyparts;
import grillo78.beycraft.network.SyncLaunchers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchersReloadListener extends SimpleJsonResourceReloadListener {
    public static Map<ResourceLocation, Launcher> LAUNCHERS = new HashMap<>();
    private static final Gson GSON = new Gson();

    public LaunchersReloadListener() {
        super(GSON, "bey_launchers");
    }

    public static void loadFromCompoundList(List<CompoundTag> parts) {
        LAUNCHERS.clear();

        parts.forEach((compound) -> {
            ResourceLocation partId = ResourceLocation.parse(compound.getString("launcherId"));
            try {
                Launcher launcher = new Launcher(partId, compound);
                LAUNCHERS.put(partId, launcher);
            } catch (Exception e) {
                Beycraft.LOGGER.debug("Error parsing \"{}\" with error: {}", partId, e.getMessage());
            }
        });
    }

    protected void apply(Map<ResourceLocation, JsonElement> parts, ResourceManager resourceManager, ProfilerFiller profiler) {
        LAUNCHERS.clear();
        List<CompoundTag> serializedParts = new ArrayList<>();

        parts.forEach((partId, jsonElement) -> {
            try {
                JsonObject object = jsonElement.getAsJsonObject();
                Launcher launcher = new Launcher(partId, object);
                addSerializedPart(launcher,serializedParts);
                LAUNCHERS.put(partId, launcher);
            } catch (Exception e) {
                Beycraft.LOGGER.debug("Error parsing \"{}\" with error: {}", partId, e.getMessage());
            }
        });
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToAllPlayers(new SyncLaunchers(serializedParts));
        }
    }

    public static void addSerializedPart(Launcher part, List<CompoundTag> serializedParts) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            CompoundTag compound = part.toCompound();
            serializedParts.add(compound);
        }
    }

}
