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

import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        command="track-change-delay",
        staticParams={ "worldName", "tickDelay"},
        description="Change the delay between music.",
        paramDescriptions = {
                "worldName= The name of the world.",
                "tickDelay= The delay in ticks. 0 for no delay."
        })

public class TrackChangeDelaySubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _WORLD_NOT_FOUND =
            "A world binding for world '{0: world name}' does not exist.";

    @Localizable static final String _SUCCESS =
            "Track change delay for world binding '{0: world name}' set to {1: ticks} ticks.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String worldName = args.getString("worldName");
        long delay = args.getLong("tickDelay");

        WorldManager manager = MusicalRegions.getWorldManager();
        MusicWorld world = manager.get(worldName);
        if (world == null)
            throw new CommandException(Lang.get(_WORLD_NOT_FOUND, worldName));

        world.setTrackChangeDelay(delay);

        tellSuccess(sender, Lang.get(_SUCCESS, world.getName(), delay));
    }
}
