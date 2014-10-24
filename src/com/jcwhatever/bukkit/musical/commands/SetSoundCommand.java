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
		
		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();
		
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
