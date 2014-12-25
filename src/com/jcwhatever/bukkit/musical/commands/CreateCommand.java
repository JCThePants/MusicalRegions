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

import com.jcwhatever.generic.commands.AbstractCommand;
import com.jcwhatever.generic.commands.CommandInfo;
import com.jcwhatever.generic.commands.arguments.CommandArguments;
import com.jcwhatever.generic.commands.exceptions.CommandException;
import com.jcwhatever.generic.regions.selection.IRegionSelection;
import com.jcwhatever.generic.sounds.ResourceSound;
import com.jcwhatever.generic.sounds.SoundManager;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandInfo(
		command="create", 
		staticParams={"regionName", "songName"},
		description="Create a musical region using your current region selection.")

public class CreateCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws CommandException{

		CommandException.assertNotConsole(this, sender);

		String regionName = args.getName("regionName");
		String songName = args.getString("songName");
		
		
		ResourceSound sound = SoundManager.getSound(songName);
		if (sound == null) {
			tellError(sender, "The resource sound '{0}' was not found.", songName);
			return; // finish
		}
		
		RegionManager regionManager = MusicalRegions.getPlugin().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region != null) {
			tellError(sender, "There is already a musical region with the name '{0}'.", regionName);
			return; // finish
		}
		
		IRegionSelection sel = getRegionSelection((Player) sender);
		if (sel == null)
			return; // finish
		
		region = regionManager.create(regionName, sound, sel.getP1(), sel.getP2());
		
		if (region == null) {
			tellError(sender, "Failed to create a new region.");
			return; // finish
		}
		
		tellSuccess(sender, "New musical region named '{0}' created.", regionName);
	}

}
