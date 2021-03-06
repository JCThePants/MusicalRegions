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


import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.events.sounds.PlayResourceSoundEvent;
import com.jcwhatever.nucleus.managed.actionbar.ActionBars;
import com.jcwhatever.nucleus.managed.actionbar.IActionBar;
import com.jcwhatever.nucleus.managed.resourcepacks.sounds.playlist.PlayList;
import com.jcwhatever.nucleus.managed.resourcepacks.sounds.types.IMusicSound;
import com.jcwhatever.nucleus.managed.resourcepacks.sounds.types.IResourceSound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Listens to Bukkit events.
 */
public class BukkitEventListener implements Listener {

    private Map<String, IActionBar> _actionBars = new HashMap<>(10);

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayResourceSound(PlayResourceSoundEvent event) {

        if (!(event.getResourceSound() instanceof IMusicSound))
            return;

        IResourceSound sound = event.getResourceSound();

        IActionBar actionBar = _actionBars.get(sound.getName());
        if (actionBar == null) {

            String msg = MusicalRegions.getPlugin().getChatPrefix() + '\'' + sound.getTitle() + '\'';

            if (sound.getCredit() != null) {
                msg += " - By " + sound.getCredit();
            }

            actionBar = ActionBars.create(msg);
            _actionBars.put(sound.getName(), actionBar);
        }

        Player player = event.getPlayer();
        actionBar.showTo(player);
    }

    /**
     * Remove player from music playlist on death since sound
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

            if (playList instanceof MusicPlayList) {
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

        MusicalRegions.getWorldManager().onEnterWorld(player, player.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerTeleport(PlayerTeleportEvent event) {

        if (event.getTo().getWorld().equals(event.getFrom().getWorld()))
            return;

        Player player = event.getPlayer();

        MusicalRegions.getWorldManager().onLeaveWorld(player, event.getFrom().getWorld());
        MusicalRegions.getWorldManager().onEnterWorld(player, event.getTo().getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        MusicalRegions.getWorldManager()
                .onEnterWorld(player, player.getWorld());
    }
}
