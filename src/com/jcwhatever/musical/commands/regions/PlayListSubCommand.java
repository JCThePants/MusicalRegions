package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="playlist",
        staticParams={ "regionName", "listName"},
        description="Set a regions playlist.",
        paramDescriptions = {
                "regionName= The name of the region to set the playlist on.",
                "listName= The name of the playlist."
        })

public class PlayListSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _SUCCESS =
            "Musical region '{0: region name}' playlist is now '{1: playlist name}'.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String regionName = args.getString("regionName");
        String listName = args.getString("listName");

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region == null)
            throw new CommandException(Lang.get(_REGION_NOT_FOUND, regionName));

        PlayListManager playListManager = MusicalRegions.getPlayListManager();
        RegionPlayList playList = playListManager.get(listName);
        if (playList == null)
            throw new CommandException(Lang.get(_PLAYLIST_NOT_FOUND, listName));

        region.setPlayList(playList);

        tellSuccess(sender, Lang.get(_SUCCESS, region.getName(), playList.getName()));
    }
}
