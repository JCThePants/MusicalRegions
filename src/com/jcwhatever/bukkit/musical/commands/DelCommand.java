package com.jcwhatever.bukkit.musical.commands;

import org.bukkit.command.CommandSender;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.ICommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;

@ICommandInfo(
		command="del", 
		staticParams={"regionName"},
		usage="/musical del <regionName>",
		description="Delete a musical region.")

public class DelCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
		
		String regionName = args.getName("regionName");
		
		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region == null) {
			tellError(sender, "A musical region with the name '{0}' was not found.", regionName);
			return; // finish
		}
		
		if (!regionManager.delete(regionName)) {
			tellError(sender, "Failed to delete musical region '{0}'.", regionName);
		}
		
		tellSuccess(sender, "Musical region '{0}' deleted.", regionName);
	}
	

}