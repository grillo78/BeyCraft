package grillo78.beycraft.client;

import grillo78.beycraft.client.entity.BeybladeRenderer;
import grillo78.beycraft.client.item.*;
import grillo78.beycraft.client.render.MeshRegistry;
import grillo78.beycraft.client.render.ObjMesh;
import grillo78.beycraft.client.render.ObjVertexFormat;
import grillo78.beycraft.entities.ModEntities;
import grillo78.beycraft.items.ModItems;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

import static grillo78.beycraft.Beycraft.MOD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class BeycraftClient {

    public static final ResourceLocation TEST_MESH =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "beyparts/burst/layers/z_achilles.obj");

    public BeycraftClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        container.getEventBus().addListener(this::onRegisterShaders);
        container.getEventBus().addListener(this::onRegisterReloadListeners);
        container.getEventBus().addListener(this::registerClientExtensions);
        container.getEventBus().addListener(this::registerRenderers);
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {

        event.registerItem(new GenericBeypartClientExtensions(), ModItems.METAL_FACEBOLT);
        event.registerItem(new MetalEnergyRingClientExtensions(), ModItems.METAL_ENERGY_RING);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.METAL_FUSION_WHEEL);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.METAL_SPIN_TRACK);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.METAL_PERFORMANCE_TIP);

        event.registerItem(new BurstLayerClientExtensions(), ModItems.BURST_LAYER);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.BURST_DISC);
        event.registerItem(new BurstCoreDiscClientExtensions(), ModItems.BURST_CORE_DISC);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.BURST_FRAME);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.BURST_DRIVER);

        event.registerItem(new XBladeClientExtensions(), ModItems.X_BLADE);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.X_RATCHET);
        event.registerItem(new GenericBeypartClientExtensions(), ModItems.X_BIT);

        event.registerItem(new LauncherClientExtensions(), ModItems.LAUNCHER);
    }

    // ── Registro de shaders (mod bus) ─────────────────────────────────────────
    public void onRegisterShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "obj"),
                            ObjVertexFormat.FORMAT
                    ), shader -> {
                        ObjMesh.OBJ_SHADER = shader;
                        // Log de atributos
                        for (int i = 0; i < 8; i++) {
                            int loc = GL20.glGetAttribLocation(shader.getId(), switch (i) {
                                case 0 -> "Position";
                                case 1 -> "Normal";
                                case 2 -> "UV0";
                                case 3 -> "UV2";
                                default -> "unknown_" + i;
                            });
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Error registrando shader OBJ", e);
        }
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerBlockEntityRenderer(ModBlockEntities.FORGE.get(), ForgeBlockEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.BEYBLADE.get(), BeybladeRenderer::new);
    }

    // ── Registro de reload listeners (mod bus) ────────────────────────────────
    public void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) rm -> {
            MeshRegistry.cleanup();
            MeshRegistry.registerAll();
        });
    }
}