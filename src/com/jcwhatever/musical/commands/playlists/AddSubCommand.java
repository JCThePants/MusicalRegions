package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.resourcepacks.sounds.types.IResourceSound;
import com.jcwhatever.nucleus.managed.sounds.Sounds;
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

public class AddSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PLAYLIST_ALREADY_EXISTS =
            "There is already a playlist with the name '{0: playlist name}'.";

    @Localizable static final String _SOUND_NOT_FOUND =
            "Failed to find a resource sound named '{0: sound name}'.";

    @Localizable static final String _FAILED =
            "Failed to create a new playlist.";

    @Localizable static final String _SUCCESS =
            "New playlist named '{0: playlist name}' was created.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String playlistName = args.getName("listName");
        String rawSoundNames = args.getString("soundNames");

        PlayListManager manager = MusicalRegions.getPlayListManager();
        MusicPlayList playList = manager.get(playlistName);
        if (playList != null)
            throw new CommandException(Lang.get(_PLAYLIST_ALREADY_EXISTS, playlistName));

        String[] soundNames = TextUtils.PATTERN_COMMA.split(rawSoundNames);
        List<IResourceSound> sounds = new ArrayList<>(soundNames.length);

        for (String soundName : soundNames) {

            IResourceSound sound = Sounds.get(soundName);
            if (sound == null)
                throw new CommandException(Lang.get(_SOUND_NOT_FOUND, soundName));

            sounds.add(sound);
        }

        playList = manager.create(playlistName, sounds);
        if (playList == null)
            throw new CommandException(Lang.get(_FAILED));

        tellSuccess(sender, Lang.get(_SUCCESS, playlistName));
    }
}
