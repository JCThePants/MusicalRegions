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


package com.jcwhatever.musical;

import com.jcwhatever.musical.commands.MusicCommandDispatcher;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.NucleusPlugin;
import com.jcwhatever.nucleus.mixins.IDisposable;
import com.jcwhatever.nucleus.managed.scripting.IEvaluatedScript;
import com.jcwhatever.nucleus.managed.scripting.IScriptApi;
import com.jcwhatever.nucleus.managed.scripting.SimpleScriptApi;
import com.jcwhatever.nucleus.managed.scripting.SimpleScriptApi.IApiObjectCreator;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

/**
 * Musical Regions plugin.
 *
 * <p>Uses NucleusFramework sounds and regions to play music to players that
 * enter a musical region.</p>
 */
public class MusicalRegions extends NucleusPlugin {

    private static MusicalRegions _instance;

    private PlayListManager _playListManager;
    private RegionManager _regionManager;
    private IScriptApi _scriptApi;

    public static MusicalRegions getPlugin() {
        return _instance;
    }

    public static RegionManager getRegionManager() {
        return _instance._regionManager;
    }

    public static PlayListManager getPlayListManager() {
        return _instance._playListManager;
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

    @Override
    protected void onEnablePlugin() {

        _instance = this;

        _playListManager = new PlayListManager(this.getDataNode().getNode("playlists"));
        _regionManager = new RegionManager(this.getDataNode().getNode("regions"));

        registerCommands(new MusicCommandDispatcher());
        registerEventListeners(new BukkitEventListener());

        _scriptApi = new SimpleScriptApi(this, "musicalRegions", new IApiObjectCreator() {
            @Override
            public IDisposable create(Plugin plugin, IEvaluatedScript script) {
                return new MusicScriptApi();
            }
        });

        Nucleus.getScriptApiRepo().registerApi(_scriptApi);
    }

    @Override
    protected void onDisablePlugin() {

        Nucleus.getScriptApiRepo().unregisterApi(_scriptApi);

        _instance = null;
    }
}
