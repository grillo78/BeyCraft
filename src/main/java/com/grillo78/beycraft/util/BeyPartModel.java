package com.grillo78.beycraft.util;

import friedrichlp.renderlib.RenderLibRegistry;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.math.Matrix4f;
import friedrichlp.renderlib.render.ViewBoxes;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class BeyPartModel {

    private final net.minecraft.util.math.vector.Matrix4f modelView;
    private static final RenderLayer layer = RenderManager.addRenderLayer(ViewBoxes.ALWAYS);
    public static final ArrayList<BeyPartModel> worldModels = new ArrayList<>();
    public static final ArrayList<BeyPartModel> handModels = new ArrayList<>();
    public static final ArrayList<BeyPartModel> interfaceModels = new ArrayList<>();
    private final Vector3d pos;
    private final ModelInfo model;

    public BeyPartModel(net.minecraft.util.math.vector.Matrix4f modelView, Vector3d pos, ModelInfo model) {
        this.modelView = modelView;
        this.pos = pos;
        this.model = model;
    }

    public void render() {
        RenderLibRegistry.Compatibility.MODEL_VIEW_PROVIDER = () -> {
            FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(16);
            modelView.store(floatBuffer);

            return new Matrix4f(floatBuffer);
        };

        RenderObject sceneLayer = layer.addRenderObject(model);
        sceneLayer.transform.setPosition((float) pos.x+0.5F, (float) pos.y+0.5F, (float) pos.z+0.5F);
        sceneLayer.forceTransformUpdate();
        RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
        layer.removeRenderObject(sceneLayer);
    }
}
