package me.miquiis.consoleapi.mixin.client;

import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.client.ClientKeybinds;
import me.miquiis.consoleapi.client.Console;
import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardListener.class)
public class KeyboardListenerMixin {

    @Shadow private boolean repeatEventsEnabled;

    @Shadow @Final private Minecraft mc;

    @Inject(method = "onKeyEvent", at = @At(value = "HEAD"))
    private void _onKeyEvent2(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci)
    {
        if (action == 1 && ConsoleAPI.getInstance() != null)
        {
            Console console = ConsoleAPI.getInstance().getConsole();
            if (console != null && !console.isConsoleOpen())
            {
                final KeyBinding[] keyBindings = ClientKeybinds.keyBindings;
                if (keyBindings[0].getKeyBinding().matchesKey(key, scanCode)) {
                    ConsoleAPI.getInstance().getConsole().openConsole();
                }
            } else if (console != null && console.isConsoleOpen())
            {
                final KeyBinding[] keyBindings = ClientKeybinds.keyBindings;
                if (keyBindings[0].getKeyBinding().matchesKey(key, scanCode)) {
                    ConsoleAPI.getInstance().getConsole().closeConsole();
                }
            }
        }
    }

    @Inject(method = "onKeyEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE), cancellable = true)
    private void _onKeyEvent(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci)
    {
        Console console = ConsoleAPI.getInstance().getConsole();
        if (console != null && console.isConsoleOpen()) {
            ci.cancel();
            boolean[] aboolean = new boolean[]{false};
            Screen.wrapScreenError(() -> {
                if (action != 1 && (action != 2 || !this.repeatEventsEnabled)) {
                    if (action == 0) {
                        if (!aboolean[0]) aboolean[0] = console.keyReleased(key, scanCode, modifiers);
                    }
                } else {
                    if (!aboolean[0]) aboolean[0] = console.keyPressed(key, scanCode, modifiers);
                }

            }, "keyPressed event handler", console.getClass().getCanonicalName());
        }
    }

    @Inject(method = "onCharEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE), cancellable = true)
    private void _onCharEvent(long windowPointer, int codePoint, int modifiers, CallbackInfo ci)
    {
        Console console = ConsoleAPI.getInstance().getConsole();
        if (console != null && console.isConsoleOpen()) {
            if (Character.charCount(codePoint) == 1) {
                Screen.wrapScreenError(() -> {
                    if (console.charTyped((char)codePoint, modifiers)) return;
                }, "charTyped event handler", console.getClass().getCanonicalName());
            } else {
                for(char c0 : Character.toChars(codePoint)) {
                    Screen.wrapScreenError(() -> {
                        if (console.charTyped(c0, modifiers)) return;
                    }, "charTyped event handler", console.getClass().getCanonicalName());
                }
            }
        }
    }


}
