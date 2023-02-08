package me.miquiis.consoleapi.server.network.messages;

import me.miquiis.consoleapi.ConsoleAPI;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class SendVariablePacket {

   public enum VariableType {
      STRING,
      FLOAT,
      INTEGER
   }

   private String variable;
   private VariableType type;
   private Object value;

   public SendVariablePacket(String variable, VariableType type, Object value) {
      this.variable = variable;
      this.type = type;
      this.value = value;
   }

   public static SendVariablePacket decodePacket(PacketBuffer buf) {
      String variable = buf.readString();
      VariableType type = buf.readEnumValue(VariableType.class);
      Object object = null;
      switch (type)
      {
         case INTEGER:
         {
            object = buf.readInt();
            break;
         }
         case STRING:
         {
            object = buf.readString();
            break;
         }
         case FLOAT:
         {
            object = buf.readFloat();
            break;
         }
      }
      return new SendVariablePacket(variable, type, object);
   }

   public static void encodePacket(SendVariablePacket packet, PacketBuffer buf) {
      buf.writeString(packet.variable);
      buf.writeEnumValue(packet.type);
      switch (packet.type)
      {
         case INTEGER:
         {
            buf.writeInt((Integer)packet.value);
            break;
         }
         case STRING:
         {
            buf.writeString((String)packet.value);
            break;
         }
         case FLOAT:
         {
            buf.writeFloat((Float)packet.value);
            break;
         }
      }
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public static void handlePacket(final SendVariablePacket msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            switch (msg.type)
            {
               case FLOAT:{
                  ConsoleAPI.setFloatVariable(msg.variable, (Float)msg.value);
                  break;
               }
               case STRING:
               {
                  ConsoleAPI.setStringVariable(msg.variable, (String)msg.value);
                  break;
               }
               case INTEGER:
               {
                  ConsoleAPI.setIntegerVariable(msg.variable, (Integer)msg.value);
                  break;
               }
            }
         });
      });
   }
}