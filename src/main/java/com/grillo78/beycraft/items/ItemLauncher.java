package com.grillo78.beycraft.items;

import javax.annotation.Nullable;

import com.grillo78.beycraft.BeyCraft;
import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.capabilities.BladerCapProvider;
import com.grillo78.beycraft.entity.EntityBey;
import com.grillo78.beycraft.inventory.ItemLauncherProvider;
import com.grillo78.beycraft.inventory.LauncherContainer;

import com.grillo78.beycraft.items.render.LauncherItemStackRendererTileEntity;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.MessageSyncBladerLevel;
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

public class ItemLauncher extends Item {

    private int rotation;

    public ItemLauncher(String name, int rotation) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1).setISTER(() -> LauncherItemStackRendererTileEntity::new));
        setRegistryName(new ResourceLocation(Reference.MODID, name));
        this.rotation = rotation;
        BeyRegistry.ITEMS.put(name, this);
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
                if (BeyRegistry.BEY_ENTITY_TYPE != null && launcher.hasTag()) {
                    launcher.getTag().put("bey", ItemStack.EMPTY.save(new CompoundNBT()));
                    launcher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                        if (h.getStackInSlot(0).getItem() instanceof ItemBeyLayer) {

                            player.getCapability(BladerCapProvider.BLADERLEVEL_CAP).ifPresent(i->{
                                h.getStackInSlot(0).getTag().putBoolean("isEntity", true);
                                EntityBey entity = new EntityBey(BeyRegistry.BEY_ENTITY_TYPE, world,
                                        h.getStackInSlot(0).copy(),
                                        getRotation(launcher), player.getName().getString(),i.getBladerLevel(), h.getStackInSlot(2).getItem() instanceof ItemBeyLogger);
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
                                i.increaseExperience(0.5f);
                                PacketHandler.instance.sendTo(new MessageSyncBladerLevel(i.getBladerLevel(),i.getExperience()), ((ServerPlayerEntity)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
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
                                    (id, playerventory, playerEntity) -> new LauncherContainer(BeyRegistry.LAUNCHER_DUAL_CONTAINER,
                                            id, player.getItemInHand(handIn), playerventory, handIn),
                                    new StringTextComponent(getRegistryName().getPath())));
                } else {
                    if (rotation == 1) {
                        NetworkHooks.openGui((ServerPlayerEntity) player,
                                new SimpleNamedContainerProvider(
                                        (id, playerventory, playerEntity) -> new LauncherContainer(BeyRegistry.LAUNCHER_RIGHT_CONTAINER,
                                                id, player.getItemInHand(handIn), playerventory, handIn),
                                        new StringTextComponent(getRegistryName().getPath())));
                    } else {
                        NetworkHooks.openGui((ServerPlayerEntity) player,
                                new SimpleNamedContainerProvider(
                                        (id, playerventory, playerEntity) -> new LauncherContainer(BeyRegistry.LAUNCHER_LEFT_CONTAINER,
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
