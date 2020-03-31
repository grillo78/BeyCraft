package com.grillo78.beycraft.network.message;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MessageBeyCreatorUpdate implements IMessage<MessageBeyCreatorUpdate> {

    private ItemStack stack;
    private BlockPos pos;

    public MessageBeyCreatorUpdate(){}

    public MessageBeyCreatorUpdate(ItemStack stack, BlockPos pos){
        this.stack = stack;
        this.pos = pos;
    }

    @Override
    public void encode(MessageBeyCreatorUpdate message, PacketBuffer buffer) {
        buffer.writeItemStack(message.stack);
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public MessageBeyCreatorUpdate decode(PacketBuffer buffer) {
        return new MessageBeyCreatorUpdate(buffer.readItemStack(),buffer.readBlockPos());
    }

    @Override
    public void handle(MessageBeyCreatorUpdate message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(()->{
           if(supplier.get().getSender().world.getTileEntity(message.pos) instanceof BeyCreatorTileEntity){
               BeyCraft.logger.info(message.pos);
               BeyCreatorTileEntity tileEntity = (BeyCreatorTileEntity) supplier.get().getSender().world.getTileEntity(message.pos);
               tileEntity.getInventory().ifPresent(h->{
                   h.insertItem(1,new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Reference.MODID,"boostdisc"))),false);
                   BeyCraft.logger.info(h.getStackInSlot(1).getItem().getTranslationKey());
               });
           }
        });
    }
}
