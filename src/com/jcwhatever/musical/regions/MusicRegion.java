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

import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.events.RegionPlayEvent;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.collections.players.PlayerMap;
import com.jcwhatever.nucleus.collections.players.PlayerSet;
import com.jcwhatever.nucleus.regions.Region;
import com.jcwhatever.nucleus.regions.options.EnterRegionReason;
import com.jcwhatever.nucleus.regions.options.LeaveRegionReason;
import com.jcwhatever.nucleus.providers.regionselect.IRegionSelection;
import com.jcwhatever.nucleus.managed.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.managed.sounds.SoundSettings;
import com.jcwhatever.nucleus.managed.sounds.playlist.PlayList;
import com.jcwhatever.nucleus.managed.sounds.playlist.PlayList.PlayerSoundQueue;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.MetaKey;
import com.jcwhatever.nucleus.utils.PreCon;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 * A region that plays sounds to players that enter.
 */
public class MusicRegion extends Region {

    public static final MetaKey<MusicRegion> META_KEY = new MetaKey<>(MusicRegion.class);

    /**
     * Calculate the volume needed to encompass a region in a sphere
     * where the audio can be heard at full volume.
     *
     * @param region  The region to get a volume for.
     */
    public static float calculateVolume(IRegionSelection region) {
        return (float) ((Math.max(region.getXBlockWidth(), region.getZBlockWidth()) * Math.sqrt(3)) / 16D);
    }

    private RegionPlayList _playList;
    private float _volumeFactor = 1.0f;
    private Set<Player> _exclude;
    private SoundSettings _settings = new SoundSettings();
    private Map<UUID, PlayList> _playLists = new PlayerMap<PlayList>(MusicalRegions.getPlugin());

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
    public RegionPlayList getPlayList() {
        return _playList;
    }

    /**
     * Get the volume the sound is played at.
     */
    public float getSoundVolume() {
        return _settings.getVolume();
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
        _settings.setVolume(calculateVolume(this) * factor);

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
        List<Location> locations = _settings.getLocations();
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

        _settings.clearLocations().addLocations(location);

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
     * Set the default playlist for the region.
     *
     * @param playList  The playlist.
     */
    public void setPlayList(RegionPlayList playList) {
        PreCon.notNull(playList);

        _playList = playList;

        IDataNode dataNode = getDataNode();
        assert dataNode != null;

        dataNode.set("playlist", playList.getName());
        dataNode.save();
    }


    /**
     * Determine if the regions playlist plays in a loop.
     */
    public boolean isLoop() {
        return _playList != null && _playList.isLoop();
    }

    /**
     * Determine if a player is excluded from hearing the regions
     * playlist.
     *
     * @param player  The player to check.
     */
    public boolean isExcluded(Player player) {
        return _exclude != null && _exclude.contains(player);
    }

    /**
     * Exclude a player from hearing the regions playlist.
     *
     * <p>The exclusion is transient. The player is removed from exclusion
     * when the player logs out of the server.</p>
     *
     * @param player  The player to exclude.
     */
    public void exclude(Player player) {

        if (_exclude == null)
            _exclude = new PlayerSet(MusicalRegions.getPlugin());

        _exclude.add(player);
    }

    /**
     * Remove a player from exclusion.
     *
     * @param player  The player to include.
     */
    public void include(Player player) {

        if (_exclude == null)
            return;

        _exclude.remove(player);
    }

    @Override
    protected void onPlayerEnter(Player player, EnterRegionReason reason) {

        if (isExcluded(player) || _playList == null)
            return;

        PlayList playList = _playLists.get(player.getUniqueId());
        if (playList != null) {

            // don't play playlist if player is already listening to a queue
            PlayerSoundQueue queue = playList.getSoundQueue(player);
            if (queue != null)
                return;
        }

        RegionPlayEvent event = new RegionPlayEvent(player, this, _playList);
        Nucleus.getEventManager().callBukkit(this, event);
        if (event.isCancelled())
            return;

        PlayerSoundQueue queue = event.getPlayList().addPlayer(player, _settings);
        if (queue != null) {
            queue.getMeta().set(META_KEY, this);

            _playLists.put(player.getUniqueId(), event.getPlayList());
        }
    }

    @Override
    protected void onPlayerLeave(Player player, LeaveRegionReason reason) {

        PlayList playList = _playLists.remove(player.getUniqueId());
        if (playList == null)
            return;

        playList.removePlayer(player);
    }

    @Override
    protected void onCoordsChanged(@Nullable Location p1, @Nullable Location p2) {

        if (p1 == null || p2 == null)
            return;

        _settings.setVolume(calculateVolume(this) * _volumeFactor);
    }

    private void load() {

        IDataNode dataNode = getDataNode();

        assert dataNode != null;

        Location source = dataNode.getLocation("source", getCenter());
        String playList = dataNode.getString("playlist");
        _volumeFactor = (float)dataNode.getDouble("volume", _volumeFactor);

        if (playList != null)
            _playList = MusicalRegions.getPlayListManager().get(playList);

        _settings.addLocations(source)
                .setVolume(calculateVolume(this) * _volumeFactor);
    }
}