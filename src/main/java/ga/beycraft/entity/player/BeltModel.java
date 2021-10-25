package ga.beycraft.entity.player;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * BeltModel - grillo78
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BeltModel extends Model {
    public ModelRenderer belt;

    public BeltModel() {
        super(RenderType::entityTranslucent);
        this.texWidth = 32;
        this.texHeight = 32;
        this.belt = new ModelRenderer(this, 0, 0);
        this.belt.setPos(0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.1F, 11.0F, -2.0F, 0.1F, 1.0F, 4.1F, 0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.1F, 11.0F, -2.1F, 8.2F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0F);
        this.belt.addBox(-4.0F, 11.0F, 2.0F, 8.1F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0F);
        this.belt.addBox(4.0F, 11.0F, -2.0F, 0.1F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.belt.addBox(4.0F, 11.0F, -2F, 2F, 4.0F, 4F, 0.0F, 0.0F, 0.0F);
        this.belt.addBox(-5.8F, 11.0F, -2F, 2F, 4.0F, 4F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.belt).forEach((modelRenderer) -> {
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

}
