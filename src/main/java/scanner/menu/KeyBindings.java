package scanner.menu;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;


@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final KeyMapping OPEN_MENU_KEY =
            new KeyMapping(
                    "key.scanner.open_menu",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    "key.categories.scanner"
            );

    public static final KeyMapping TOGGLE_GLASSES_KEY =
            new KeyMapping(
                    "key.scanner.toggle_glasses",
                    KeyConflictContext.IN_GAME,
                    KeyModifier.NONE,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    "key.categories.scanner"
            );

    public static void register(final RegisterKeyMappingsEvent event) {
        event.register(OPEN_MENU_KEY);
        event.register(TOGGLE_GLASSES_KEY);
    }
}
