package grillo78.beycraft.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemContent {
    public static final ItemContent EMPTY = new ItemContent(ItemStack.EMPTY);
    public static final Codec<ItemContent> CODEC = RecordCodecBuilder.create(
            omnitrixComponentInstance -> omnitrixComponentInstance.group(
                            ItemStack.CODEC.fieldOf("stack").forGetter(ItemContent::getStack)
                    )
                    .apply(omnitrixComponentInstance, ItemContent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ItemContent::getStack,
            ItemContent::new);
    
    private ItemStack stack;

    public ItemContent(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return !(other instanceof ItemContent itemContent) ? false : stack.equals(itemContent.stack);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.stack);
    }
}
