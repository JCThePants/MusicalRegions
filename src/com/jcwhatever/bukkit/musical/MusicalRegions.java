package com.jcwhatever.bukkit.musical;

import com.jcwhatever.bukkit.generic.GenericsPlugin;
import com.jcwhatever.bukkit.musical.commands.CommandHandler;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.ChatColor;

public class MusicalRegions extends GenericsPlugin {

	private static MusicalRegions _instance;
	
	private RegionManager _regionManager; 
	
	public static MusicalRegions getInstance() {
		return _instance;
	}
	
	public MusicalRegions() {
		super();
	}
	
	@Override
	protected void init() {
		_instance = this;
	}

    @Override
	public String getChatPrefix() {
		return ChatColor.LIGHT_PURPLE + "[Music] " + ChatColor.RESET;
	}

	@Override
	public String getConsolePrefix() {
		return "[Music] ";
	}

	
	public RegionManager getRegionManager() {
		return _regionManager;
	}

    @Override
    protected void onEnablePlugin() {
        registerCommands(new CommandHandler());
        registerEventListeners(new EventListener());

        _regionManager = new RegionManager(this.getSettings().getNode("regions"));
    }

    @Override
    protected void onDisablePlugin() {

    }




}
