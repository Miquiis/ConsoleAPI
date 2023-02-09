package me.miquiis.consoleapi.client.listeners;

import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.client.ClientKeybinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = ConsoleAPI.MOD_ID)
public class ModClientEvent {

    @SubscribeEvent
    public static void onClientSetupEvent(final FMLClientSetupEvent event)
    {
        ClientKeybinds.registerBindings();
    }

}
