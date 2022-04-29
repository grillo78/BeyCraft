package ga.beycraft.common.item;

import ga.beycraft.client.item.LauncherRenderer;
import ga.beycraft.common.capability.entity.BladerCapabilityProvider;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.capability.item.LauncherCapabilityProvider;
import ga.beycraft.common.container.LauncherContainer;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.launch.Launch;
import ga.beycraft.common.tab.BeycraftItemGroup;
import ga.beycraft.utils.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class LauncherItem extends Item {

    private Direction direction;

    public LauncherItem(Properties properties, Direction direction) {
        super(properties.tab(BeycraftItemGroup.INSTANCE).setISTER(() -> LauncherRenderer::new));
        this.direction = direction;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new LauncherCapabilityProvider();
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();

        CompoundNBT combinedTag = new CompoundNBT();

        if (baseTag != null) {
            combinedTag.put("base_tag", baseTag);
        }

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            INBT capabilityTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(cap, null);

            if (capabilityTag != null) {
                combinedTag.put("capability_tag", capabilityTag);
            }
        });

        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound("base_tag"); // empty if not found
        INBT capabilityTag = nbt.get("capability_tag");    // empty if not found
        stack.setTag(baseTag);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = super.use(level, player, hand);
        if (result.getResult() != ActionResultType.SUCCESS && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayerEntity) player,
                        new SimpleNamedContainerProvider(
                                (id, playerInventory, playerEntity) -> generateContainer(id, stack, playerInventory),
                                new StringTextComponent(getDescriptionId())));
                result = ActionResult.success(stack);
            } else {
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
                    if (cap.getStackInSlot(0).getItem() instanceof LayerItem) {
                        ItemStack beybladeStack = cap.getStackInSlot(0);
                        player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader->{
                            Launch launch = blader.getLaunchType().generateLaunch();
                            launch.launchBeyblade(beybladeStack, level, player, hand);
                        });
                    }
                });
            }
        }
        return result;
    }

    public LauncherContainer generateContainer(int id, ItemStack stack, PlayerInventory playerInventory) {
        return new LauncherContainer(direction == Direction.RIGHT ? ModContainers.RIGHT_LAUNCHER : ModContainers.LEFT_LAUNCHER, id, stack, playerInventory, direction);
    }
}
