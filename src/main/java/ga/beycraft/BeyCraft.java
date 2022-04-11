package ga.beycraft;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import friedrichlp.renderlib.RenderLibSettings;
import friedrichlp.renderlib.math.Vector3;
import friedrichlp.renderlib.tracking.RenderManager;
import ga.beycraft.client.screen.LayerScreen;
import ga.beycraft.client.util.BeyPartModel;
import ga.beycraft.common.block.ModBlocks;
import ga.beycraft.common.container.ModContainers;
import ga.beycraft.common.item.BeyPartItem;
import ga.beycraft.common.item.ModItems;
import ga.beycraft.utils.CommonUtils;
import ga.beycraft.utils.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BuiltInModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Mod(Beycraft.MOD_ID)
public class Beycraft
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "beycraft";
    public static boolean HAS_INTERNET = true;

    public Beycraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            try {
                URL url = new URL("http://www.google.com");
                URLConnection connection = url.openConnection();
                connection.connect();
            } catch (MalformedURLException e) {
                HAS_INTERNET = false;
                System.out.println("Internet is not connected");
            } catch (IOException e) {
                HAS_INTERNET = false;
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
//            RenderLibSettings.Caching.CHECK_CACHE = false;
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
            MinecraftForge.EVENT_BUS.addListener(this::onRenderWorld);
            MinecraftForge.EVENT_BUS.addListener(this::renderHand);
        });
        if (HAS_INTERNET){
            try {
                downloadDefaultPack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    private void downloadDefaultPack() throws IOException {
        if (Config.COMMON.downloadDefaultPack.get()) {
            BufferedInputStream in = new BufferedInputStream(new URL("https://beycraft.ga/Starter%20Pack.zip").openStream());
            File itemsFolder = new File("BeyParts");
            if (!itemsFolder.exists()) {
                itemsFolder.mkdir();
            }
            File starterPackFile = new File("BeyParts/Starter Pack temp.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(starterPackFile);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            CommonUtils.ZipUtils.unzip(starterPackFile, itemsFolder);
            starterPackFile.delete();
        }
    }


    @OnlyIn(Dist.CLIENT)
    private void onRenderWorld(RenderWorldLastEvent event){
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        RenderManager.setCameraPos(new Vector3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z));
        RenderLibSettings.Rendering.CAMERA_HEIGHT_OFFSET = (float) (cameraPos.y - Minecraft.getInstance().player.getPosition(event.getPartialTicks()).y);
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
            event.getModelRegistry().put(modelResourceLocation, new BuiltInModel(ItemCameraTransforms.NO_TRANSFORMS, ItemOverrideList.EMPTY.EMPTY, MissingTextureSprite.newInstance(new AtlasTexture(PlayerContainer.BLOCK_ATLAS), 0, 10, 10, 0, 0), true));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.STADIUM, RenderType.cutoutMipped());
        ScreenManager.register(ModContainers.LAYER, LayerScreen::new);
    }
}
