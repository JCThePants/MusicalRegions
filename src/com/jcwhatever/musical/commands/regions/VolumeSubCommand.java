package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="volume",
        staticParams={ "regionName", "factor"},
        description="Change the factor applied to a regions pre-calculated audio volume.",
        paramDescriptions = {
                "regionName= The name of the region.",
                "factor= The factor to apply (multiply) against the regions pre-calculated audio volume."
        })

public class VolumeSubCommand extends AbstractCommand {

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _SUCCESS =
            "Musical region '{0: region name}' sound volume is now factored " +
                    "against {1: factor}. The volume is now {2: math product}.";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidArgumentException {

        String regionName = args.getString("regionName");
        float factor = args.getFloat("factor");

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region == null) {
            tellError(sender, Lang.get(_REGION_NOT_FOUND, regionName));
            return; // finish
        }

        region.setSoundVolumeFactor(factor);

        tellSuccess(sender, Lang.get(_SUCCESS, region.getName(), factor, region.getSoundVolume()));
    }
}

