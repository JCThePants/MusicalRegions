package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;

@CommandInfo(
        command="regions",
        description="Manage musical regions.")

public class RegionsCommand extends AbstractCommand {

    public RegionsCommand() {
        super();

        registerCommand(AddSubCommand.class);
        registerCommand(DelSubCommand.class);
        registerCommand(InfoSubCommand.class);
        registerCommand(ListSubCommand.class);
        registerCommand(PlayListSubCommand.class);
        registerCommand(RedefineSubCommand.class);
        registerCommand(SetSourceSubCommand.class);
        registerCommand(VolumeSubCommand.class);
    }
}
