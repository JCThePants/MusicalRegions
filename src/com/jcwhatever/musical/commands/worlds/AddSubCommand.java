package com.jcwhatever.musical.commands.worlds;

import com.jcwhatever.musical.Lang;
import com.jcwhatever.musical.MusicalRegions;
import com.jcwhatever.musical.playlists.MusicPlayList;
import com.jcwhatever.musical.playlists.PlayListManager;
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
        command="add",
        staticParams={"worldName", "playListName"},
        description="Play a playlist in a world.",
        paramDescriptions = {
                "worldName= The name of the world.",
                "playListName= The name of the playlist to use."
        })

public class AddSubCommand extends AbstractCommand implements IExecutableCommand {

    @Localizable static final String _WORLD_ALREADY_EXISTS =
            "World '{0: world name}' already has a playlist.";

    @Localizable static final String _WORLD_NOT_FOUND =
            "A world named '{0: world name}' was not found.";

    @Localizable static final String _PLAYLIST_NOT_FOUND =
            "Failed to find a playlist named '{0: playlist name}'.";

    @Localizable static final String _FAILED =
            "Failed to add playlist to world.";

    @Localizable static final String _SUCCESS =
            "Playlist {0: play list name} added to world '{1: world name}'.";

    @Override
    public void execute(CommandSender sender, ICommandArguments args) throws CommandException {

        String worldName = args.getString("worldName");
        String playListName = args.getString("playListName");

        World world = Bukkit.getWorld(worldName);
        if (world == null)
            throw new CommandException(Lang.get(_WORLD_NOT_FOUND, worldName));

        WorldManager manager = MusicalRegions.getWorldManager();
        MusicWorld musicWorld = manager.get(worldName);
        if (musicWorld != null)
            throw new CommandException(Lang.get(_WORLD_ALREADY_EXISTS, worldName));

        PlayListManager listManager = MusicalRegions.getPlayListManager();
        MusicPlayList playList = listManager.get(playListName);
        if (playList == null)
            throw new CommandException(Lang.get(_PLAYLIST_NOT_FOUND, playListName));

        musicWorld = manager.create(world, playList);
        if (musicWorld == null)
            throw new CommandException(Lang.get(_FAILED));

        tellSuccess(sender, Lang.get(_SUCCESS, playList.getName(), worldName));
    }
}

