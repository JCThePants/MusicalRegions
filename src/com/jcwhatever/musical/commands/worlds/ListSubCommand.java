package com.jcwhatever.musical.commands.worlds;

import java.util.Collection;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.musical.worlds.MusicWorld;
import com.jcwhatever.musical.worlds.WorldManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.text.TextUtils;

import org.bukkit.command.CommandSender;

@CommandInfo(
        command="list",
        staticParams={"page=1"},
        description="List world playlist bindings.",
        paramDescriptions = {
                "page= {PAGE}"
        })

public class ListSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Musical Worlds";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws InvalidArgumentException {

        int page = args.getInteger("page");

        ChatPaginator pagin = new ChatPaginator(getPlugin(), 6, Lang.get(_PAGINATOR_TITLE));

        WorldManager manager = MusicalRegions.getWorldManager();

        Collection<MusicWorld> worlds = manager.getAll();

        for (MusicWorld world : worlds) {
            pagin.add(world.getName(), world.getPlaylistName());
        }

        pagin.show(sender, page, TextUtils.FormatTemplate.LIST_ITEM_DESCRIPTION);
    }
}
