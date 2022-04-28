package ga.beycraft.client.util;

import ga.beycraft.Beycraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static final KeyBinding LAUNCH_SCREEN = new KeyBinding("key."+ Beycraft.MOD_ID +".launch_screen", GLFW.GLFW_KEY_L, "key."+ Beycraft.MOD_ID +".category");
}
