package org.notionsmp.notion.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.notionsmp.notion.Notion;
import org.notionsmp.notion.permissions.Group;
import org.notionsmp.notion.permissions.PermissionManager;
import org.notionsmp.notion.permissions.PlayerPermissions;

@CommandAlias("notion")
@CommandPermission("notion.admin")
public class NotionCommand extends BaseCommand {

    private final PermissionManager permissionManager = Notion.getInstance().getPermissionManager();

    @Subcommand("reload")
    @Description("Reload the plugin's configuration files")
    public void onReload(CommandSender sender) {
        permissionManager.reload();
        send(sender, "<green>Notion configuration reloaded successfully!");
    }

    @Subcommand("group create")
    @CommandCompletion("@nothing")
    @Syntax("<name> <prefix> <weight> - Create a new group")
    public void onGroupCreate(CommandSender sender, String groupName, String prefix, int weight) {
        Group group = new Group(groupName, prefix, weight);
        permissionManager.addGroup(group);
        permissionManager.saveGroups();
        send(sender, "<green>Group " + groupName + " created with prefix " + prefix + " and weight " + weight);
    }

    @Subcommand("group delete")
    @CommandCompletion("@groups")
    @Syntax("<name> - Delete a group")
    public void onGroupDelete(CommandSender sender, String groupName) {
        permissionManager.removeGroup(groupName);
        permissionManager.saveGroups();
        send(sender, "<green>Group " + groupName + " deleted.");
    }

    @Subcommand("group addpermission")
    @CommandCompletion("@groups @nothing")
    @Syntax("<group> <permission> - Add a permission to a group")
    public void onGroupAddPermission(CommandSender sender, String groupName, String permission) {
        Group group = permissionManager.getGroup(groupName);
        if (group != null) {
            group.addPermission(permission);
            permissionManager.saveGroups();
            send(sender, "<green>Permission " + permission + " added to group " + groupName);
        } else {
            send(sender, "<red>Group " + groupName + " not found.");
        }
    }

    @Subcommand("group removepermission")
    @CommandCompletion("@groups @permissions")
    @Syntax("<group> <permission> - Remove a permission from a group")
    public void onGroupRemovePermission(CommandSender sender, String groupName, String permission) {
        Group group = permissionManager.getGroup(groupName);
        if (group != null) {
            group.removePermission(permission);
            permissionManager.saveGroups();
            send(sender, "<green>Permission " + permission + " removed from group " + groupName);
        } else {
            send(sender, "<red>Group " + groupName + " not found.");
        }
    }

    @Subcommand("player addgroup")
    @CommandCompletion("@players @groups")
    @Syntax("<player> <group> - Add a group to a player")
    public void onPlayerAddGroup(CommandSender sender, @Flags("other") Player targetPlayer, String groupName) {
        PlayerPermissions playerPermissions = permissionManager.getPlayerPermissions(targetPlayer.getUniqueId());
        if (playerPermissions != null) {
            if (permissionManager.getGroup(groupName) != null) {
                playerPermissions.addGroup(groupName);
                permissionManager.savePlayers();
                permissionManager.updatePlayerPrefix(targetPlayer);
                send(sender, "<green>Group " + groupName + " added to player " + targetPlayer.getName());
            } else {
                send(sender, "<red>Group " + groupName + " does not exist.");
            }
        } else {
            send(sender, "<red>Player " + targetPlayer.getName() + " not found.");
        }
    }

    @Subcommand("player removegroup")
    @CommandCompletion("@players @groups")
    @Syntax("<player> <group> - Remove a group from a player")
    public void onPlayerRemoveGroup(CommandSender sender, @Flags("other") Player targetPlayer, String groupName) {
        PlayerPermissions playerPermissions = permissionManager.getPlayerPermissions(targetPlayer.getUniqueId());
        if (playerPermissions != null) {
            if (permissionManager.getGroup(groupName) != null) {
                playerPermissions.removeGroup(groupName);
                permissionManager.savePlayers();
                permissionManager.updatePlayerPrefix(targetPlayer);
                send(sender, "<green>Group " + groupName + " removed from player " + targetPlayer.getName());
            } else {
                send(sender, "<red>Group " + groupName + " does not exist.");
            }
        } else {
            send(sender, "<red>Player " + targetPlayer.getName() + " not found.");
        }
    }

    @Subcommand("player addpermission")
    @CommandCompletion("@players @nothing")
    @Syntax("<player> <permission> - Add a permission to a player")
    public void onPlayerAddPermission(CommandSender sender, @Flags("other") Player targetPlayer, String permission) {
        PlayerPermissions playerPermissions = permissionManager.getPlayerPermissions(targetPlayer.getUniqueId());
        if (playerPermissions != null) {
            playerPermissions.addPermission(permission);
            permissionManager.savePlayers();
            send(sender, "<green>Permission " + permission + " added to player " + targetPlayer.getName());
        } else {
            send(sender, "<red>Player " + targetPlayer.getName() + " not found.");
        }
    }

    @Subcommand("player removepermission")
    @CommandCompletion("@players @permissions")
    @Syntax("<player> <permission> - Remove a permission from a player")
    public void onPlayerRemovePermission(CommandSender sender, @Flags("other") Player targetPlayer, String permission) {
        PlayerPermissions playerPermissions = permissionManager.getPlayerPermissions(targetPlayer.getUniqueId());
        if (playerPermissions != null) {
            playerPermissions.removePermission(permission);
            permissionManager.savePlayers();
            send(sender, "<green>Permission " + permission + " removed from player " + targetPlayer.getName());
        } else {
            send(sender, "<red>Player " + targetPlayer.getName() + " not found.");
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        send(sender, "<#FFB022>┌────────────┤ NAME<#FFB022>├────────────┐".replace("NAME", Notion.NAME));
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion help'><#454545>/notion <#FFB022>help <white>- shows this menu</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion reload'><#454545>/notion <#FFB022>reload <white>- reload the plugin's configuration</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion group create'><#454545>/notion <#FFB022>group create <white>- create a new group</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion group delete'><#454545>/notion <#FFB022>group delete <white>- delete a group</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion group addpermission'><#454545>/notion <#FFB022>group addpermission <white>- add a permission to a group</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion group removepermission'><#454545>/notion <#FFB022>group removepermission <white>- remove a permission from a group</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion player addgroup'><#454545>/notion <#FFB022>player addgroup <white>- add a group to a player</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion player removegroup'><#454545>/notion <#FFB022>player removegroup <white>- remove a group from a player</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion player addpermission'><#454545>/notion <#FFB022>player addpermission <white>- add a permission to a player</click></hover>");
        send(sender, "<hover:show_text:'Click to insert...'><click:suggest_command:'/notion player removepermission'><#454545>/notion <#FFB022>player removepermission <white>- remove a permission from a player</click></hover>");
        send(sender, "<#FFB022>└──────────────────────────────┘");
    }

    private void send(CommandSender sender, String message) {
        sender.sendMessage(Notion.MINI_MESSAGE.deserialize(message));
    }
}