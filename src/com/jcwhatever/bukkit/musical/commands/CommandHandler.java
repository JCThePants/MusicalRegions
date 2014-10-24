package com.jcwhatever.bukkit.musical.commands;

import com.jcwhatever.bukkit.generic.commands.AbstractCommandHandler;
import com.jcwhatever.bukkit.musical.MusicalRegions;

public class CommandHandler extends AbstractCommandHandler {

	public CommandHandler() {
		super(MusicalRegions.getInstance());
	}

	@Override
	protected void registerCommands() {
		this.registerCommand(CreateCommand.class);
		this.registerCommand(DelCommand.class);
		this.registerCommand(ListCommand.class);
		this.registerCommand(ListMusicCommand.class);
		this.registerCommand(LoopCommand.class);
		this.registerCommand(SetRegionCommand.class);
		this.registerCommand(SetSoundCommand.class);
		
	}

}
