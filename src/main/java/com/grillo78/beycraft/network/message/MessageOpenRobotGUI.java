package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.blocks.RobotBlock;
import com.grillo78.beycraft.tileentity.RobotTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageOpenRobotGUI implements IMessage<MessageOpenRobotGUI>{
    private boolean valid = false;
    public MessageOpenRobotGUI() {
    }
    public MessageOpenRobotGUI(boolean valid) {
        this.valid = valid;
    }

    @Override
    public void encode(MessageOpenRobotGUI message, PacketBuffer buffer) {
        buffer.writeBoolean(message.valid);
    }

    @Override
    public MessageOpenRobotGUI decode(PacketBuffer buffer) {
        return new MessageOpenRobotGUI(buffer.readBoolean());
    }

    @Override
    public void handle(MessageOpenRobotGUI message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()-> {
            if (message.valid) {
                RobotTileEntity tileEntity;
                RayTraceResult rayTraceBlock = Minecraft.getInstance().player.pick(20.0D, 0.0F, false);
                BlockPos pos = ((BlockRayTraceResult) rayTraceBlock).getBlockPos();
                if (Minecraft.getInstance().level.getBlockState(pos).getValue(RobotBlock.PART).equals(RobotBlock.EnumPartType.TOP)) {
                    tileEntity = (RobotTileEntity) Minecraft.getInstance().level.getBlockEntity(pos);
                } else {
                    tileEntity = (RobotTileEntity) Minecraft.getInstance().level.getBlockEntity(pos.above());
                }
                tileEntity.openGUI();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
