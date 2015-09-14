package com.jcwhatever.musical.worlds;

import javax.annotation.Nullable;

import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.PreCon;
import com.jcwhatever.nucleus.utils.managers.NamedInsensitiveDataManager;

import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Manages world play list bindings.
 */
public class WorldManager extends NamedInsensitiveDataManager<MusicWorld> {

    /**
     * Constructor.
     *
     * @param dataNode The data node.
     */
    public WorldManager(IDataNode dataNode) {
        super(dataNode, true);
    }

    /**
     * Create a new world playlist binding.
     *
     * @param world     The world.
     * @param playList  The play list.
     *
     * @return  The music world playlist binding that was created or null
     * if the world already has a binding.
     */
    @Nullable
    public MusicWorld create(World world, MusicPlayList playList) {
        PreCon.notNull(world);
        PreCon.notNull(playList);

        if (contains(world.getName()))
            return null;

        MusicWorld musicWorld = new MusicWorld(world,
                playList.getName(), getNode(world.getName()));
        add(musicWorld);
        return musicWorld;
    }

    /**
     * Invoke when a player enters a world.
     *
     * @param player  The player.
     * @param world   The world.
     */
    public void onEnterWorld(Player player, World world) {
        PreCon.notNull(player);
        PreCon.notNull(world);

        MusicWorld musicWorld = get(world.getName());
        if (musicWorld == null)
            return;

        musicWorld.addPlayer(player);
    }

    /**
     * Invoke when a player leaves a world.
     *
     * @param player  The player.
     * @param world   The world.
     */
    public void onLeaveWorld(Player player, World world) {
        PreCon.notNull(player);
        PreCon.notNull(world);

        MusicWorld musicWorld = get(world.getName());
        if (musicWorld == null)
            return;

        musicWorld.removePlayer(player);
    }

    @Nullable
    @Override
    protected MusicWorld load(String name, IDataNode itemNode) {
        return new MusicWorld(name, itemNode);
    }

    @Override
    protected void save(MusicWorld item, IDataNode itemNode) {
        // do nothing
    }
}
