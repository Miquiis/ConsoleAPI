package me.miquiis.consoleapi.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientKeybinds {

    public static KeyBinding[] keyBindings;

    public static void registerBindings() {
        ClientKeybinds.keyBindings[0] = new KeyBinding("key.consoleapi.open_console.desc", 96, "key.consoleapi.group");
        for (final KeyBinding keyBinding : ClientKeybinds.keyBindings) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
    }

    static {
        ClientKeybinds.keyBindings = new KeyBinding[1];
    }

}
