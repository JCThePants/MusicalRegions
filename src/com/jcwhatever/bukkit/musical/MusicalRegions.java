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


package com.jcwhatever.bukkit.musical;

import com.jcwhatever.generic.GenericsPlugin;
import com.jcwhatever.bukkit.musical.commands.MusicalCommandDispatcher;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.ChatColor;

public class MusicalRegions extends GenericsPlugin {

	private static MusicalRegions _instance;
	
	private RegionManager _regionManager; 
	
	public static MusicalRegions getPlugin() {
		return _instance;
	}
	
	public MusicalRegions() {
		super();
	}
	
	@Override
	protected void onInit() {
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
        registerCommands(new MusicalCommandDispatcher());
        registerEventListeners(new EventListener());

        _regionManager = new RegionManager(this.getDataNode().getNode("regions"));
    }

    @Override
    protected void onDisablePlugin() {

    }




}
