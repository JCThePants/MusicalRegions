package com.jcwhatever.musical.worlds;

import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.nucleus.managed.sounds.SoundSettings;
import com.jcwhatever.nucleus.mixins.INamedInsensitive;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.PreCon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents a world playlist binding.
 */
public class MusicWorld implements INamedInsensitive {

    private final String _name;
    private final String _searchName;
    private final IDataNode _dataNode;
    private final SoundSettings _settings = new SoundSettings().setVolume(Integer.MAX_VALUE);

    private World _world;
    private String _playListName;

    /**
     * Constructor.
     *
     * <p>Used for loading an existing world binding.</p>
     *
     * @param name      The name of the world.
     * @param dataNode  The settings data node.
     */
    public MusicWorld(String name, IDataNode dataNode) {
        _name = name;
        _searchName = name.toLowerCase();
        _dataNode = dataNode;

        load();
    }

    /**
     * Constructor.
     *
     * <p>Used to load a new world binding.</p>
     *
     * @param world         The world.
     * @param playListName  The name of the play list.
     * @param dataNode      The settings data node.
     */
    public MusicWorld(World world, String playListName, IDataNode dataNode) {
        _name = world.getName();
        _searchName = _name.toLowerCase();
        _playListName = playListName;
        _dataNode = dataNode;

        _dataNode.set("playlist", playListName);
        _dataNode.save();
    }

    /**
     * Add a player to the world play list.
     *
     * @param player  The player.
     */
    public void addPlayer(Player player) {
        PreCon.notNull(player);

        if (world() == null)
            return;

        MusicPlayList playList = MusicalRegions.getPlayListManager().get(_playListName);
        if (playList == null)
            return;

        playList.addPlayer(player, _settings);
    }

    /**
     * Remove a player from the world play list.
     *
     * @param player  The player.
     */
    public void removePlayer(Player player) {
        PreCon.notNull(player);

        MusicPlayList playList = MusicalRegions.getPlayListManager().get(_playListName);
        if (playList == null)
            return;

        playList.removePlayer(player, true);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String getSearchName() {
        return _searchName;
    }

    public String getPlaylistName() {
        return _playListName;
    }

    public long getTrackChangeDelay() {
        return _settings.getTrackChangeDelay();
    }

    public void setTrackChangeDelay(long ticks) {
        _settings.setTrackChangeDelay(ticks);
    }

    public void setPlaylistName(String playlistName) {
        PreCon.notNull(playlistName);

        _dataNode.set("playlist", _playListName = playlistName);
    }

    private void load() {
        _playListName = _dataNode.getString("playlist");
        world();
    }

    private World world() {
        if (_world == null) {
            _world = Bukkit.getWorld(_name);
            _settings.addLocations(new Location(_world, 0, 0, 0));
        }

        return _world;
    }
}
