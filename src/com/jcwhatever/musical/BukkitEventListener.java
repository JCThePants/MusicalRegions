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


package com.jcwhatever.musical;


import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.utils.actionbar.ActionBar;
import com.jcwhatever.nucleus.events.sounds.PlayResourceSoundEvent;
import com.jcwhatever.nucleus.managed.sounds.types.MusicSound;
import com.jcwhatever.nucleus.managed.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.managed.sounds.playlist.PlayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Listens to Bukkit events.
 */
public class BukkitEventListener implements Listener {

    private Map<String, ActionBar> _actionBars = new HashMap<>(10);

    @EventHandler()
    private void onPlayResourceSound(PlayResourceSoundEvent event) {

        if (!(event.getResourceSound() instanceof MusicSound))
            return;

        ResourceSound sound = event.getResourceSound();

        ActionBar actionBar = _actionBars.get(sound.getName());
        if (actionBar == null) {

            String msg = MusicalRegions.getPlugin().getChatPrefix() + '\'' + sound.getTitle() + '\'';

            if (sound.getCredit() != null) {
                msg += " - By " + sound.getCredit();
            }

            actionBar = new ActionBar(msg);
            _actionBars.put(sound.getName(), actionBar);
        }

        Player p = event.getPlayer();
        actionBar.show(p);
    }

    /**
     * Remove player from region playlist on death since sound
     * ends on death.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        // check for "cancelled" event
        if (player.getHealth() > 0.0D)
            return;

        List<PlayList> playLists = PlayList.getAll(player);
        if (playLists.isEmpty())
            return;

        for (PlayList playList : playLists) {

            if (playList instanceof RegionPlayList) {
                playList.removePlayer(player, true);
            }
        }
    }

    /**
     * Make {@link MusicRegion}'s the player respawns within forget the player
     * in case the player was already in the region. Causes the region to
     * replay playlist to player.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        List<MusicRegion> regions = Nucleus.getRegionManager().getRegions(
                event.getRespawnLocation(), MusicRegion.class);

        for (MusicRegion region : regions) {
            Nucleus.getRegionManager().forgetPlayer(player, region);
        }
    }
}
