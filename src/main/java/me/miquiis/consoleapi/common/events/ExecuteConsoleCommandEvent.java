package me.miquiis.consoleapi.common.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ExecuteConsoleCommandEvent extends Event {

    private final PlayerEntity executor;
    private final String command;
    private final String[] args;

    public ExecuteConsoleCommandEvent(PlayerEntity executor, String rawCommand)
    {
        this.executor = executor;
        this.command = rawCommand.split(" ")[0];

        String[] args = new String[0];
        if (rawCommand.split(" ").length > 1)
        {
            args = rawCommand.substring(command.length() + 1).split(" ");
        }

        this.args = args;
    }

    public ExecuteConsoleCommandEvent(PlayerEntity executor, String command, String[] args)
    {
        this.executor = executor;
        this.command = command;
        this.args = args;
    }

    public PlayerEntity getExecutor() {
        return executor;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
