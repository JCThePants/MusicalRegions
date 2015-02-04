package com.jcwhatever.musical.events;

import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.nucleus.mixins.IPlayerReference;
import com.jcwhatever.nucleus.sounds.playlist.PlayList;
import com.jcwhatever.nucleus.utils.PreCon;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that is called when a region plays a playlist to a player.
 */
public class RegionPlayEvent extends Event implements Cancellable, IPlayerReference {

    private static final HandlerList handlers = new HandlerList();

    private final Player _player;
    private final MusicRegion _region;
    private PlayList _playList;
    private boolean _isCancelled;

    /**
     * Constructors.
     *
     * @param player    The player that will be added to a playlist.
     * @param region    The region that the player moved into.
     * @param playList  The playlist the player will be added to.
     */
    public RegionPlayEvent(Player player, MusicRegion region, PlayList playList) {
        _player = player;
        _region = region;
        _playList = playList;
    }

    @Override
    public Player getPlayer() {
        return _player;
    }

    /**
     * Get the region.
     */
    public MusicRegion getRegion() {
        return _region;
    }

    /**
     * Get the playlist the player is to be added to.
     */
    public PlayList getPlayList() {
        return _playList;
    }

    /**
     * Set the playlist the player is to be added to.
     *
     * @param playList  The playlist.
     */
    public void setPlayList(PlayList playList) {
        PreCon.notNull(playList);

        _playList = playList;
    }

    @Override
    public boolean isCancelled() {
        return _isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        _isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
