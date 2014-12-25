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


package com.jcwhatever.bukkit.musical.regions;

import com.jcwhatever.generic.internal.Msg;
import com.jcwhatever.generic.regions.Region;
import com.jcwhatever.generic.sounds.PlayList;
import com.jcwhatever.generic.sounds.PlayList.PlayerSoundQueue;
import com.jcwhatever.generic.sounds.ResourceSound;
import com.jcwhatever.generic.sounds.SoundManager;
import com.jcwhatever.generic.storage.IDataNode;
import com.jcwhatever.generic.utils.PreCon;
import com.jcwhatever.generic.utils.text.TextUtils;
import com.jcwhatever.bukkit.musical.MusicalRegions;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MusicRegion extends Region {
	
	private PlayList _playList;
    private IDataNode _dataNode;
		
	public MusicRegion(String name, IDataNode dataNode) {
		super(MusicalRegions.getPlugin(), name, dataNode);

        PreCon.notNull(dataNode);

        _dataNode = dataNode;

		setEventListener(true);

        loadSettings();
	}
	
	
	@Override
	protected void onPlayerEnter(Player p, EnterRegionReason reason) {
		_playList.addPlayer(p);
	}
	
	@Override
	protected boolean canDoPlayerEnter(Player p, EnterRegionReason reason) {

        PlayerSoundQueue queue = _playList.getSoundQueue(p);
        return queue == null && super.canDoPlayerEnter(p, reason);
    }
	
	@Override
	protected void onPlayerLeave(Player p, LeaveRegionReason reason) {
		if (!p.getWorld().equals(this.getWorld()))
            _playList.removePlayer(p);
	}
	
	public PlayList getPlayList() {
		return _playList;
	}
	
	public void setSound(ResourceSound sound) {
        _playList.clearSounds();
		_playList.addSound(sound);
	}
	
	public void setSound(Collection<ResourceSound> sounds) {

        _playList.clearSounds();
        _playList.addSounds(sounds);
	}
	
	public boolean isLoop() {
		return _playList.isLoop();
	}
	
	public void setIsLoop(boolean isLoop) {
		_playList.setLoop(isLoop);
	}

    private void loadSettings() {

        boolean isLoop = _dataNode.getBoolean("loop");

        String rawSounds = _dataNode.getString("resource-sound");

        String[] soundNames = TextUtils.PATTERN_COMMA.split(rawSounds);
        List<ResourceSound> sounds = new ArrayList<>(soundNames.length);

        for (String soundName : soundNames) {

            ResourceSound sound = SoundManager.getSound(soundName.trim());
            if (sound == null) {
                Msg.debug("Failed to find resource sound '{0}' for musical region '{1}'", soundName, getName());
            }

            sounds.add(sound);
        }

        _playList = new PlayList(MusicalRegions.getPlugin(), sounds);
        _playList.setLoop(isLoop);
        _playList.setLocation(getCenter());
        _playList.setVolume(Math.max(1, (Math.max(getXBlockWidth(), getZBlockWidth())) / 16));

    }

}
