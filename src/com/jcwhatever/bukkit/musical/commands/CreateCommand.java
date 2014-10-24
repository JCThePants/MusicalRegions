package com.jcwhatever.bukkit.musical.commands;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.ICommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidCommandSenderException;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidCommandSenderException.CommandSenderType;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.generic.regions.RegionSelection;
import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.generic.sounds.SoundManager;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@ICommandInfo(
		command="create", 
		staticParams={"regionName", "songName"},
		usage="/musical create <regionName> <songName>",
		description="Create a musical region whose cuboid region is the current World Edit selection.")

public class CreateCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args)
	        throws InvalidValueException, InvalidCommandSenderException {
		
	    InvalidCommandSenderException.check(sender, CommandSenderType.PLAYER, "Console cannot select regions.");
		
		if (!isWorldEditInstalled(sender))
			return; // finish
		
		String regionName = args.getName("regionName");
		String songName = args.getString("songName");
		
		
		ResourceSound sound = SoundManager.getSound(songName);
		if (sound == null) {
			tellError(sender, "The resource sound '{0}' was not found.", songName);
			return; // finish
		}
		
		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();
		
		MusicRegion region = regionManager.getRegion(regionName);
		if (region != null) {
			tellError(sender, "There is already a musical region with the name '{0}'.", regionName);
			return; // finish
		}
		
		RegionSelection sel = getWorldEditSelection((Player)sender);
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
