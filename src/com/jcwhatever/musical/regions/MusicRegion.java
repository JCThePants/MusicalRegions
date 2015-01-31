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


package com.jcwhatever.musical.regions;

import com.jcwhatever.musical.Msg;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.nucleus.regions.Region;
import com.jcwhatever.nucleus.regions.selection.IRegionSelection;
import com.jcwhatever.nucleus.sounds.PlayList;
import com.jcwhatever.nucleus.sounds.ResourceSound;
import com.jcwhatever.nucleus.sounds.SoundManager;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.ArrayUtils;
import com.jcwhatever.nucleus.utils.CollectionUtils;
import com.jcwhatever.nucleus.utils.PreCon;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

/**
 * A region that plays sounds to players that enter.
 */
public class MusicRegion extends Region {

    /**
     * Calculate the volume needed to encompass a region in a sphere
     * where the audio can be heard at full volume.
     *
     * @param region  The region to get a volume for.
     */
    public static float calculateVolume(IRegionSelection region) {
        return (float) ((Math.max(region.getXBlockWidth(), region.getZBlockWidth()) * Math.sqrt(3)) / 16D);
    }

    private PlayList _playList;
    private float _volumeFactor = 1.0f;

    /**
     * Constructor.
     *
     * @param name      The name of the region.
     * @param dataNode  The regions data node.
     */
    public MusicRegion(String name, IDataNode dataNode) {
        super(MusicalRegions.getPlugin(), name, dataNode);

        setEventListener(true);

        load();
    }

    /**
     * Get the regions playlist.
     */
    public PlayList getPlayList() {
        return _playList;
    }

    /**
     * Get the volume the sound is played at.
     */
    public float getSoundVolume() {
        return _playList.getSettings().getVolume();
    }

    /**
     * Get the factor applied to the regions pre-calculated audio volume value.
     *
     * <p>This is not the Minecraft audio volume. The value is factored into the default
     * volume calculations for the region and serves as a final adjustment.</p>
     */
    public float getSoundVolumeFactor() {
        return _volumeFactor;
    }

    /**
     * Set the factor applied to the regions pre-calculated audio volume value.
     *
     * <p>This is not the Minecraft audio volume. The value is factored into the default
     * volume calculations for the region and serves as a final adjustment.</p>
     *
     * @param factor  The factor to apply to the sound volume.
     */
    public void setSoundVolumeFactor(float factor) {
        _volumeFactor = factor;
        _playList.getSettings().setVolume(calculateVolume(this) * factor);

        IDataNode dataNode = getDataNode();
        assert dataNode != null;

        dataNode.set("volume", factor);
        dataNode.save();
    }

    /**
     * Get the resource sounds that the region plays.
     */
    public List<ResourceSound> getSounds() {
        return _playList.getSounds();
    }

    /**
     * Get the location the sound is played from.
     */
    public Location getSoundSource() {
        List<Location> locations = _playList.getSettings().getLocations();
        assert !locations.isEmpty();

        return locations.get(0);
    }

    /**
     * Set the location the sound is played from.
     *
     * @param location  The location to play the sound from. Must be in the
     *                  same world as the region.
     */
    public void setSoundSource(Location location) {
        PreCon.notNull(location);
        PreCon.isValid(getWorld() != null && getWorld().equals(location.getWorld()),
                "Sound source location must be in the same world as the region.");

        _playList.getSettings().clearLocations().addLocations(location);

        IDataNode dataNode = getDataNode();
        assert dataNode != null;

        dataNode.set("source", location);
        dataNode.save();
    }

    /**
     * Reset the sound source back to the regions center point.
     */
    public void resetSoundSource() {
        setSoundSource(getCenter());
    }

    /**
     * Erase the regions playlist and replace with a single resource sound.
     *
     * @param sound  The resource sound.
     */
    public void setSound(ResourceSound sound) {
        PreCon.notNull(sound);

        setSound(ArrayUtils.asList(sound));
    }

    /**
     * Erase the regions playlist and replace with the specified
     * resource sounds.
     *
     * @param sounds  The resource sounds.
     */
    public void setSound(Collection<ResourceSound> sounds) {
        PreCon.notNull(sounds);

        _playList.clearSounds();
        _playList.addSounds(sounds);

        IDataNode dataNode = getDataNode();
        assert dataNode != null;

        List<String> soundNames = new ArrayList<>(sounds.size());

        for (ResourceSound sound : sounds) {
            soundNames.add(sound.getName());
        }

        dataNode.set("sounds", soundNames);
        dataNode.save();
    }

    /**
     * Determine if the regions playlist plays in a loop.
     */
    public boolean isLoop() {
        return _playList.isLoop();
    }

    /**
     * Enable or disable playlist looping for the region.
     *
     * @param isLoop  True to enable, false to disable.
     */
    public void setLoop(boolean isLoop) {
        _playList.setLoop(isLoop);

        IDataNode dataNode = getDataNode();
        assert dataNode != null;

        dataNode.set("loop", isLoop);
    }

    @Override
    protected void onPlayerEnter(Player p, EnterRegionReason reason) {
        _playList.addPlayer(p);
    }

    @Override
    protected void onPlayerLeave(Player p, LeaveRegionReason reason) {
        _playList.removePlayer(p);
    }

    @Override
    protected void onCoordsChanged(@Nullable Location p1, @Nullable Location p2) {

        if (p1 == null || p2 == null)
            return;

        _playList.getSettings()
                .setVolume(calculateVolume(this) * _volumeFactor);
    }

    private void load() {

        IDataNode dataNode = getDataNode();

        assert dataNode != null;

        boolean isLoop = dataNode.getBoolean("loop");
        Location source = dataNode.getLocation("source", getCenter());
        _volumeFactor = (float)dataNode.getDouble("volume", _volumeFactor);

        //noinspection unchecked
        List<String> soundNames = dataNode.getStringList("sounds", CollectionUtils.UNMODIFIABLE_EMPTY_LIST);
        assert soundNames != null;

        List<ResourceSound> sounds = new ArrayList<>(soundNames.size());

        for (String soundName : soundNames) {

            ResourceSound sound = SoundManager.getSound(soundName.trim());
            if (sound == null) {
                Msg.debug("Failed to find resource sound '{0}' for musical region '{1}'", soundName, getName());
                continue;
            }

            sounds.add(sound);
        }

        _playList = new PlayList(MusicalRegions.getPlugin(), sounds);
        _playList.setLoop(isLoop);
        _playList.getSettings()
                .addLocations(source)
                .setVolume(calculateVolume(this) * _volumeFactor);
    }
}