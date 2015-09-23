package com.jcwhatever.musical.commands.worlds;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.worlds.MusicWorld;
import com.jcwhatever.musical.worlds.WorldManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="info",
        staticParams={"worldName", "page=1"},
        description="Get information about the settings of a world binding.",
        paramDescriptions = {
                "worldName= The name of the world to get info about.",
                "page= {PAGE}"
        })

public class InfoSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Info for '{0: world name}'";

    @Localizable static final String _WORLD_NOT_FOUND =
            "A world binding for world '{0: world name}' was not found.";

    @Localizable static final String _LABEL_WORLD = "World";
    @Localizable static final String _LABEL_TRACK_CHANGE_DELAY = "Track Change Delay";
    @Localizable static final String _LABEL_PLAYLIST = "Resource Sounds";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String worldName = args.getString("worldName");
        int page = args.getInteger("page");

        ChatPaginator pagin = createPagin(args, 7, Lang.get(_PAGINATOR_TITLE));

        WorldManager manager = MusicalRegions.getWorldManager();
        MusicWorld musicWorld = manager.get(worldName);
        if (musicWorld == null)
            throw new CommandException(Lang.get(_WORLD_NOT_FOUND, worldName));

        pagin.add(Lang.get(_LABEL_WORLD), musicWorld.getName());
        pagin.add(Lang.get(_LABEL_TRACK_CHANGE_DELAY), musicWorld.getTrackChangeDelay());
        pagin.add(Lang.get(_LABEL_PLAYLIST), musicWorld.getPlaylistName());

        pagin.show(sender, page, TextUtils.FormatTemplate.LIST_ITEM_DESCRIPTION);
    }
}

