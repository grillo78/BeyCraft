package ga.beycraft;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.common.block.ModBlocks;
import ga.beycraft.common.item.BeyPartItem;
import ga.beycraft.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Mod(Beycraft.MOD_ID)
public class Beycraft
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "beycraft";
    public static boolean HAS_INTERNET = false;

    public Beycraft() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
                HAS_INTERNET = true;
            } catch (MalformedURLException e) {
                System.out.println("Internet is not connected");
            } catch (IOException e) {
                System.out.println("Internet is not connected");
            }
            try {
                File folder = new File("beycraft_cached_models");
                if (folder.exists())
                    FileUtils.deleteDirectory(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RenderLibSettings.Caching.CACHE_LOCATION = "beycraft_cached_models";
            RenderLibSettings.Caching.CACHE_VERSION = "1";
            RenderLibSettings.General.MODEL_UNLOAD_DELAY_MS = Integer.MAX_VALUE;
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerRenderers);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderLevel);
            MinecraftForge.EVENT_BUS.addListener(this::renderHand);
        });

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @OnlyIn(Dist.CLIENT)
    private void onRenderLevel(RenderLevelLastEvent event){
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        RenderManager.setCameraPos(new Vector3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z));
        RenderLibSettings.Rendering.CAMERA_HEIGHT_OFFSET = (float) (cameraPos.y - Minecraft.getInstance().player.getPosition(event.getPartialTick()).y);
        RenderManager.setRenderDistance(Minecraft.getInstance().options.renderDistance * 16);
        try {
            BeyPartModel.worldModels.forEach(h -> h.render());
            RenderManager.update();
        } catch (NullPointerException e) {
        }
//        for (Runnable runnable : BeyRender.getRunnables()) {
//            runnable.run();
//        }
        BeyPartModel.worldModels.clear();
//        BeyRender.getRunnables().clear();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderHand(RenderHandEvent event) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        BeyPartModel.handModels.forEach(h -> h.render());
        BeyPartModel.handModels.clear();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @OnlyIn(Dist.CLIENT)
    private void onModelBake(ModelBakeEvent event){
        for (BeyPartItem item : BeyPartItem.getParts()) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(ItemTransforms.NO_TRANSFORMS, ItemOverrides.EMPTY, MissingTextureAtlasSprite.newInstance(new TextureAtlas(InventoryMenu.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STADIUM, RenderType.cutoutMipped());
    }

    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }
}
