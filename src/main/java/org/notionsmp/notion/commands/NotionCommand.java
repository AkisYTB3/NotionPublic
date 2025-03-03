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
        send(sender, "<#FFB022>┌────────────┤ NAME<#FFB022>├────────────┐".replace("NAME", NAME));
        send(sender, "<#FFB022>├ <hover:show_text:'Click to insert...'><click:suggest_command:'/notion help'><#454545>/notion <#FFB022>help <white>- shows this menu</click></hover>");
    }


    private void send(CommandSender sender, String message) {
        sender.sendMessage(MINI_MESSAGE.deserialize(message));
    }
}
