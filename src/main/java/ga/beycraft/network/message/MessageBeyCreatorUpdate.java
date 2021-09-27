package ga.beycraft.network.message;

import ga.beycraft.tileentity.BeyCreatorTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageBeyCreatorUpdate implements IMessage<MessageBeyCreatorUpdate> {

	private ItemStack stack;
	private BlockPos pos;

	public MessageBeyCreatorUpdate() {
	}

	public MessageBeyCreatorUpdate(ItemStack stack, BlockPos pos) {
		this.stack = stack;
		this.pos = pos;
	}

	@Override
	public void encode(MessageBeyCreatorUpdate message, PacketBuffer buffer) {
		buffer.writeItem(message.stack);
		buffer.writeBlockPos(message.pos);
	}

	@Override
	public MessageBeyCreatorUpdate decode(PacketBuffer buffer) {
		return new MessageBeyCreatorUpdate(buffer.readItem(), buffer.readBlockPos());
	}

	@Override
	public void handle(MessageBeyCreatorUpdate message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(() -> {
			if (supplier.get().getSender().level.getBlockEntity(message.pos) instanceof BeyCreatorTileEntity) {
				BeyCreatorTileEntity tileEntity = (BeyCreatorTileEntity) supplier.get().getSender().level
						.getBlockEntity(message.pos);
				tileEntity.getInventory().ifPresent(h -> {
					if (!h.getStackInSlot(1).isEmpty()) {
						h.getStackInSlot(1).shrink(1);
					}
					h.insertItem(1, message.stack, false);
					supplier.get().getSender().level.sendBlockUpdated(message.pos,
							supplier.get().getSender().level.getBlockState(message.pos),
							supplier.get().getSender().level.getBlockState(message.pos), 0);
				});
			}
		});
		supplier.get().setPacketHandled(true);
	}
}
