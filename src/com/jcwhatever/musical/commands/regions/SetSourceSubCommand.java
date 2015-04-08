package com.jcwhatever.musical.commands.regions;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.arguments.ILocationHandler;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        command="setsource",
        staticParams={"regionName", "location"},
        description="Change the location a musical regions sound is played from.",
        paramDescriptions = {
                "regionName= The name of the region.",
                "location= Type 'center' to place the location at the regions center. " +
                        "Otherwise specify specific location: {LOCATION}"
        })

public class SetSourceSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _REGION_NOT_FOUND =
            "A musical region with the name '{0: region name}' was not found.";

    @Localizable static final String _INVALID_WORLD =
            "The specified location must be in the same world as the region.";

    @Localizable static final String _SUCCESS =
            "Musical region '{0: region name}' sound source location updated to '{1: location}'.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String regionName = args.getName("regionName");

        RegionManager regionManager = MusicalRegions.getRegionManager();
        final MusicRegion region = regionManager.get(regionName);
        if (region == null) {
            tellError(sender, Lang.get(_REGION_NOT_FOUND, regionName));
            return; // finish
        }

        if (args.getString("location").equalsIgnoreCase("center")) {
            setSource(sender, region, region.getCenter());
        }
        else {

            args.getLocation(sender, "location", new ILocationHandler() {
                @Override
                public void onLocationRetrieved(Player player, Location result) {
                    setSource(player, region, result);
                }
            });
        }
    }

    private void setSource(CommandSender sender, MusicRegion region, Location location) {

        if (location.getWorld() == null || !location.getWorld().equals(region.getWorld())) {
            tellError(sender, Lang.get(_INVALID_WORLD));
            return;
        }

        region.setSoundSource(location);

        tellSuccess(sender, Lang.get(_SUCCESS, region.getName(),
                TextUtils.formatLocation(location, true)));
    }
}
