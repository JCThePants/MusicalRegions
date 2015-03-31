package com.jcwhatever.musical.playlists;

import com.jcwhatever.musical.Msg;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.events.MusicLoopEvent;
import com.jcwhatever.musical.events.MusicTrackChangeEvent;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.nucleus.Nucleus;
import com.jcwhatever.nucleus.mixins.INamedInsensitive;
import com.jcwhatever.nucleus.sounds.playlist.SimplePlayList;
import com.jcwhatever.nucleus.sounds.types.ResourceSound;
import com.jcwhatever.nucleus.storage.IDataNode;
import com.jcwhatever.nucleus.utils.CollectionUtils;
import com.jcwhatever.nucleus.utils.PreCon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

/**
 * An extended implementation of {@link SimplePlayList} that saves to a data node and
 * implements {@link INamedInsensitive}.
 */
public class RegionPlayList extends SimplePlayList implements INamedInsensitive {

    private final String _name;
    private final String _searchName;
    private final IDataNode _dataNode;

    public RegionPlayList(String name, IDataNode dataNode) {
        super(MusicalRegions.getPlugin());

        PreCon.notNullOrEmpty(name);
        PreCon.notNull(dataNode);

        _name = name;
        _searchName = name.toLowerCase();
        _dataNode = dataNode;

        load();
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String getSearchName() {
        return _searchName;
    }

    @Override
    public void addSound(ResourceSound sound) {
        super.addSound(sound);
        save();
    }

    @Override
    public void removeSound(ResourceSound sound) {
        super.removeSound(sound);
        save();
    }

    @Override
    public void addSounds(Collection<? extends ResourceSound> sounds) {
        super.addSounds(sounds);
        save();
    }

    @Override
    public void clearSounds() {
        super.clearSounds();
        save();
    }

    @Override
    public void setLoop(boolean isLoop) {
        super.setLoop(isLoop);

        _dataNode.set("loop", isLoop);
        _dataNode.save();
    }

    @Override
    public void setRandom(boolean isRandom) {
        super.setRandom(isRandom);

        _dataNode.set("random", isRandom);
        _dataNode.save();
    }

    @Override
    @Nullable
    protected ResourceSound onTrackChange(PlayerSoundQueue queue,
                                          @Nullable ResourceSound prev, ResourceSound next) {

        next = super.onTrackChange(queue, prev, next);
        if (next == null)
            return null;

        MusicRegion region = queue.getMeta().get(MusicRegion.META_KEY);
        if (region == null)
            return next;

        MusicTrackChangeEvent event = new MusicTrackChangeEvent(region, this, queue, prev, next);
        Nucleus.getEventManager().callBukkit(this, event);

        if (event.isCancelled())
            return null;

        return event.getNextSound();
    }

    @Override
    protected void onLoop(PlayerSoundQueue queue, List<ResourceSound> sounds, int loopCount) {

        super.onLoop(queue, sounds, loopCount);

        MusicRegion region = queue.getMeta().get(MusicRegion.META_KEY);
        if (region == null)
            return;

        MusicLoopEvent event = new MusicLoopEvent(region, this, queue, sounds, loopCount);
        event.setCancelled(sounds.isEmpty());
        Nucleus.getEventManager().callBukkit(this, event);

        if (event.isCancelled())
            sounds.clear();
    }

    private void save() {

        List<ResourceSound> sounds = getSounds();
        List<String> soundNames = new ArrayList<>(sounds.size());
        for (ResourceSound sound : sounds) {
            soundNames.add(sound.getName());
        }

        _dataNode.set("sounds", soundNames);
        _dataNode.save();
    }

    private void load() {

        super.setLoop(_dataNode.getBoolean("loop"));
        super.setRandom(_dataNode.getBoolean("random"));

        List<String> soundNames = _dataNode.getStringList(
                "sounds", CollectionUtils.unmodifiableList(String.class));
        assert soundNames != null;

        List<ResourceSound> sounds = new ArrayList<>(soundNames.size());

        for (String soundName : soundNames) {
            ResourceSound sound = Nucleus.getSoundManager().getSound(soundName);
            if (sound == null) {
                Msg.debug("Failed to load sound named '{0}'. Not found.", soundName);
                continue;
            }

            sounds.add(sound);
        }

        super.addSounds(sounds);
    }
}
