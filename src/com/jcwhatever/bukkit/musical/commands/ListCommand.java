/* This file is part of MusicalRegions for Bukkit, licensed under the MIT License (MIT).
 *
 * Copyright (c) JCThePants (www.jcwhatever.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.jcwhatever.bukkit.musical.commands;

import com.jcwhatever.bukkit.generic.commands.AbstractCommand;
import com.jcwhatever.bukkit.generic.commands.ICommandInfo;
import com.jcwhatever.bukkit.generic.commands.arguments.CommandArguments;
import com.jcwhatever.bukkit.generic.commands.exceptions.InvalidValueException;
import com.jcwhatever.bukkit.generic.messaging.ChatPaginator;
import com.jcwhatever.bukkit.generic.messaging.ChatPaginator.PaginatorTemplate;
import com.jcwhatever.bukkit.generic.utils.TextUtils;
import com.jcwhatever.bukkit.generic.utils.TextUtils.FormatTemplate;
import com.jcwhatever.bukkit.musical.Lang;
import com.jcwhatever.bukkit.musical.MusicalRegions;
import com.jcwhatever.bukkit.musical.regions.MusicRegion;
import com.jcwhatever.bukkit.musical.regions.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@ICommandInfo(
		command="list", 
		staticParams={"page=1"},
		usage="/musical list [page]",
		description="List music regions.")

public class ListCommand extends AbstractCommand {

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
		
		int page = args.getInt("page");
		
		String paginTitle = Lang.get("Available Resource Sounds");
		ChatPaginator pagin = new ChatPaginator(getPlugin(), 6, PaginatorTemplate.HEADER, PaginatorTemplate.FOOTER, paginTitle);

		RegionManager regionManager = MusicalRegions.getInstance().getRegionManager();

		List<MusicRegion> regions = regionManager.getRegions();

		String noneLabel = Lang.get("<none>");

		for (MusicRegion region : regions) {

            String description = (region.getPlayList().size() == 0
                    ? ChatColor.RED + noneLabel
                    : TextUtils.concat(region.getPlayList().getSounds(), ", "));

			pagin.add(region.getName(), description);
		}
		
		pagin.show(sender, page, FormatTemplate.ITEM_DESCRIPTION);
	}
	
}
