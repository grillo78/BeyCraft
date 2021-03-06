package com.grillo78.beycraft.util;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.OptionalDouble;

import net.minecraft.client.renderer.RenderState.LineState;
import net.minecraft.client.renderer.RenderState.TransparencyState;
import net.minecraft.client.renderer.RenderType.State;

public class CustomRenderType extends RenderType {

    private static final LineState THICKLINESTATE = new LineState(OptionalDouble.of(8.0D));
    public static final RenderType THICKLINE = create("overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256, State.builder().setLineState(THICKLINESTATE).setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TransparencyState.TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE)
                    .setCullState(NO_CULL).setLightmapState(NO_LIGHTMAP).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    private static final LineState THINLINESTATE = new LineState(OptionalDouble.of(2.0D));
    public static final RenderType THINLINE = create("overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256, State.builder().setLineState(THINLINESTATE).setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TransparencyState.TRANSLUCENT_TRANSPARENCY).setTextureState(NO_TEXTURE)
                    .setCullState(NO_CULL).setLightmapState(NO_LIGHTMAP).setWriteMaskState(COLOR_WRITE).createCompositeState(false));

    public CustomRenderType(String name, VertexFormat vertexFormat, int drawMode, int bufferSize,
                            boolean useDelegate, boolean needsSorting, Runnable setupTaskIn,
                            Runnable clearTaskIn) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTaskIn, clearTaskIn);
    }
}
