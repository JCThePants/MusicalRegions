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


package com.jcwhatever.bukkit.musical;


import com.jcwhatever.generic.events.sounds.PlayResourceSoundEvent;
import com.jcwhatever.generic.internal.Msg;
import com.jcwhatever.generic.sounds.MusicSound;
import com.jcwhatever.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.musical.regions.RegionManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventListener implements Listener {

    RegionManager _regionManager;

    public EventListener () {
        _regionManager = MusicalRegions.getPlugin().getRegionManager();
    }


    @EventHandler()
    private void onPlayResourceSound(PlayResourceSoundEvent event) {

        if (!(event.getResourceSound() instanceof MusicSound))
            return;

        String msg = "";
        ResourceSound sound = event.getResourceSound();
        Player p = event.getPlayer();

        if (sound.getCredit() != null) {
            msg += sound.getCredit();
        }

        if (!msg.isEmpty())
            msg += " - ";

        msg += '\'' + sound.getDisplayName() + '\'';

        if (!msg.isEmpty())
            Msg.tell(p, msg);
    }

}
