package grillo78.beycraft.common.item;

import grillo78.beycraft.client.item.LauncherRenderer;
import grillo78.beycraft.common.capability.entity.BladerCapabilityProvider;
import grillo78.beycraft.common.capability.item.LauncherCapabilityProvider;
import grillo78.beycraft.common.container.LauncherContainer;
import grillo78.beycraft.common.container.ModContainers;
import grillo78.beycraft.common.launch.Launch;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import grillo78.beycraft.utils.Direction;
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

import javax.annotation.Nullable;

public class LauncherItem extends Item {

    private Direction direction;

    public LauncherItem(Properties properties, Direction direction) {
        super(properties.tab(BeycraftItemGroup.INSTANCE).setISTER(() -> LauncherRenderer::new).stacksTo(1));
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
        CompoundNBT baseTag = stack.getOrCreateTag();

        CompoundNBT combinedTag = new CompoundNBT();

        if (baseTag != null) {
            combinedTag.put("base_tag", baseTag);
        }

        stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> {
            CompoundNBT capabilityTag = cap.serializeNBT();
            if(capabilityTag.size()>0)
            combinedTag.put("capability_tag", capabilityTag);
        });

        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null) {
            if (nbt.contains("base_tag")) {
                CompoundNBT baseTag = nbt.getCompound("base_tag");
                stack.setTag(baseTag);
            }
            if (nbt.contains("capability_tag")) {
                INBT capabilityTag = nbt.get("capability_tag");
                stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> cap.deserializeNBT((CompoundNBT) capabilityTag));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = super.use(level, player, hand);
        if (result.getResult() != ActionResultType.SUCCESS && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayerEntity) player,
                        new SimpleNamedContainerProvider(
                                (id, playerInventory, playerEntity) -> generateContainer(id, stack, playerInventory, hand),
                                new StringTextComponent(getDescriptionId())));
                result = ActionResult.success(stack);
            } else {
                stack.getCapability(LauncherCapabilityProvider.LAUNCHER_CAPABILITY).ifPresent(cap -> {
                    if (cap.getInventory().getStackInSlot(0).getItem() instanceof LayerItem) {
                        ItemStack beybladeStack = cap.getInventory().getStackInSlot(0);
                        player.getCapability(BladerCapabilityProvider.BLADER_CAP).ifPresent(blader -> {
                            Launch launch = blader.getLaunchType().generateLaunch();
                            launch.launchBeyblade(beybladeStack, level, (ServerPlayerEntity) player, hand);
                        });
                    }
                });
            }
        }
        return result;
    }

    public LauncherContainer generateContainer(int id, ItemStack stack, PlayerInventory playerInventory, Hand hand) {
        return new LauncherContainer(direction == Direction.RIGHT ? ModContainers.RIGHT_LAUNCHER : ModContainers.LEFT_LAUNCHER, id, stack, playerInventory, hand == Hand.MAIN_HAND, direction);
    }
}
