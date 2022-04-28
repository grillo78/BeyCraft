package ga.beycraft.common.item;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import ga.beycraft.BeyTypes;
import ga.beycraft.ability.AbilityType;
import ga.beycraft.client.item.LayerRenderer;
import ga.beycraft.common.capability.item.parts.BeyCapabilityProvider;
import ga.beycraft.common.container.LayerContainer;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.entity.BeybladeEntity;
import ga.beycraft.common.tab.BeycraftItemGroup;
import ga.beycraft.utils.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayerItem extends BeyPartItem {
    protected final Direction rotationDirection;
    private final float attack;
    private final float defense;
    private final float weight;
    private final float burst;
    private final Color resonanceColor;
    private final Color secondResonanceColor;
    private final Color thirdResonanceColor;

    public LayerItem(String name, String displayName, int rotationDirection, float attack, float defense, float weight,
                     float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color resonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.LAYERS, new Properties().setISTER(() -> LayerRenderer::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = Direction.getFromValue(rotationDirection);
        this.resonanceColor = resonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
    }

    public int getSlotsAmount() {
        return 2;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BeyCapabilityProvider(new ItemStackHandler(getSlotsAmount()));
    }

    public Direction getRotationDirection(ItemStack stack) {
        return rotationDirection;
    }

    public boolean isBeyAssembled(ItemStack stack) {
        AtomicBoolean assembled = new AtomicBoolean(true);
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {

        });
        return assembled.get();
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = super.use(level, player, hand);
        if (result.getResult() != ActionResultType.SUCCESS && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new LayerContainer(ModContainers.LAYER, id,
                                    stack, playerInventory),
                            new StringTextComponent(getDescriptionId())));
            result = ActionResult.success(stack);
        }
        return result;
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

    public void renderBey(BeybladeEntity beyblade, RenderLayer layer, IItemHandler cap, float partialTicks) {
        if (cap.getStackInSlot(0).getItem() instanceof BeyPartItem) {
            List<RenderObject> childs = new ArrayList<>();
            Vector3d pos = beyblade.getPosition(partialTicks);
            RenderObject mainScene = layer.addRenderObject(getModelInfo(cap.getStackInSlot(0).getItem()));
            mainScene.setHidden(true);
            RenderObject sceneDisc = mainScene.addChild(getModelInfo(cap.getStackInSlot(0).getItem()));
            for (int i = 1; i < cap.getSlots(); i++) {
                childs.add(sceneDisc.addChild(getModelInfo(cap.getStackInSlot(i).getItem())));
            }
            RenderObject sceneLayer = sceneDisc.addChild(getModelInfo(beyblade.getStack().getItem()));
            RenderSystem.depthMask(true);
            mainScene.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            mainScene.transform.scale(0.3F, 0.3F, 0.3F);
            double stadiumCenterDistance = beyblade.findStadiumCenter().distanceTo(beyblade.getPosition(partialTicks));
            double inclinationMultiplier = beyblade.getEnergy()>1?stadiumCenterDistance : 1-beyblade.getEnergy();
            Vector3d vector = new Vector3d(-30, 0, this.getRotationDirection(beyblade.getStack()).getValue() * 30).multiply(inclinationMultiplier, inclinationMultiplier, inclinationMultiplier).yRot((float) Math.toRadians(MathHelper.lerp(partialTicks, beyblade.yRotO, beyblade.yRot)));
            mainScene.transform.rotate((float) vector.x, (float) 0, (float) -vector.z);
            sceneDisc.transform.rotate(0, MathHelper.lerp(partialTicks, beyblade.getSpinAngleO(), beyblade.getSpinAngle()), 0);
            mainScene.forceTransformUpdate();
            RenderManager.render(layer, RenderMode.USE_CUSTOM_MATS);
            sceneLayer.remove();
            sceneDisc.remove();
            for (RenderObject child : childs) {
                child.remove();
            }
            childs.clear();
            mainScene.remove();
        }
    }

    private ModelInfo getModelInfo(Item item) {
        return ModItems.ItemCreator.MODELS.get(item);
    }

    public double getRadiusReduction(ItemStack beyblade) {
        AtomicDouble radiusReduction = new AtomicDouble(0);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(1).getItem() instanceof DriverItem) {
                radiusReduction.set(((DriverItem) cap.getStackInSlot(1).getItem()).getRadiusReduction());
            }
        });

        return radiusReduction.get();
    }

    public double getDefense(ItemStack beyblade) {
        AtomicDouble defense = new AtomicDouble(this.defense);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(0).getItem() instanceof DiscItem) {
                defense.set(((DiscItem) cap.getStackInSlot(0).getItem()).getDefense(cap.getStackInSlot(0)));
            }
        });

        return defense.get();
    }

    public double getAttack(ItemStack beyblade) {
        AtomicDouble attack = new AtomicDouble(this.attack);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(0).getItem() instanceof DiscItem) {
                attack.set(((DiscItem) cap.getStackInSlot(0).getItem()).getAttack(cap.getStackInSlot(0)));
            }
        });
        return attack.get();
    }

    public double getFriction(ItemStack beyblade) {
        AtomicDouble friction = new AtomicDouble(1);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(1).getItem() instanceof DriverItem) {
                friction.set(((DriverItem) cap.getStackInSlot(1).getItem()).getFriction());
            }
        });
        return friction.get();
    }

    public static class Color {
        private final float red;
        private final float green;
        private final float blue;

        public Color(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }
    }
}
