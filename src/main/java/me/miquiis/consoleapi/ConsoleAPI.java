package me.miquiis.consoleapi;

import me.miquiis.consoleapi.server.commands.ModCommand;
import me.miquiis.consoleapi.server.network.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.HashMap;
import java.util.Map;

@Mod(ModInformation.MOD_ID)
public class ConsoleAPI
{
    private static ConsoleAPI instance;
    public static final String MOD_ID = ModInformation.MOD_ID;

    private static final Map<String, String> CLIENT_STRING_VARIABLES = new HashMap<>();
    private static final Map<String, String> SERVER_STRING_VARIABLES = new HashMap<>();

    private static final Map<String, Float> CLIENT_FLOAT_VARIABLES = new HashMap<>();
    private static final Map<String, Float> SERVER_FLOAT_VARIABLES = new HashMap<>();

    private static final Map<String, Integer> CLIENT_INTEGER_VARIABLES = new HashMap<>();
    private static final Map<String, Integer> SERVER_INTEGER_VARIABLES = new HashMap<>();

    public ConsoleAPI() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static boolean isServerSide()
    {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
    }

    public static void setFloatVariable(String variable, float value)
    {
        if (isServerSide())
        {
            SERVER_FLOAT_VARIABLES.put(variable, value);
        } else {
            CLIENT_FLOAT_VARIABLES.put(variable, value);
        }
    }

    public static void setStringVariable(String variable, String value)
    {
        if (isServerSide())
        {
            SERVER_STRING_VARIABLES.put(variable, value);
        } else {
            CLIENT_STRING_VARIABLES.put(variable, value);
        }
    }

    public static void setIntegerVariable(String variable, int value)
    {
        if (isServerSide())
        {
            SERVER_INTEGER_VARIABLES.put(variable, value);
        } else {
            CLIENT_INTEGER_VARIABLES.put(variable, value);
        }
    }

    public static float getFloatVariable(String variable, float defaultValue)
    {
        if (isServerSide())
        {
            return SERVER_FLOAT_VARIABLES.getOrDefault(variable, defaultValue);
        } else {
            return CLIENT_FLOAT_VARIABLES.getOrDefault(variable, defaultValue);
        }
    }

    public static int getIntegerVariable(String variable, int defaultValue)
    {
        if (isServerSide())
        {
            return SERVER_INTEGER_VARIABLES.getOrDefault(variable, defaultValue);
        } else {
            return CLIENT_INTEGER_VARIABLES.getOrDefault(variable, defaultValue);
        }
    }

    public static String getStringVariable(String variable, String defaultValue)
    {
        if (isServerSide())
        {
            return SERVER_STRING_VARIABLES.getOrDefault(variable, defaultValue);
        } else {
            return CLIENT_STRING_VARIABLES.getOrDefault(variable, defaultValue);
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        instance = this;
        ModNetwork.init();
    }

    public static ConsoleAPI getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new ModCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
