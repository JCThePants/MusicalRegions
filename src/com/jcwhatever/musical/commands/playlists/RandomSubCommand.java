package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="random",
        staticParams={"listName", "isEnabled"},
        description="Enable or disable a playlist's random playback mode.",
        paramDescriptions = {
                "listName= The name of the playlist to change loop settings for.",
                "isEnabled= True to enable playlist random playback. False to disable."
        })

public class RandomSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _ENABLED =
            "Playlist '{0: playlist name}' random playback Enabled.";

    @Localizable static final String _DISABLED =
            "Playlist '{0: playlist name}' random playback Disabled.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws InvalidArgumentException {

        String listName = args.getString("listName");
        boolean isEnabled = args.getBoolean("isEnabled");

        PlayListManager manager = MusicalRegions.getPlayListManager();

        RegionPlayList list = manager.get(listName);
        if (list == null) {
            tellError(sender, Lang.get(_PLAYLIST_NOT_FOUND, listName));
            return; // finish
        }

        list.setRandom(isEnabled);

        if (isEnabled)
            tellSuccess(sender, Lang.get(_ENABLED, listName));
        else
            tellSuccess(sender, Lang.get(_DISABLED, listName));
    }
}
