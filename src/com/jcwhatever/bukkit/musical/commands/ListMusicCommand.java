/* This file is part of BankOfArboria for Bukkit, licensed under the MIT License (MIT).
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
import com.jcwhatever.bukkit.generic.language.Localizable;
import com.jcwhatever.bukkit.generic.sounds.MusicSound;
import com.jcwhatever.bukkit.generic.sounds.ResourceSound;
import com.jcwhatever.bukkit.generic.sounds.SoundManager;
import com.jcwhatever.bukkit.generic.messaging.ChatPaginator;
import com.jcwhatever.bukkit.generic.messaging.ChatPaginator.PaginatorTemplate;
import com.jcwhatever.bukkit.generic.utils.TextUtils.FormatTemplate;
import com.jcwhatever.bukkit.musical.Lang;
import org.bukkit.command.CommandSender;

import java.util.Set;

@ICommandInfo(
		command="listmusic", 
		staticParams={"page=1"},
		usage="/musical listmusic [page]",
		description="List available resource sounds.")

public class ListMusicCommand extends AbstractCommand {

    @Localizable static final String _PAGINATOR_TITLE = "Available Resource Sounds";
    @Localizable static final String _LABEL_SECONDS = "seconds";

	@Override
	public void execute(CommandSender sender, CommandArguments args) throws InvalidValueException {
		
		int page = args.getInt("page");
		
		String paginTitle = Lang.get(_PAGINATOR_TITLE);
		ChatPaginator pagin = new ChatPaginator(getPlugin(), 6, PaginatorTemplate.HEADER, PaginatorTemplate.FOOTER, paginTitle);
		
		Set<ResourceSound> sounds = SoundManager.getSounds(MusicSound.class);
		
		String secondsLabel = Lang.get(_LABEL_SECONDS);
		for (ResourceSound sound : sounds) {

            String description = sound.getDisplayName() != null
                    ? sound.getDisplayName() + " - "
                    : "";

			pagin.add(sound.getName(),
                    description + sound.getDurationSeconds() + ' ' + secondsLabel);
		}
		
		pagin.show(sender, page, FormatTemplate.ITEM_DESCRIPTION);
	}
	
}