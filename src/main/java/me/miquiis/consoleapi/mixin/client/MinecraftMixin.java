package me.miquiis.consoleapi.mixin.client;

import me.miquiis.consoleapi.ConsoleAPI;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private MainWindow mainWindow;

    @Inject(method = "updateWindowSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MainWindow;setGuiScale(D)V", shift = At.Shift.AFTER))
    private void _updateWindowSize(CallbackInfo ci)
    {
        if (ConsoleAPI.getInstance() != null && ConsoleAPI.getInstance().getConsole() != null && ConsoleAPI.getInstance().getConsole().isConsoleOpen())
        {
            ConsoleAPI.getInstance().getConsole().resize((Minecraft)(Object)this, this.mainWindow.getScaledWidth(), this.mainWindow.getScaledHeight());
        }
    }

}
