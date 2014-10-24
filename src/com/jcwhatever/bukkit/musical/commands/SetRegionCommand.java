package com.jcwhatever.bukkit.musical.commands;

import com.jcwhatever.bukkit.generic.regions.RegionSelection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.ICommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidCommandSenderException;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidCommandSenderException.CommandSenderType;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;

@ICommandInfo(
		command="setregion", 
		staticParams={"regionName"},
		usage="/musical setregion <regionName>",
		description="Change a musical regions cuboid region using the current World Edit selection.")

public class SetRegionCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args)
	        throws InvalidValueException, InvalidCommandSenderException {
		
	    InvalidCommandSenderException.check(sender, CommandSenderType.PLAYER, "Console cannot select regions.");
		
		if (!isWorldEditInstalled(sender))
			return; // finish
		
		String regionName = args.getName("regionName");
		
		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region == null) {
			tellError(sender, "A musical region with the name '" + regionName + "' was not found.");
			return; // finish
		}
		
		RegionSelection sel = getWorldEditSelection((Player)sender);
		if (sel == null)
			return; // finish
		
		region.setCoords(sel.getP1(), sel.getP2());
		
		tellSuccess(sender, "Region changed for '" + regionName + "'.");
	}
	
}