package com.jcwhatever.bukkit.musical.regions;

import com.jcwhatever.bukkit.generic.messaging.Messenger;
import com.jcwhatever.bukkit.generic.regions.Region;
import com.jcwhatever.bukkit.generic.sounds.PlayList;
import com.jcwhatever.bukkit.generic.sounds.PlayList.PlayerSoundQueue;
import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.generic.sounds.SoundManager;
import com.jcwhatever.bukkit.generic.storage.IDataNode;
import com.jcwhatever.bukkit.generic.utils.TextUtils;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MusicRegion extends Region {
	
	private PlayList _playList;
		
	public MusicRegion(String name, IDataNode settings) {
		super(MusicalRegions.getInstance(), name, settings);
		
		setIsPlayerWatcher(true);

        loadSettings();
	}
	
	
	@Override
	protected void onPlayerEnter(Player p) {
		_playList.addPlayer(p);
	}
	
	@Override
	protected boolean canDoPlayerEnter(Player p) {

        PlayerSoundQueue queue = _playList.getSoundQueue(p);
        return queue == null && super.canDoPlayerEnter(p);
    }
	
	@Override
	protected void onPlayerLeave(Player p) {
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
                Messenger.debug(_plugin, "Failed to find resource sound '{0}' for musical region '{1}'", soundName, getName());
            }

            sounds.add(sound);
        }

        _playList = new PlayList(MusicalRegions.getInstance(), sounds);
        _playList.setLoop(isLoop);
        _playList.setLocation(getCenter());
        _playList.setVolume(Math.max(1, (Math.max(getXBlockWidth(), getZBlockWidth())) / 16));

    }

}
