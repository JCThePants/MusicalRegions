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
		command="loop", 
		staticParams={"regionName", "isEnabled"},
		usage="/musical loop <regionName> <isEnabled>",
		description="Set if a music region loops.")

public class LoopCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
		
		String regionName = args.getName("regionName");
		boolean isEnabled = args.getBoolean("isEnabled");
		
		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region == null) {
			tellError(sender, "A musical region with the name '{0}' was not found.", regionName);
			return; // finish
		}
		
		region.setIsLoop(isEnabled);
		
		tellEnabled(sender, "Musical region '{0}' looping is {e}.", isEnabled, regionName);		
	}
	
}
