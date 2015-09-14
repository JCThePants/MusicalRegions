package com.jcwhatever.musical.commands.worlds;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.musical.playlists.PlayListManager;
import com.jcwhatever.musical.regions.MusicRegion;
import com.jcwhatever.musical.regions.RegionManager;
import com.jcwhatever.musical.worlds.MusicWorld;
import com.jcwhatever.musical.worlds.WorldManager;
import com.jcwhatever.nucleus.managed.commands.CommandInfo;
import com.jcwhatever.nucleus.managed.commands.arguments.ICommandArguments;
import com.jcwhatever.nucleus.managed.commands.exceptions.CommandException;
import com.jcwhatever.nucleus.managed.commands.mixins.IExecutableCommand;
import com.jcwhatever.nucleus.managed.commands.utils.AbstractCommand;
import com.jcwhatever.nucleus.managed.language.Localizable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        command="playlist",
        staticParams={ "worldName", "listName"},
        description="Set a worlds playlist binding.",
        paramDescriptions = {
                "worldName= The name of the world to bind the playlist to.",
                "listName= The name of the playlist."
        })

public class PlayListSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _WORLD_NOT_FOUND =
            "A world named '{0: world name}' was not found.";

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "A playlist with the name '{0: playlist name}' was not found.";

    @Localizable static final String _SUCCESS =
            "Playlist binding for world '{0: world name}' changed to '{1: play list name}'.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String worldName = args.getString("worldName");
        String listName = args.getString("listName");

        World world = Bukkit.getWorld(worldName);
        if (world == null)
            throw new CommandException(Lang.get(_WORLD_NOT_FOUND, worldName));

        PlayListManager listManager = MusicalRegions.getPlayListManager();
        MusicPlayList playList = listManager.get(listName);
        if (playList == null)
            throw new CommandException(Lang.get(_PLAYLIST_NOT_FOUND, listName));

        WorldManager manager = MusicalRegions.getWorldManager();
        MusicWorld musicWorld = manager.get(worldName);
        if (musicWorld == null) {
            manager.create(world, playList);
        }
        else {
            musicWorld.setPlaylistName(playList.getName());
        }

        tellSuccess(sender, Lang.get(_SUCCESS, worldName, playList.getName()));
    }
}

