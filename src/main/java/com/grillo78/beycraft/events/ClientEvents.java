package com.grillo78.beycraft.events;

import com.grillo78.beycraft.BeyRegistry;
import com.grillo78.beycraft.Reference;
import com.grillo78.beycraft.entity.BeyEntityRenderFactory;
import com.grillo78.beycraft.gui.*;
import com.grillo78.beycraft.network.PacketHandler;
import com.grillo78.beycraft.network.message.IMessage;
import com.grillo78.beycraft.network.message.MessageOpenBelt;
import com.grillo78.beycraft.particles.SparkleParticle;
import com.grillo78.beycraft.tileentity.RenderExpository;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.*;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    public static final KeyBinding beltKey = new KeyBinding("key.beycraft.belt", 66, "key.beycraft.category");

    public static void injectResources() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;


        File itemsFolder = new File(Minecraft.getInstance().gameDir, "BeyParts");
        File[] zipFiles = itemsFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".zip");
            }
        });
        try {
            for (File file : zipFiles) {
                try {
                    final String id = file.getName().replace(".zip", "").replace(" ", "_") + "_addon_resources";
                    final ITextComponent name = new StringTextComponent(file.getName().replace(".zip", "") + " Resources");
                    final ITextComponent description = new StringTextComponent("Beycraft addon resources.");

                    final IResourcePack pack = new FilePack(file) {
                        String prefix = "assets/beycraft";

                        @Override
                        protected InputStream getInputStream(String resourcePath) throws IOException {
                            if ("pack.mcmeta".equals(resourcePath))
                                return new ByteArrayInputStream(("{\"pack\":{\"description\": \"dummy\",\"pack_format\": 4}}").getBytes(StandardCharsets.UTF_8));
                            if (!resourcePath.startsWith(prefix)) throw new FileNotFoundException(resourcePath);

                            return super.getInputStream(resourcePath);
                        }

                        @Override
                        public boolean resourceExists(String resourcePath) {
                            if ("pack.mcmeta".equals(resourcePath)) return true;
                            if (!resourcePath.startsWith(prefix)) return false;

                            return super.resourceExists(resourcePath);
                        }
                    };

                    Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
                        @Override
                        public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
                            nameToPackMap.put(id, (T) new ClientResourcePackInfo(id, true, () ->
                                    pack, name, description, PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, false, null, false
                            ));
                        }
                    });
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }


    }

    @SubscribeEvent
    public static void onParticleFactorieRegistry(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(BeyRegistry.SPARKLE, SparkleParticle.Factory::new);
    }

    @SubscribeEvent
    public static void editHud(RenderGameOverlayEvent.Post event) {
        if (!Minecraft.getInstance().gameSettings.showDebugInfo) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft.getInstance().getRenderManager().textureManager
                        .bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/bladerlevel.png"));
            }
        }
    }

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BeyRegistry.STADIUM, RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(BeyRegistry.EXPOSITORY, RenderType.getCutoutMipped());
        for (Item item : BeyRegistry.ITEMSLAYER) {
        }
        ClientRegistry.registerKeyBinding(ClientEvents.beltKey);
        ScreenManager.registerFactory(BeyRegistry.LAUNCHER_CONTAINER, LauncherGUI::new);
        ScreenManager.registerFactory(BeyRegistry.DISK_FRAME_CONTAINER, DiskFrameGUI::new);
        try {
            Class.forName("com.lazy.baubles.Baubles");
            ScreenManager.registerFactory(BeyRegistry.BELT_CONTAINER, BeltGUI::new);
        } catch (Exception e) {
        }
        ScreenManager.registerFactory(BeyRegistry.BEY_CONTAINER, BeyGUI::new);
        ScreenManager.registerFactory(BeyRegistry.HANDLE_CONTAINER, HandleGUI::new);
        for (Item item : BeyRegistry.ITEMSLAYER) {
            ModelLoader.addSpecialModel(new ResourceLocation("beycraft", "layers/" + item.getTranslationKey().replace("item.beycraft.", "")));
        }
        RenderingRegistry.registerEntityRenderingHandler(BeyRegistry.BEY_ENTITY_TYPE,
                new BeyEntityRenderFactory());
        ClientRegistry.bindTileEntityRenderer(BeyRegistry.EXPOSITORYTILEENTITYTYPE, RenderExpository::new);
    }

    @SubscribeEvent
    public static void onKeyPressed(final InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().player == null)
            return;
        if (ClientEvents.beltKey.isPressed()) {
            try {
                Class.forName("com.lazy.baubles.Baubles");
                PacketHandler.instance.sendToServer(new MessageOpenBelt());
            } catch (Exception e) {

            }
        }
    }
}
