package ga.beycraft.items;

import ga.beycraft.BeyCraft;
import ga.beycraft.BeyCraftRegistry;
import ga.beycraft.Reference;
import ga.beycraft.capabilities.BladerCapProvider;
import ga.beycraft.entity.EntityBey;
import ga.beycraft.inventory.ItemLauncherProvider;
import ga.beycraft.inventory.LauncherContainer;
import ga.beycraft.items.render.LauncherItemStackRendererTileEntity;
import ga.beycraft.network.PacketHandler;
import ga.beycraft.network.message.MessageSyncBladerLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class ItemLauncher extends Item {

    private int rotation;

    public ItemLauncher(String name, int rotation) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1).setISTER(() -> LauncherItemStackRendererTileEntity::new));
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.rotation = rotation;
        BeyCraftRegistry.ITEMS.put(name, this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemLauncherProvider();
    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        ItemStack launcher = player.getItemInHand(handIn);
        if (!player.isCrouching()) {
            if (!world.isClientSide) {
                if (BeyCraftRegistry.BEY_ENTITY_TYPE != null && launcher.hasTag()) {
                    ItemStack bey = ItemStack.of(launcher.getTag().getCompound("bey"));
                    launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                        if (bey.getItem() instanceof ItemBeyLayer) {
                            launcher.getTag().put("bey", ItemStack.EMPTY.save(new CompoundNBT()));
                            player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i->{
                                bey.getTag().putBoolean("isEntity", true);
                                EntityBey entity = new EntityBey(BeyCraftRegistry.BEY_ENTITY_TYPE, world,
                                        bey,
                                        getRotation(launcher), player.getName().getString(),i.getBladerLevel());
                                entity.moveTo(player.position().x + player.getLookAngle().x / 2,
                                        player.position().y + 1 + player.getLookAngle().y/2,
                                        player.position().z + player.getLookAngle().z/2, player.yRot , 0);
                                entity.yHeadRot = entity.yRot;
                                entity.yBodyRot = entity.yRot;
                                world.addFreshEntity(entity);
                            });
                            h.getStackInSlot(0).shrink(1);
                            player.awardStat(Stats.ITEM_USED.get(this));
                            player.getCooldowns().addCooldown(this, 20);
                            player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i->{
                                i.increaseExperience(0.025F);
                                PacketHandler.instance.sendTo(new MessageSyncBladerLevel(i.getExperience(), i.isInResonance(), true, player.getId()), ((ServerPlayerEntity)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                            });
                        }
                    });
                }
            }
        } else {
            if (!world.isClientSide) {
                if (launcher.getItem() instanceof ItemDualLauncher) {
                    NetworkHooks.openGui((ServerPlayerEntity) player,
                            new SimpleNamedContainerProvider(
                                    (id, playerventory, playerEntity) -> new LauncherContainer(BeyCraftRegistry.LAUNCHER_DUAL_CONTAINER,
                                            id, player.getItemInHand(handIn), playerventory, handIn),
                                    new StringTextComponent(getRegistryName().getPath())));
                } else {
                    if (rotation == 1) {
                        NetworkHooks.openGui((ServerPlayerEntity) player,
                                new SimpleNamedContainerProvider(
                                        (id, playerventory, playerEntity) -> new LauncherContainer(BeyCraftRegistry.LAUNCHER_RIGHT_CONTAINER,
                                                id, player.getItemInHand(handIn), playerventory, handIn),
                                        new StringTextComponent(getRegistryName().getPath())));
                    } else {
                        NetworkHooks.openGui((ServerPlayerEntity) player,
                                new SimpleNamedContainerProvider(
                                        (id, playerventory, playerEntity) -> new LauncherContainer(BeyCraftRegistry.LAUNCHER_LEFT_CONTAINER,
                                                id, player.getItemInHand(handIn), playerventory, handIn),
                                        new StringTextComponent(getRegistryName().getPath())));
                    }
                }
            }
            return ActionResult.success(launcher);

        }
        return super.use(world, player, handIn);

    }

    public int getRotation(ItemStack stack) {
        return rotation;
    }
}
