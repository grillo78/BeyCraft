package grillo78.beycraft.client.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class ObjVertexFormat {
    public static final VertexFormatElement NORMAL_FLOAT = new VertexFormatElement(
            10, 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.NORMAL, 3
    );

    public static final VertexFormatElement UV2_FLOAT = new VertexFormatElement(
            11, 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2
    );

    public static final VertexFormat FORMAT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)  // vec3 float, 12 bytes
            .add("Normal",   NORMAL_FLOAT)                  // vec3 float, 12 bytes
            .add("UV0",      VertexFormatElement.UV0)       // vec2 float,  8 bytes
            .build();
}