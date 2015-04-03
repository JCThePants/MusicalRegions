package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="del",
        staticParams={"listName"},
        description="Delete a playlist.",
        paramDescriptions = {
                "listName= The name of the playlist to delete."
        })

public class DelSubCommand extends AbstractCommand {

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _FAILED =
            "Failed to delete playlist '{0: playlist name}'.";

    @Localizable static final String _SUCCESS =
            "Playlist '{0: playlist name}' deleted.";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidArgumentException {

        String listName = args.getString("listName");

        PlayListManager manager = MusicalRegions.getPlayListManager();

        RegionPlayList playList = manager.get(listName);
        if (playList == null) {
            tellError(sender, Lang.get(_PLAYLIST_NOT_FOUND, listName));
            return; // finish
        }

        if (!manager.remove(listName)) {
            tellError(sender, Lang.get(_FAILED, listName));
            return; // finish
        }

        tellSuccess(sender, Lang.get(_SUCCESS, listName));
    }
}
