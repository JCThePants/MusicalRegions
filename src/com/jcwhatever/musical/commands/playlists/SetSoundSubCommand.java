/* This file is part of MusicalRegions for Bukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) JCThePants (www.jcwhatever.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(
        command="setsound",
        staticParams={"listName", "soundNames"},
        description="Change a playlist's sounds.",
        paramDescriptions = {
                "listName= The name of the playlist.",
                "soundNames= A comma delimited list of resource sound names."
        })

public class SetSoundSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _SOUND_NOT_FOUND =
            "A resource sound named '{0: sound name}' was not found.";

    @Localizable static final String _SUCCESS =
            "Playlist '{0: playlist name}' sounds updated.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws InvalidArgumentException {

        String listName = args.getName("listName");
        String rawSoundNames = args.getString("soundNames");

        PlayListManager manager = MusicalRegions.getPlayListManager();
        RegionPlayList playList = manager.get(listName);
        if (playList == null) {
            tellError(sender, Lang.get(_PLAYLIST_NOT_FOUND, listName));
            return; // finish
        }

        String[] soundNames = TextUtils.PATTERN_COMMA.split(rawSoundNames);
        List<ResourceSound> sounds = new ArrayList<ResourceSound>(soundNames.length);

        for (String soundName : soundNames) {
            ResourceSound sound = Nucleus.getSoundManager().getSound(soundName);
            if (sound == null) {
                tellError(sender, Lang.get(_SOUND_NOT_FOUND, soundName));
                return; // finish
            }
            sounds.add(sound);
        }

        playList.clearSounds();
        playList.addSounds(sounds);

        tellSuccess(sender, Lang.get(_SUCCESS, listName));
    }
}
