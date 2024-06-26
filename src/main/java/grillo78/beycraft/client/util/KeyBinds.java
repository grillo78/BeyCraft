package grillo78.beycraft.client.util;

import grillo78.beycraft.Beycraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyBinds {

    public static final KeyBinding LAUNCH_SCREEN = new KeyBinding("key." + Beycraft.MOD_ID + ".launch_screen", GLFW.GLFW_KEY_L, "key." + Beycraft.MOD_ID + ".category");
    public static final KeyBinding ACTIVATE_LAUNCH = new KeyBinding("key." + Beycraft.MOD_ID + ".activate_launch", GLFW.GLFW_KEY_APOSTROPHE, "key." + Beycraft.MOD_ID + ".category");
    public static final KeyBinding ACTIVATE_RESONANCE = new KeyBinding("key." + Beycraft.MOD_ID + ".activate_resonance", GLFW.GLFW_KEY_R, "key." + Beycraft.MOD_ID + ".category");
    public static final KeyBinding BELT = new KeyBinding("key." + Beycraft.MOD_ID + ".belt", GLFW.GLFW_KEY_B, "key." + Beycraft.MOD_ID + ".category");

    public static void registerKeys() {
        ClientRegistry.registerKeyBinding(KeyBinds.LAUNCH_SCREEN);
        ClientRegistry.registerKeyBinding(KeyBinds.ACTIVATE_LAUNCH);
        ClientRegistry.registerKeyBinding(KeyBinds.ACTIVATE_RESONANCE);
        ClientRegistry.registerKeyBinding(KeyBinds.BELT);
    }
}
