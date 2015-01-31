package com.jcwhatever.musical;

import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.nucleus.scripting.IEvaluatedScript;
import com.jcwhatever.nucleus.scripting.ScriptApiInfo;
import com.jcwhatever.nucleus.scripting.api.IScriptApiObject;
import com.jcwhatever.nucleus.scripting.api.NucleusScriptApi;
import com.jcwhatever.nucleus.sounds.ResourceSound;
import com.jcwhatever.nucleus.utils.PreCon;
import com.jcwhatever.nucleus.utils.player.PlayerUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Musical Regions script API.
 */
@ScriptApiInfo(
        variableName = "musicalRegions",
        description = "Script API access to MusicRegions plugin.")
public class MusicScriptApi extends NucleusScriptApi {

    /**
     * Constructor.
     *
     * @param plugin The owning plugin
     */
    public MusicScriptApi(Plugin plugin) {
        super(plugin);
    }

    @Override
    public IScriptApiObject getApiObject(IEvaluatedScript script) {
        return new ApiObject();
    }

    public static class ApiObject implements IScriptApiObject {

        boolean _isDisposed;

        @Override
        public boolean isDisposed() {
            return _isDisposed;
        }

        @Override
        public void dispose() {
            _isDisposed = true;
        }

        /**
         * Determine if a player is excluded from hearing a regions
         * playlist.
         *
         * @param regionName  The name of the region to check.
         * @param player      The player to check.
         */
        public boolean isExcluded(String regionName, Object player) {
            PreCon.notNullOrEmpty(regionName);
            PreCon.notNull(player);

            Player p = PlayerUtils.getPlayer(player);
            PreCon.notNull(p);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            return region.isExcluded(p);
        }

        /**
         * Exclude a player from hearing a regions playlist.
         *
         * @param regionName  The name of the region to exclude the player from.
         * @param player      The player to exclude.
         */
        public void excludePlayer(String regionName, Object player) {
            PreCon.notNullOrEmpty(regionName);
            PreCon.notNull(player);

            Player p = PlayerUtils.getPlayer(player);
            PreCon.notNull(p);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            region.exclude(p);
        }

        /**
         * Re-include a player to hear a regions playlist.
         *
         * @param regionName  The name of the region.
         * @param player      The player.
         */
        public void includePlayer(String regionName, Object player) {
            PreCon.notNullOrEmpty(regionName);
            PreCon.notNull(player);

            Player p = PlayerUtils.getPlayer(player);
            PreCon.notNull(p);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            region.include(p);
        }

        /**
         * Get a musical regions sound source location.
         *
         * @param regionName  The name of the region to check.
         */
        public Location getSoundSource(String regionName) {
            PreCon.notNullOrEmpty(regionName);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            return region.getSoundSource();
        }

        /**
         * Get a musical regions sound volume.
         *
         * @param regionName  The name of the region to check.
         */
        public float getSoundVolume(String regionName) {
            PreCon.notNullOrEmpty(regionName);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            return region.getSoundVolume();
        }

        /**
         * Get a musical regions sounds.
         *
         * @param regionName  The name of the region to check.
         */
        public List<ResourceSound> getSounds(String regionName) {
            PreCon.notNullOrEmpty(regionName);

            MusicRegion region = MusicalRegions.getRegionManager().get(regionName);
            PreCon.notNull(region);

            return region.getSounds();
        }

        /**
         * Get a playlist by name.
         *
         * @param listName  The name of the playlist.
         */
        public RegionPlayList getPlayList(String listName) {
            PreCon.notNullOrEmpty(listName);

            return MusicalRegions.getPlayListManager().get(listName);
        }
    }
}
