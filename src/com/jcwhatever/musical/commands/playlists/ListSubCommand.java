package com.jcwhatever.musical.commands.playlists;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.playlists.RegionPlayList;
import com.jcwhatever.nucleus.commands.AbstractCommand;
import com.jcwhatever.nucleus.commands.CommandInfo;
import com.jcwhatever.nucleus.commands.arguments.CommandArguments;
import com.jcwhatever.nucleus.commands.exceptions.InvalidArgumentException;
import com.jcwhatever.nucleus.messaging.ChatPaginator;
import com.jcwhatever.nucleus.utils.language.Localizable;
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

public class ListSubCommand extends AbstractCommand {

    @Localizable static final String _PAGINATOR_TITLE =
            "Playlists";

    @Localizable static final String _LABEL_NONE =
            "{RED}<no sounds>";

    @Override
    public void execute(CommandSender sender, CommandArguments args) throws InvalidArgumentException {

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

