package me.miquiis.consoleapi.client.listeners;

import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.client.Console;
import me.miquiis.consoleapi.common.events.ExecuteConsoleCommandEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT, modid = ConsoleAPI.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onExecuteConsoleCommand(ExecuteConsoleCommandEvent event)
    {
        if (event.getCommand().equalsIgnoreCase("consoleapi"))
        {
            if (event.getArgs().length == 3)
            {
                String prefix = event.getArgs()[0];
                String variable = event.getArgs()[1];
                String value = event.getArgs()[2];
                if (prefix.equalsIgnoreCase("setFloat"))
                {
                    ConsoleAPI.setFloatVariable(variable, Float.parseFloat(value));
                } else if (prefix.equalsIgnoreCase("setString"))
                {
                    ConsoleAPI.setStringVariable(variable, value);
                } else if (prefix.equalsIgnoreCase("setInteger"))
                {
                    ConsoleAPI.setIntegerVariable(variable, Integer.parseInt(value));
                }
            } else if (event.getArgs().length == 2)
            {
                String prefix = event.getArgs()[0];
                String variable = event.getArgs()[1];
                if (prefix.equalsIgnoreCase("getFloat"))
                {
                    ConsoleAPI.getInstance().getConsole().setConsoleOutput("" + ConsoleAPI.getFloatVariable(variable, -1));
                } else if (prefix.equalsIgnoreCase("getString"))
                {
                    ConsoleAPI.getInstance().getConsole().setConsoleOutput(ConsoleAPI.getStringVariable(variable, ""));
                } else if (prefix.equalsIgnoreCase("getInteger"))
                {
                    ConsoleAPI.getInstance().getConsole().setConsoleOutput("" + ConsoleAPI.getIntegerVariable(variable, -1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGUIRenderPost(RenderGameOverlayEvent.Post event)
    {
        if (Minecraft.getInstance().currentScreen == null)
        {
            ConsoleAPI.getInstance().getConsole().render(event.getMatrixStack(), event.getPartialTicks(), false);
        }
    }

    @SubscribeEvent
    public static void oScreenRenderPost(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        ConsoleAPI.getInstance().getConsole().render(event.getMatrixStack(), event.getRenderPartialTicks(), true);
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            ConsoleAPI.getInstance().getConsole().tick();
        }
    }

}
