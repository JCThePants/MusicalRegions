package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="del",
        staticParams={"listName"},
        description="Delete a playlist.",
        paramDescriptions = {
                "listName= The name of the playlist to delete."
        })

public class DelSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _FAILED =
            "Failed to delete playlist '{0: playlist name}'.";

    @Localizable static final String _SUCCESS =
            "Playlist '{0: playlist name}' deleted.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String listName = args.getString("listName");

        PlayListManager manager = MusicalRegions.getPlayListManager();

        MusicPlayList playList = manager.get(listName);
        if (playList == null)
            throw new CommandException(Lang.get(_PLAYLIST_NOT_FOUND, listName));

        if (!manager.remove(listName))
            throw new CommandException(Lang.get(_FAILED, listName));

        tellSuccess(sender, Lang.get(_SUCCESS, listName));
    }
}
