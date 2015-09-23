package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.text.TextUtils;
import com.jcwhatever.nucleus.utils.text.TextUtils.FormatTemplate;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="info",
        staticParams={"regionName", "page=1"},
        description="Get information about the settings of a musical region.",
        paramDescriptions = {
                "regionName= The name of the region to get info about.",
                "page= {PAGE}"
        })

public class InfoSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Region Info for '{0: region name}'";

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _LABEL_WORLD = "World";
    @Localizable static final String _LABEL_P1_COORDS = "P1 Coords";
    @Localizable static final String _LABEL_P2_COORDS = "P2 Coords";
    @Localizable static final String _LABEL_AUDIO_VOLUME = "Audio Volume";
    @Localizable static final String _LABEL_AUDIO_VOLUME_FACTOR = "Audio Volume Factor";
    @Localizable static final String _LABEL_TRACK_CHANGE_DELAY = "Track Change Delay";
    @Localizable static final String _LABEL_RESOURCE_SOUNDS = "Resource Sounds";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String regionName = args.getString("regionName");
        int page = args.getInteger("page");

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region == null)
            throw new CommandException(Lang.get(_REGION_NOT_FOUND, regionName));

        ChatPaginator pagin = createPagin(args, 7, Lang.get(_PAGINATOR_TITLE, region.getName()));
        pagin.add(Lang.get(_LABEL_WORLD), region.getWorldName());
        pagin.add(Lang.get(_LABEL_P1_COORDS), TextUtils.formatLocation(region.getP1(), true));
        pagin.add(Lang.get(_LABEL_P2_COORDS), TextUtils.formatLocation(region.getP2(), true));
        pagin.add(Lang.get(_LABEL_AUDIO_VOLUME), region.getSoundVolume());
        pagin.add(Lang.get(_LABEL_AUDIO_VOLUME_FACTOR), region.getSoundVolumeFactor());
        pagin.add(Lang.get(_LABEL_TRACK_CHANGE_DELAY), region.getTrackChangeDelay());
        pagin.add(Lang.get(_LABEL_RESOURCE_SOUNDS), TextUtils.concat(region.getSounds(), ", "));

        pagin.show(sender, page, FormatTemplate.LIST_ITEM_DESCRIPTION);
    }
}
