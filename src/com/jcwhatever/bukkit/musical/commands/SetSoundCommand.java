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


package com.jcwhatever.bukkit.musical.commands;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.ICommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.generic.sounds.SoundManager;
import com.jcwhatever.bukkit.generic.utils.TextUtils;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@ICommandInfo(
		command="setsound", 
		staticParams={"regionName", "soundId"},
		usage="/musical setsound <regionName> <soundId>",
		description="Change a musical regions sound.")

public class SetSoundCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
		
		String regionName = args.getName("regionName");
		String rawSoundNames = args.getString("soundId");
		
		RegionManager regionManager = MusicalRegions.getPlugin().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region == null) {
			tellError(sender, "A musical region with the name '{0}' was not found.", regionName);
			return; // finish
		}
		
		
		String[] soundNames = TextUtils.PATTERN_COMMA.split(rawSoundNames);
		List<ResourceSound> sounds = new ArrayList<ResourceSound>(soundNames.length);
		
		for (String soundName : soundNames) {
			ResourceSound sound = SoundManager.getSound(soundName);
			if (sound == null) {
				tellError(sender, "A resource sound with id '{0}' was not found.", soundName);
				return; // finish
			}
			sounds.add(sound);
		}
				
		
		region.setSound(sounds);
		
		tellSuccess(sender, "Region sounds changed for '" + regionName + "'.");
	}

}
