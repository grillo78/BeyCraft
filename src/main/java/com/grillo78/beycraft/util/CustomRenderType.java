package com.grillo78.beycraft.util;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.OptionalDouble;

public class CustomRenderType extends RenderType {

    private static final LineState THICKLINESTATE = new LineState(OptionalDouble.of(8.0D));
    public static final RenderType THICKLINE = makeType("overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256, State.getBuilder().line(THICKLINESTATE).layer(PROJECTION_LAYERING)
                    .transparency(TransparencyState.TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
                    .cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false));
    private static final LineState THINLINESTATE = new LineState(OptionalDouble.of(2.0D));
    public static final RenderType THINLINE = makeType("overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256, State.getBuilder().line(THINLINESTATE).layer(PROJECTION_LAYERING)
                    .transparency(TransparencyState.TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
                    .cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false));

    public CustomRenderType(String name, VertexFormat vertexFormat, int drawMode, int bufferSize,
                            boolean useDelegate, boolean needsSorting, Runnable setupTaskIn,
                            Runnable clearTaskIn) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTaskIn, clearTaskIn);
    }
}
