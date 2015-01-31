package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.language.Localizable;
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

public class InfoSubCommand extends AbstractCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Region Info for '{0: region name}'";

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _LABEL_WORLD = "World";
    @Localizable static final String _LABEL_P1_COORDS = "P1 Coords";
    @Localizable static final String _LABEL_P2_COORDS = "P1 Coords";
    @Localizable static final String _LABEL_AUDIO_VOLUME = "Audio Volume";
    @Localizable static final String _LABEL_AUDIO_VOLUME_FACTOR = "Audio Volume Factor";
    @Localizable static final String _LABEL_RESOURCE_SOUNDS = "Resource Sounds";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidArgumentException {

        String regionName = args.getString("regionName");
        int page = args.getInteger("page");

        ChatPaginator pagin = new ChatPaginator(getPlugin(), 7, Lang.get(_PAGINATOR_TITLE));

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region == null) {
            tellError(sender, Lang.get(_REGION_NOT_FOUND, regionName));
            return; // finish
        }

        pagin.add(Lang.get(_LABEL_WORLD), region.getWorldName());
        pagin.add(Lang.get(_LABEL_P1_COORDS), TextUtils.formatLocation(region.getP1(), true));
        pagin.add(Lang.get(_LABEL_P2_COORDS), TextUtils.formatLocation(region.getP1(), true));
        pagin.add(Lang.get(_LABEL_AUDIO_VOLUME), region.getSoundVolume());
        pagin.add(Lang.get(_LABEL_AUDIO_VOLUME_FACTOR), region.getSoundVolumeFactor());
        pagin.add(Lang.get(_LABEL_RESOURCE_SOUNDS), TextUtils.concat(region.getSounds(), ", "));

        pagin.show(sender, page, FormatTemplate.LIST_ITEM_DESCRIPTION);
    }
}
