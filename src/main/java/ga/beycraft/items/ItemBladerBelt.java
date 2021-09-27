package ga.beycraft.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ga.beycraft.BeyCraft;
import ga.beycraft.BeyRegistry;
import ga.beycraft.Reference;
import ga.beycraft.entity.player.BeltModel;
import ga.beycraft.inventory.BeltContainer;
import ga.beycraft.inventory.ItemBeltProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.heroesunited.heroesunited.common.objects.container.EquipmentAccessoriesSlot;
import xyz.heroesunited.heroesunited.common.objects.items.IAccessory;

import javax.annotation.Nullable;

public class ItemBladerBelt extends Item implements IAccessory {

    @OnlyIn(Dist.CLIENT)
    private BeltModel model = new BeltModel();
    private final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/belt/belt.png");

    public ItemBladerBelt(String name) {
        super(new Item.Properties().tab(BeyCraft.BEYCRAFTTAB).stacksTo(1));
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        BeyRegistry.ITEMS.put(name, this);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemBeltProvider(stack);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        if (!world.isClientSide) {
            player.playNotifySound(BeyRegistry.OPEN_CLOSE_BELT, SoundCategory.PLAYERS, 1, 1);
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider(
                            (id, playerInventory, playerEntity) -> new BeltContainer(BeyRegistry.BELT_CONTAINER, id,
                                    player.getItemInHand(handIn), playerInventory, true),
                            new StringTextComponent(getRegistryName().getPath())));
        }
        return ActionResult.success(player.getItemInHand(handIn));
    }

    @Override
    public ResourceLocation getTexture(ItemStack itemStack, PlayerEntity playerEntity, EquipmentAccessoriesSlot equipmentAccessoriesSlot) {
        return null;
    }

    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int slot) {
        matrixStack.pushPose();
        renderer.getModel().body.translateAndRotate(matrixStack);
        IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(bufferIn, model.renderType(TEXTURE), false,
                false);
        model.renderToBuffer(matrixStack, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        if (stack.hasTag() && stack.getTag().contains("launcher")) {
            matrixStack.translate(0.4, 0.6, -0.1);
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 0, 1), -90, true));
            matrixStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 180, true));
            matrixStack.scale(0.2f, 0.2f, 0.2f);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    ItemStack.of((CompoundNBT) stack.getTag().get("launcher")),
                    ItemCameraTransforms.TransformType.NONE, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStack,
                    bufferIn);
        }
        matrixStack.popPose();
    }

    @Override
    public boolean renderDefaultModel() {
        return false;
    }

    @Override
    public EquipmentAccessoriesSlot getSlot() {
        return EquipmentAccessoriesSlot.BELT;
    }
}
