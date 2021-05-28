package com.grillo78.beycraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.grillo78.beycraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import xyz.heroesunited.heroesunited.hupacks.HUPacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Stream;

public class VoxelShapesUtil {

    public static VoxelShape stadiumMiddleShape(){

        JsonObject jsonObject = null;
        ArrayList<VoxelShape> list = new ArrayList<>();
        try {
            IResourceManager manager = ServerLifecycleHooks.getCurrentServer().getDataPackRegistries().getResourceManager();
            jsonObject = JSONUtils.fromJson(HUPacks.GSON, new BufferedReader(new InputStreamReader(manager.getResource(new ResourceLocation(Reference.MODID,"hitboxes/stadium_hitbox.json")).getInputStream(), StandardCharsets.UTF_8)), JsonObject.class);
            JsonArray elements = jsonObject.get("elements").getAsJsonArray();

            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonArray from = element.get("from").getAsJsonArray();
                JsonArray to = element.get("to").getAsJsonArray();
                list.add(Block.box(from.get(0).getAsDouble(),from.get(1).getAsDouble(),from.get(2).getAsDouble(),to.get(0).getAsDouble(),to.get(1).getAsDouble(),to.get(2).getAsDouble()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Stream<VoxelShape> stream = list.stream();
        return stream.reduce((v1, v2) -> { return VoxelShapes.join(v1, v2, IBooleanFunction.OR); }).get();
    }

}
