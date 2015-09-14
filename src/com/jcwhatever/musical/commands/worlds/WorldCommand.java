package com.jcwhatever.musical.commands.worlds;

import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;

@CommandInfo(
        command={"worlds", "world"},
        description="Manage worlds.")

public class WorldCommand extends AbstractCommand {

    public WorldCommand() {
        super();

        registerCommand(AddSubCommand.class);
        registerCommand(DelSubCommand.class);
        registerCommand(InfoSubCommand.class);
        registerCommand(ListSubCommand.class);
        registerCommand(PlayListSubCommand.class);
        registerCommand(TrackChangeDelaySubCommand.class);
    }
}