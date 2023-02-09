package me.miquiis.consoleapi.server.network;

import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.server.network.messages.ExecuteConsoleCommandPacket;
import me.miquiis.consoleapi.server.network.messages.SendVariablePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    public static final String NETWORK_VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ConsoleAPI.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION)
    );

    public static void init() {
        CHANNEL.registerMessage(0, SendVariablePacket.class, SendVariablePacket::encodePacket, SendVariablePacket::decodePacket, SendVariablePacket::handlePacket);
        CHANNEL.registerMessage(1, ExecuteConsoleCommandPacket.class, ExecuteConsoleCommandPacket::encodePacket, ExecuteConsoleCommandPacket::decodePacket, ExecuteConsoleCommandPacket::handlePacket);
    }

}
