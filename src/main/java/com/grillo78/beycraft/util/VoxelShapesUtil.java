package com.grillo78.beycraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import xyz.heroesunited.heroesunited.hupacks.HUPacks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VoxelShapesUtil {
    private static VoxelShape shape = VoxelShapes.block();

    public static void init(){

        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("hitboxes/stadium_hitbox.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            JsonObject jsonObject = JSONUtils.fromJson(HUPacks.GSON, reader, JsonObject.class);
            JsonArray elements = jsonObject.get("elements").getAsJsonArray();

            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();
                JsonArray from = element.get("from").getAsJsonArray();
                JsonArray to = element.get("to").getAsJsonArray();
                if(shape == VoxelShapes.block()) {
                    shape = Block.box(from.get(0).getAsDouble(),from.get(1).getAsDouble(),from.get(2).getAsDouble(),to.get(0).getAsDouble(),to.get(1).getAsDouble(),to.get(2).getAsDouble());
                } else {
                    shape = VoxelShapes.join(shape, Block.box(from.get(0).getAsDouble(),from.get(1).getAsDouble(),from.get(2).getAsDouble(),to.get(0).getAsDouble(),to.get(1).getAsDouble(),to.get(2).getAsDouble()), IBooleanFunction.OR).optimize();
                }
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VoxelShape getStadiumShape(float offsetX, float offsetZ){
        return shape.move(offsetX,0,offsetZ);
    }
}
