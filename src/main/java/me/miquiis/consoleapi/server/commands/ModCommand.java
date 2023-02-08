package me.miquiis.consoleapi.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.server.network.ModNetwork;
import me.miquiis.consoleapi.server.network.messages.SendVariablePacket;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;

public class ModCommand {

    public ModCommand(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("consoleapi")
                .then(Commands.literal("setFloat")
                        .then(Commands.argument("variable", StringArgumentType.string())
                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                        .then(Commands.argument("side", EnumArgument.enumArgument(LogicalSide.class))
                                                .executes(context -> {
                                                    ServerPlayerEntity player = context.getSource().asPlayer();
                                                    String variable = StringArgumentType.getString(context, "variable");
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (context.getArgument("side", LogicalSide.class) == LogicalSide.SERVER)
                                                    {
                                                        ConsoleAPI.setFloatVariable(variable, value);
                                                    } else {
                                                        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SendVariablePacket(variable, SendVariablePacket.VariableType.FLOAT, value));
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(Commands.literal("setString")
                        .then(Commands.argument("variable", StringArgumentType.string())
                                .then(Commands.argument("value", StringArgumentType.string())
                                        .then(Commands.argument("side", EnumArgument.enumArgument(LogicalSide.class))
                                                .executes(context -> {
                                                    ServerPlayerEntity player = context.getSource().asPlayer();
                                                    String variable = StringArgumentType.getString(context, "variable");
                                                    String value = StringArgumentType.getString(context, "value");
                                                    if (context.getArgument("side", LogicalSide.class) == LogicalSide.SERVER)
                                                    {
                                                        ConsoleAPI.setStringVariable(variable, value);
                                                    } else {
                                                        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SendVariablePacket(variable, SendVariablePacket.VariableType.STRING, value));
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(Commands.literal("setInteger")
                        .then(Commands.argument("variable", StringArgumentType.string())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .then(Commands.argument("side", EnumArgument.enumArgument(LogicalSide.class))
                                                .executes(context -> {
                                                    ServerPlayerEntity player = context.getSource().asPlayer();
                                                    String variable = StringArgumentType.getString(context, "variable");
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    if (context.getArgument("side", LogicalSide.class) == LogicalSide.SERVER)
                                                    {
                                                        ConsoleAPI.setIntegerVariable(variable, value);
                                                    } else {
                                                        ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SendVariablePacket(variable, SendVariablePacket.VariableType.INTEGER, value));
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }

}
