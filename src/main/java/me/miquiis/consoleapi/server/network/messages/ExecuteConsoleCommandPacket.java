package me.miquiis.consoleapi.server.network.messages;

import me.miquiis.consoleapi.common.events.ExecuteConsoleCommandEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ExecuteConsoleCommandPacket {

   private String command;

   public ExecuteConsoleCommandPacket(String command) {
      this.command = command;
   }

   public static ExecuteConsoleCommandPacket decodePacket(PacketBuffer buf) {
      return new ExecuteConsoleCommandPacket(buf.readString());
   }

   public static void encodePacket(ExecuteConsoleCommandPacket packet, PacketBuffer buf) {
      buf.writeString(packet.command);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public static void handlePacket(final ExecuteConsoleCommandPacket msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         MinecraftForge.EVENT_BUS.post(new ExecuteConsoleCommandEvent(ctx.get().getSender(), msg.command));
      });
   }
}