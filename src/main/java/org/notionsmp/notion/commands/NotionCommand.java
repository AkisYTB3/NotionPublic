package org.notionsmp.notion.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;

import static org.notionsmp.notion.Notion.MINI_MESSAGE;
import static org.notionsmp.notion.Notion.NAME;

@CommandAlias("notion")
@CommandPermission("notion.admin")
public class NotionCommand extends BaseCommand {
    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(MINI_MESSAGE.deserialize("""
                <#FFB022>┌────────────┤ NAME <#FFB022>├────────────┐
                <#FFB022>├<#FFFF00>/notion <#FF8800>help <white> - shows this menu
                """.replace("NAME", NAME)));
    }
}
