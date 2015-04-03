package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(
        command="add",
        staticParams={"listName", "soundNames"},
        description="Create a new playlist.",
        paramDescriptions = {
                "listName= The name of the playlist. {NAME16}",
                "soundNames= A comma delimited list of resource sound names."
        })

public class AddSubCommand extends AbstractCommand {

    @Localizable static final String _PLAYLIST_ALREADY_EXISTS =
            "There is already a playlist with the name '{0: playlist name}'.";

    @Localizable static final String _SOUND_NOT_FOUND =
            "Failed to find a resource sound named '{0: sound name}'.";

    @Localizable static final String _FAILED =
            "Failed to create a new playlist.";

    @Localizable static final String _SUCCESS =
            "New playlist named '{0: playlist name}' was created.";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws CommandException {

        String playlistName = args.getName("listName");
        String rawSoundNames = args.getString("soundNames");

        PlayListManager manager = MusicalRegions.getPlayListManager();
        RegionPlayList playList = manager.get(playlistName);
        if (playList != null) {
            tellError(sender, Lang.get(_PLAYLIST_ALREADY_EXISTS, playlistName));
            return; // finish
        }

        String[] soundNames = TextUtils.PATTERN_COMMA.split(rawSoundNames);
        List<ResourceSound> sounds = new ArrayList<>(soundNames.length);

        for (String soundName : soundNames) {

            ResourceSound sound = Nucleus.getSoundManager().getSound(soundName);
            if (sound == null) {
                tellError(sender, Lang.get(_SOUND_NOT_FOUND, soundName));
                return; // finish
            }

            sounds.add(sound);
        }

        playList = manager.create(playlistName, sounds);
        if (playList == null) {
            tellError(sender, Lang.get(_FAILED));
            return; // finish
        }

        tellSuccess(sender, Lang.get(_SUCCESS, playlistName));
    }
}
