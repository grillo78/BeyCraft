package com.grillo78.beycraft.util;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;

import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;

public class BlurLayerBuffer implements IRenderTypeBuffer {
    private final IRenderTypeBuffer.Impl buffer;
    private final IRenderTypeBuffer.Impl outlineBuffer = IRenderTypeBuffer.immediate(new BufferBuilder(256));

    public BlurLayerBuffer(Impl buffer) {
        this.buffer = buffer;
    }

    @Override
    public IVertexBuilder getBuffer(RenderType p_getBuffer_1_) {
        return null;
    }
}
