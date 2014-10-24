package com.jcwhatever.bukkit.musical;


import com.jcwhatever.bukkit.generic.events.bukkit.sounds.PlayResourceSoundEvent;
import com.jcwhatever.bukkit.generic.messaging.Messenger;
import com.jcwhatever.bukkit.generic.sounds.MusicSound;
import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventListener implements Listener {

    RegionManager _regionManager;

    public EventListener () {
        _regionManager = MusicalRegions.getInstance().getRegionManager();
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
            Messenger.tell(MusicalRegions.getInstance(), p, msg);
    }

}
