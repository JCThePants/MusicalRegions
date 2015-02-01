package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;

@CommandInfo(
        command="playlist",
        description="Manage playlists.")

public class PlayListCommand extends AbstractCommand {

    public PlayListCommand() {
        super();

        registerCommand(AddSubCommand.class);
        registerCommand(DelSubCommand.class);
        registerCommand(ListSubCommand.class);
        registerCommand(LoopSubCommand.class);
        registerCommand(RandomSubCommand.class);
        registerCommand(SetSoundSubCommand.class);
    }
}
