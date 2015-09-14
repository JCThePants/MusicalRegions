package com.jcwhatever.musical.commands.worlds;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.musical.worlds.MusicWorld;
import com.jcwhatever.musical.worlds.WorldManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="del",
        staticParams={"worldName"},
        description="Delete a playlist/world binding.",
        paramDescriptions = {
                "worldName= The name of the world."
        })

public class DelSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _WORLD_NOT_FOUND =
            "A world named '{0: region name}' was not found.";

    @Localizable static final String _FAILED =
            "Failed to delete world binding.";

    @Localizable static final String _SUCCESS =
            "World binding deleted from world '{0: world name}'.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String worldName = args.getString("worldName");

        WorldManager manager = MusicalRegions.getWorldManager();

        MusicWorld musicWorld = manager.get(worldName);
        if (musicWorld == null)
            throw new CommandException(Lang.get(_WORLD_NOT_FOUND, worldName));

        if (!manager.remove(worldName))
            throw new CommandException(Lang.get(_FAILED, worldName));

        tellSuccess(sender, Lang.get(_SUCCESS, worldName));
    }
}
