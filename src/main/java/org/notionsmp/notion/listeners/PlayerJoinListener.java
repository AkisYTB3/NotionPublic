package org.notionsmp.notion.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.notionsmp.notion.Notion;
import org.notionsmp.notion.permissions.PermissionManager;
import org.notionsmp.notion.permissions.PlayerPermissions;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final PermissionManager permissionManager = Notion.getInstance().getPermissionManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (permissionManager.getPlayerPermissions(uuid) == null) {
            PlayerPermissions playerPermissions = new PlayerPermissions(uuid.toString());
            permissionManager.addPlayerPermissions(playerPermissions);
        }

        permissionManager.updatePlayerPermissions(event.getPlayer());
        permissionManager.updatePlayerPrefix(event.getPlayer());
    }
}