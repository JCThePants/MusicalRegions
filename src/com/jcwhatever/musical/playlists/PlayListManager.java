package com.jcwhatever.musical.playlists;

import com.jcwhatever.nucleus.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.PreCon;
import com.jcwhatever.nucleus.utils.managers.NamedInsensitiveDataManager;

import java.util.Collection;
import javax.annotation.Nullable;

/**
 * Manages play lists.
 */
public class PlayListManager extends NamedInsensitiveDataManager<RegionPlayList> {
    /**
     * Constructor.
     *
     * @param dataNode The data node.
     */
    public PlayListManager(IDataNode dataNode) {
        super(dataNode, true);
    }

    /**
     * Create a new play list.
     *
     * @param name    The name of the playlist.
     * @param sounds  The sounds to add to the playlist.
     *
     * @return  The new play list or null if a playlist by the specified name
     * already exists.
     */
    public RegionPlayList create(String name, Collection<ResourceSound> sounds) {
        PreCon.notNullOrEmpty(name);
        PreCon.notNull(sounds);

        if (contains(name))
            return null;

        assert _dataNode != null;
        IDataNode dataNode = _dataNode.getNode(name);

        RegionPlayList playList = new RegionPlayList(name, dataNode);
        playList.addSounds(sounds);

        add(playList);

        return playList;
    }

    @Nullable
    @Override
    protected RegionPlayList load(String name, IDataNode itemNode) {
        return new RegionPlayList(name, itemNode);
    }

    @Override
    protected void save(RegionPlayList item, IDataNode itemNode) {
        // do nothing
    }

}
