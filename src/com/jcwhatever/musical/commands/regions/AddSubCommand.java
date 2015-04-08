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


package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.providers.regionselect.IRegionSelection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandInfo(
        command="add",
        staticParams={"regionName", "playListName"},
        description="Create a musical region using your current region selection.",
        paramDescriptions = {
                "regionName= The name of the region. {NAME16}",
                "playListName= The name of the playlist to use."
        })

public class AddSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _REGION_ALREADY_EXISTS =
            "There is already a musical region with the name '{0: region name}'.";

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "Failed to find a playlist named '{0: playlist name}'.";

    @Localizable static final String _FAILED =
            "Failed to create a new region.";

    @Localizable static final String _SUCCESS =
            "New musical region named '{0: region name}' was created.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException{

        CommandException.checkNotConsole(getPlugin(), this, sender);

        String regionName = args.getName("regionName");
        String playListName = args.getString("playListName");

        IRegionSelection regionSelection = getRegionSelection((Player) sender);

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region != null)
            throw new CommandException(Lang.get(_REGION_ALREADY_EXISTS, regionName));

        PlayListManager playListManager = MusicalRegions.getPlayListManager();
        RegionPlayList playList = playListManager.get(playListName);
        if (playList == null)
            throw new CommandException(Lang.get(_PLAYLIST_NOT_FOUND, playListName));

        region = regionManager.create(regionName, playList, regionSelection);
        if (region == null)
            throw new CommandException(Lang.get(_FAILED));

        tellSuccess(sender, Lang.get(_SUCCESS, regionName));
    }
}
