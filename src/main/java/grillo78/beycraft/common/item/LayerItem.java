package grillo78.beycraft.common.item;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.library.RenderMode;
import friedrichlp.renderlib.tracking.ModelInfo;
import friedrichlp.renderlib.tracking.RenderLayer;
import friedrichlp.renderlib.tracking.RenderManager;
import friedrichlp.renderlib.tracking.RenderObject;
import grillo78.beycraft.BeyTypes;
import grillo78.beycraft.Beycraft;
import grillo78.beycraft.client.item.LayerRenderer;
import grillo78.beycraft.common.ability.Ability;
import grillo78.beycraft.common.ability.AbilityType;
import grillo78.beycraft.common.capability.item.parts.BeyCapabilityProvider;
import grillo78.beycraft.common.container.LayerContainer;
import grillo78.beycraft.common.container.ModContainers;
import grillo78.beycraft.common.entity.BeybladeEntity;
import grillo78.beycraft.common.tab.BeycraftItemGroup;
import grillo78.beycraft.utils.Direction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
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
    private final Color firstResonanceColor;
    private final Color secondResonanceColor;
    private final Color thirdResonanceColor;

    public LayerItem(String name, String displayName, int rotationDirection, float attack, float defense, float weight,
                     float burst, AbilityType primaryAbility, AbilityType secondaryAbility, BeyTypes type, Color firstResonanceColor, Color secondResonanceColor, Color thirdResonanceColor) {
        super(name, displayName, type, primaryAbility, secondaryAbility, BeycraftItemGroup.LAYERS, new Properties().setISTER(() -> LayerRenderer::new));
        this.attack = attack;
        this.defense = defense;
        this.weight = weight;
        this.burst = burst;
        this.rotationDirection = Direction.getFromValue(rotationDirection);
        this.firstResonanceColor = firstResonanceColor;
        this.secondResonanceColor = secondResonanceColor;
        this.thirdResonanceColor = thirdResonanceColor;
        ModItems.ItemCreator.LAYERS.add(this);
    }

    public List<Ability> getAbilities(ItemStack stack) {

        List<Ability> abilities = new ArrayList<>();

        if (getPrimaryAbility() != null)
            abilities.add(getPrimaryAbility());
        if (getSecondaryAbility() != null)
            abilities.add(getSecondaryAbility());

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).getItem() instanceof BeyPartItem)
                    abilities.add(((BeyPartItem) itemHandler.getStackInSlot(i).getItem()).getPrimaryAbility());
                if (itemHandler.getStackInSlot(i).getItem() instanceof BeyPartItem)
                    abilities.add(((BeyPartItem) itemHandler.getStackInSlot(i).getItem()).getSecondaryAbility());
            }
        });

        return abilities;
    }

    @Override
    public StringTextComponent getName(ItemStack stack) {
        List<ITextComponent> names = getPartsNames(stack);
        StringTextComponent name = getLayerName(stack);
        for (int i = 0; i < names.size(); i++) {
            name.append(" ").append(names.get(i));
        }
        return name;
    }

    protected StringTextComponent getLayerName(ItemStack stack) {
        return new StringTextComponent(getDisplayName());
    }

    protected List<ITextComponent> getPartsNames(ItemStack stack) {
        List<ITextComponent> names = new ArrayList<>();

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            if(!itemHandler.getStackInSlot(0).isEmpty()) names.add(itemHandler.getStackInSlot(0).getHoverName());
            if(!itemHandler.getStackInSlot(1).isEmpty()) names.add(itemHandler.getStackInSlot(1).getHoverName());
        });

        return names;
    }

    public ResourceLocation getBookCategory() {
        return new ResourceLocation(Beycraft.MOD_ID, "layers");
    }

    @Override
    protected JsonObject getSecondPage() {
        JsonObject page = super.getSecondPage();
        return page;
    }

    public Color getFirstResonanceColor() {
        return firstResonanceColor;
    }

    public Color getSecondResonanceColor() {
        return secondResonanceColor;
    }

    public Color getThirdResonanceColor() {
        return thirdResonanceColor;
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
            for (int i = 0; i < getSlotsAmount(); i++) {
                if (cap.getStackInSlot(i).getItem() == Items.AIR)
                    assembled.set(false);
            }
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
                            (id, playerInventory, playerEntity) -> getLayerContainer(id, stack, playerInventory),
                            new StringTextComponent(getDescriptionId())));
            result = ActionResult.success(stack);
        }
        return result;
    }

    protected Container getLayerContainer(int id, ItemStack stack, PlayerInventory playerInventory) {
        return new LayerContainer(ModContainers.LAYER, id,
                stack, playerInventory);
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
        if (nbt != null) {
            if(nbt.contains("base_tag")) {
                CompoundNBT baseTag = nbt.getCompound("base_tag");
                stack.setTag(baseTag);
            }
            if(nbt.contains("capability_tag")){
                INBT capabilityTag = nbt.get("capability_tag");
                stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(items -> CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, capabilityTag));
            }
        }
    }

    public void renderBey(BeybladeEntity beyblade, RenderLayer layer, IItemHandler cap, float partialTicks) {
        if (cap.getStackInSlot(0).getItem() instanceof BeyPartItem) {
            List<RenderObject> childs = new ArrayList<>();
            Vector3d pos = beyblade.getPosition(partialTicks);
            RenderObject mainScene = layer.addRenderObject(getModelInfo(cap.getStackInSlot(0).getItem()));
            mainScene.setHidden(true);
            RenderObject sceneDisc = mainScene.addChild(getModelInfo(cap.getStackInSlot(0).getItem()));
            cap.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(discCap -> {
                for (int i = 0; i < discCap.getSlots(); i++) {
                    ModelInfo model = getModelInfo(discCap.getStackInSlot(i).getItem());
                    if (model != null) {
                        RenderObject child = sceneDisc.addChild(model);
                        if (cap.getStackInSlot(0).getItem() instanceof DiscFrameItem)
                            child.transform.rotate(0, ((DiscFrameItem) cap.getStackInSlot(0).getItem()).getFrameRotation(), 0);
                        childs.add(child);
                    }
                }
            });
            for (int i = 1; i < cap.getSlots(); i++) {
                childs.add(sceneDisc.addChild(getModelInfo(cap.getStackInSlot(i).getItem())));
            }
            RenderObject sceneLayer = sceneDisc.addChild(getModelInfo(beyblade.getStack().getItem()));
            RenderSystem.depthMask(true);
            mainScene.transform.setPosition((float) pos.x, (float) pos.y, (float) pos.z);
            mainScene.transform.scale(0.3F, 0.3F, 0.3F);
            double stadiumCenterDistance = beyblade.findStadiumCenter().distanceTo(beyblade.getPosition(partialTicks));
            double inclinationMultiplier = beyblade.getEnergy() > 1 ? stadiumCenterDistance : 1 - beyblade.getEnergy();
            Vector3d vector = new Vector3d(-30, 0, this.getRotationDirection(beyblade.getStack()).getValue() * 30).multiply(inclinationMultiplier, inclinationMultiplier, inclinationMultiplier).yRot((float) Math.toRadians(MathHelper.lerp(partialTicks, beyblade.yRotO, beyblade.yRot)));
            mainScene.transform.rotate((float) vector.x, (float) 0, (float) -vector.z);
            sceneDisc.transform.rotate(0, MathHelper.lerp(partialTicks, beyblade.getSpinAngleO(), beyblade.getSpinAngle()), 0);
            int direction = getRotationDirection(beyblade.getStack()).getValue();
            if (this instanceof DualLayerItem) {
                sceneLayer.transform.rotate(0, -(direction * 35) + direction * (70 * beyblade.getHealth() / beyblade.getMaxHealth()), 0);
            } else {
                sceneLayer.transform.rotate(0, 10, 0);
            }
            mainScene.forceTransformUpdate();
//            GlStateManager._blendColor(1,1,1,1F);
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

    public double getSpeed(ItemStack beyblade) {
        AtomicDouble speed = new AtomicDouble(1);
        beyblade.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            if (cap.getStackInSlot(1).getItem() instanceof DriverItem) {
                speed.set(((DriverItem) cap.getStackInSlot(1).getItem()).getSpeed());
            }
        });
        return speed.get();
    }

    public double getBurst(ItemStack beyblade) {
        return burst;
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
