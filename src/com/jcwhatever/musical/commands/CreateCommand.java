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


package com.jcwhatever.musical.commands;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.regions.selection.IRegionSelection;
import com.jcwhatever.nucleus.sounds.ResourceSound;
import com.jcwhatever.nucleus.sounds.SoundManager;
import com.jcwhatever.nucleus.utils.language.Localizable;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


@CommandInfo(
        command="create",
        staticParams={"regionName", "soundNames"},
        description="Create a musical region using your current region selection.",
        paramDescriptions = {
                "regionName= The name of the region. {NAME16}",
                "soundNames= A comma delimited list of resource sounds that the region will play."
        })

public class CreateCommand extends AbstractCommand {

    @Localizable static final String _REGION_ALREADY_EXISTS =
            "There is already a musical region with the name '{0: region name}'.";

    @Localizable static final String _SOUND_NOT_FOUND =
            "Failed to find resource sound named '{0: sound name}'.";

    @Localizable static final String _FAILED =
            "Failed to create a new region.";

    @Localizable static final String _SUCCESS =
            "New musical region named '{0: region name}' created.";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws CommandException{

        CommandException.checkNotConsole(this, sender);

        String regionName = args.getName("regionName");
        String soundNames = args.getString("soundNames");

        IRegionSelection regionSelection = getRegionSelection((Player) sender);
        if (regionSelection == null)
            return; // finish

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region != null) {
            tellError(sender, Lang.get(_REGION_ALREADY_EXISTS, regionName));
            return; // finish
        }

        String[] names = TextUtils.PATTERN_COMMA.split(soundNames);
        List<ResourceSound> sounds = new ArrayList<>(names.length);

        for (String name : names) {
            ResourceSound sound = SoundManager.getSound(name.trim());
            if (sound == null) {
                tellError(sender, Lang.get(_SOUND_NOT_FOUND, name));
            }

            sounds.add(sound);
        }

        region = regionManager.create(regionName, sounds, regionSelection);
        if (region == null) {
            tellError(sender, Lang.get(_FAILED));
            return; // finish
        }

        tellSuccess(sender, Lang.get(_SUCCESS, regionName));
    }
}
