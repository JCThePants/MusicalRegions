package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;
import com.jcwhatever.nucleus.managed.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.text.TextUtils;
import com.jcwhatever.nucleus.utils.text.TextUtils.FormatTemplate;

import org.bukkit.command.CommandSender;

import java.util.Collection;

@CommandInfo(
        command="list",
        staticParams={"page=1"},
        description="List playlists.",
        paramDescriptions = {
                "page= {PAGE}"
        })

public class ListSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Playlists";

    @Localizable static final String _LABEL_NONE =
            "{RED}<no sounds>";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws InvalidArgumentException {

        int page = args.getInteger("page");

        ChatPaginator pagin = new ChatPaginator(getPlugin(), 6, Lang.get(_PAGINATOR_TITLE));

        PlayListManager manager = MusicalRegions.getPlayListManager();

        Collection<RegionPlayList> playLists = manager.getAll();

        String noneLabel = Lang.get(_LABEL_NONE);

        for (RegionPlayList playList : playLists) {

            String description = (playList.getSounds().isEmpty()
                    ? noneLabel
                    : TextUtils.concat(playList.getSounds(), ", "));

            pagin.add(playList.getName(), description);
        }

        pagin.show(sender, page, FormatTemplate.LIST_ITEM_DESCRIPTION);
    }
}

