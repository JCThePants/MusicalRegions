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

import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.regions.selection.IRegionSelection;
import com.jcwhatever.nucleus.storage.DataBatchOperation;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.PreCon;
import com.jcwhatever.nucleus.utils.managers.NamedInsensitiveDataManager;

import javax.annotation.Nullable;

/**
 * Manages musical regions
 */
public class RegionManager extends NamedInsensitiveDataManager<MusicRegion> {

    /**
     * Constructor.
     *
     * @param dataNode The data node.
     */
    public RegionManager(IDataNode dataNode) {
        super(dataNode, true);
    }

    /**
     * Create a new musical region.
     *
     * @param regionName  The name of the region.
     * @param playList    The playlist to use.
     * @param selection   The regions coordinates.
     *
     * @return  The new region instance or null if failed.
     */
    public MusicRegion create(String regionName,
                              final RegionPlayList playList,
                              final IRegionSelection selection) {

        PreCon.notNullOrEmpty(regionName);
        PreCon.notNull(playList);
        PreCon.notNull(selection);

        assert _dataNode != null;

        final MusicRegion region = new MusicRegion(regionName, _dataNode.getNode(regionName));

        _dataNode.runBatchOperation(new DataBatchOperation() {

            @Override
            public void run(IDataNode dataNode) {
                region.setCoords(selection.getP1(), selection.getP2());
                region.setPlayList(playList);
                region.resetSoundSource();
            }
        });

        add(region);

        return region;
    }

    @Nullable
    @Override
    protected MusicRegion load(String name, IDataNode regionNode) {
        return new MusicRegion(name, regionNode);
    }

    @Override
    protected void save(MusicRegion item, IDataNode itemNode) {
        // do nothing.
    }

    @Override
    protected void onRemove(MusicRegion removed) {
        super.onRemove(removed);

        removed.dispose();
    }
}
