package grillo78.beycraft.data.parts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.ModTabs;
import grillo78.beycraft.data.parts.burst.Layer;
import grillo78.beycraft.network.RegisterModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeypartsReloadListener extends SimpleJsonResourceReloadListener {
    public static Map<ResourceLocation, Beypart> BEYPARTS = new HashMap<>();
    public static List<Beypart> BURST_LAYERS = new ArrayList<>();
    private static final Gson GSON = new Gson();

    public BeypartsReloadListener() {
        super(GSON, "beyparts");
    }

    protected void apply(Map<ResourceLocation, JsonElement> parts, ResourceManager resourceManager, ProfilerFiller profiler) {
        BEYPARTS.clear();

        parts.forEach((id, jsonElement) -> {
            try {
                JsonObject object = jsonElement.getAsJsonObject();
                Beypart part;
                switch (object.get("type").getAsString()) {
                    case "layer":
                        part = new Layer(id, object);
                        BURST_LAYERS.add(part);
                        break;
                    default:
                        throw new Exception("Unkown beypart type.");
                }
                BEYPARTS.put(id, part);
            } catch (Exception e) {
                Beycraft.LOGGER.debug("Error parsing \"{}\" with error: {}", id, e.getMessage());
            }
        });
//        if (ServerLifecycleHooks.getCurrentServer() != null)
//            PacketDistributor.sendToAllPlayers(new RegisterModel());
    }

}
