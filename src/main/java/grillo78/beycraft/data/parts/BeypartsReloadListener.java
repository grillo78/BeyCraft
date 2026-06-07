package grillo78.beycraft.data.parts;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.data.parts.burst.*;
import grillo78.beycraft.data.parts.metal.*;
import grillo78.beycraft.network.SyncBeyparts;
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

public class BeypartsReloadListener extends SimpleJsonResourceReloadListener {
    public static Map<ResourceLocation, Beypart> BEYPARTS = new HashMap<>();
    public static List<Beypart> BURST_LAYERS = new ArrayList<>();
    public static List<Beypart> BURST_DISCS = new ArrayList<>();
    public static List<Beypart> BURST_DRIVERS = new ArrayList<>();
    public static List<Beypart> METAL_FACEBOLTS = new ArrayList<>();
    public static List<Beypart> METAL_ENERGY_RINGS = new ArrayList<>();
    public static List<Beypart> METAL_FUSION_WHEELS = new ArrayList<>();
    public static List<Beypart> METAL_SPIN_TRACKS = new ArrayList<>();
    public static List<Beypart> METAL_PERFORMANCE_TIPS = new ArrayList<>();
    private static final Gson GSON = new Gson();

    public BeypartsReloadListener() {
        super(GSON, "beyparts");
    }

    public static void loadFromCompoundList(List<CompoundTag> parts) {
        BEYPARTS.clear();

        parts.forEach((compound) -> {
            ResourceLocation partId = ResourceLocation.parse(compound.getString("partId"));
            try {
                Beypart part;
                switch (compound.getString("type")) {
                    case "layer":
                        part = new Layer(partId, compound);
                        BURST_LAYERS.add(part);
                        break;
                    case "disc":
                        part = new Disc(partId, compound);
                        BURST_DISCS.add(part);
                        break;
                    case "core_disc":
                        part = new CoreDisc(partId, compound);
                        BURST_DISCS.add(part);
                        break;
                    case "frame":
                        part = new Frame(partId, compound);
                        BURST_DISCS.add(part);
                        break;
                    case "driver":
                        part = new Driver(partId, compound);
                        BURST_DRIVERS.add(part);
                        break;
                    case "facebolt":
                        part = new Facebolt(partId, compound);
                        METAL_FACEBOLTS.add(part);
                        break;
                    case "energy_ring":
                        part = new EnergyRing(partId, compound);
                        METAL_ENERGY_RINGS.add(part);
                        break;
                    case "fusion_wheel":
                        part = new FusionWheel(partId, compound);
                        METAL_FUSION_WHEELS.add(part);
                        break;
                    case "spin_track":
                        part = new SpinTrack(partId, compound);
                        METAL_SPIN_TRACKS.add(part);
                        break;
                    case "performance_tip":
                        part = new PerformanceTip(partId, compound);
                        METAL_PERFORMANCE_TIPS.add(part);
                        break;
                    default:
                        throw new Exception("Unkown beypart type.");
                }
                BEYPARTS.put(partId, part);
            } catch (Exception e) {
                Beycraft.LOGGER.debug("Error parsing \"{}\" with error: {}", partId, e.getMessage());
            }
        });
    }

    protected void apply(Map<ResourceLocation, JsonElement> parts, ResourceManager resourceManager, ProfilerFiller profiler) {
        BEYPARTS.clear();
        List<CompoundTag> serializedParts = new ArrayList<>();

        parts.forEach((partId, jsonElement) -> {
            try {
                JsonObject object = jsonElement.getAsJsonObject();
                Beypart part;
                switch (object.get("type").getAsString()) {
                    case "layer":
                        part = new Layer(partId, object);
                        BURST_LAYERS.add(part);
                        break;
                    case "disc":
                        part = new Disc(partId, object);
                        BURST_DISCS.add(part);
                        break;
                    case "core_disc":
                        part = new CoreDisc(partId, object);
                        BURST_DISCS.add(part);
                        break;
                    case "frame":
                        part = new Frame(partId, object);
                        BURST_DISCS.add(part);
                        break;
                    case "driver":
                        part = new Driver(partId, object);
                        BURST_DRIVERS.add(part);
                        break;
                    case "facebolt":
                        part = new Facebolt(partId, object);
                        METAL_FACEBOLTS.add(part);
                        break;
                    case "energy_ring":
                        part = new EnergyRing(partId, object);
                        METAL_ENERGY_RINGS.add(part);
                        break;
                    case "fusion_wheel":
                        part = new FusionWheel(partId, object);
                        METAL_FUSION_WHEELS.add(part);
                        break;
                    case "spin_track":
                        part = new SpinTrack(partId, object);
                        METAL_SPIN_TRACKS.add(part);
                        break;
                    case "performance_tip":
                        part = new PerformanceTip(partId, object);
                        METAL_PERFORMANCE_TIPS.add(part);
                        break;
                    default:
                        throw new Exception("Unkown beypart type.");
                }
                addSerializedPart(part,serializedParts);
                BEYPARTS.put(partId, part);
            } catch (Exception e) {
                Beycraft.LOGGER.debug("Error parsing \"{}\" with error: {}", partId, e.getMessage());
            }
        });
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            PacketDistributor.sendToAllPlayers(new SyncBeyparts(serializedParts));
        }
    }

    public static void addSerializedPart(Beypart part, List<CompoundTag> serializedParts) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            CompoundTag compound = part.toCompound();
            serializedParts.add(compound);
        }
    }

}
