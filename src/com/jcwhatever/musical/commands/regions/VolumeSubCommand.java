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

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="volume",
        staticParams={ "regionName", "factor"},
        description="Change the factor applied to a regions pre-calculated audio volume.",
        paramDescriptions = {
                "regionName= The name of the region.",
                "factor= The factor to apply (multiply) against the regions pre-calculated audio volume."
        })

public class VolumeSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _SUCCESS =
            "Musical region '{0: region name}' sound volume is now factored " +
                    "against {1: factor}. The volume is now {2: math product}.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String regionName = args.getString("regionName");
        float factor = args.getFloat("factor");

        RegionManager regionManager = MusicalRegions.getRegionManager();
        MusicRegion region = regionManager.get(regionName);
        if (region == null)
            throw new CommandException(Lang.get(_REGION_NOT_FOUND, regionName));

        region.setSoundVolumeFactor(factor);

        tellSuccess(sender, Lang.get(_SUCCESS, region.getName(), factor, region.getSoundVolume()));
    }
}

