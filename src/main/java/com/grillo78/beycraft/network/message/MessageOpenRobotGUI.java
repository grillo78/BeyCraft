package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.blocks.RobotBlock;
import com.grillo78.beycraft.gui.RobotGUI;
import com.grillo78.beycraft.tileentity.RobotTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.awt.*;
import java.util.function.Supplier;

public class MessageOpenRobotGUI implements IMessage<MessageOpenRobotGUI>{
    @Override
    public void encode(MessageOpenRobotGUI message, PacketBuffer buffer) {

    }

    @Override
    public MessageOpenRobotGUI decode(PacketBuffer buffer) {
        return new MessageOpenRobotGUI();
    }

    @Override
    public void handle(MessageOpenRobotGUI message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
            RobotTileEntity tileEntity;
            RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
            BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getPos();
            if(Minecraft.getInstance().world.getBlockState(pos).get(RobotBlock.PART).equals(RobotBlock.EnumPartType.TOP)){
                tileEntity = (RobotTileEntity) Minecraft.getInstance().world.getTileEntity(pos);
            } else {
                tileEntity = (RobotTileEntity) Minecraft.getInstance().world.getTileEntity(pos.up());
            }
            Minecraft.getInstance().displayGuiScreen(new RobotGUI(new StringTextComponent(""),tileEntity));
        });
        supplier.get().setPacketHandled(true);
    }
}
