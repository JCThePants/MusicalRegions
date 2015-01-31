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
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.utils.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="loop",
        staticParams={"listName", "isEnabled"},
        description="Enable or disable a playlist's loop mode.",
        paramDescriptions = {
                "listName= The name of the playlist to change loop settings for.",
                "isEnabled= True to enable playlist looping. False to disable."
        })

public class LoopSubCommand extends AbstractCommand {

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _ENABLED =
            "Playlist '{0: playlist name}' loop Enabled.";

    @Localizable static final String _DISABLED =
            "Playlist '{0: playlist name}' loop Disabled.";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidArgumentException {

        String listName = args.getString("listName");
        boolean isEnabled = args.getBoolean("isEnabled");


        PlayListManager manager = MusicalRegions.getPlayListManager();

        RegionPlayList list = manager.get(listName);
        if (list == null) {
            tellError(sender, Lang.get(_PLAYLIST_NOT_FOUND, listName));
            return; // finish
        }

        list.setLoop(isEnabled);

        if (isEnabled)
            tellSuccess(sender, Lang.get(_ENABLED, listName));
        else
            tellSuccess(sender, Lang.get(_DISABLED, listName));
    }
}
