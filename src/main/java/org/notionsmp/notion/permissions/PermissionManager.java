package org.notionsmp.notion.permissions;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.notionsmp.notion.Notion;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class PermissionManager {
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<UUID, PlayerPermissions> players = new HashMap<>();
    private final Map<UUID, PermissionAttachment> permissionAttachments = new HashMap<>();
    private final File configFile;
    private YamlConfiguration config;

    public PermissionManager(File configFile) {
        this.configFile = configFile;
        this.config = YamlConfiguration.loadConfiguration(configFile);
        loadGroups();
        loadPlayers();
    }

    public void reload() {
        groups.clear();
        players.clear();

        config = YamlConfiguration.loadConfiguration(configFile);

        loadGroups();
        loadPlayers();

        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerPermissions(player);
            updatePlayerPrefix(player);
        }
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
        saveGroups();
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
        saveGroups();
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public void addPlayerPermissions(PlayerPermissions playerPermissions) {
        players.put(UUID.fromString(playerPermissions.getUuid()), playerPermissions);
        savePlayers();
    }

    public PlayerPermissions getPlayerPermissions(UUID uuid) {
        return players.get(uuid);
    }

    public void loadGroups() {
        if (config.contains("groups")) {
            for (String groupName : config.getConfigurationSection("groups").getKeys(false)) {
                String prefix = config.getString("groups." + groupName + ".prefix");
                int weight = config.getInt("groups." + groupName + ".weight");
                Group group = new Group(groupName, prefix, weight);
                group.setPermissions(new HashSet<>(config.getStringList("groups." + groupName + ".permissions")));
                groups.put(groupName, group);
            }
        }
    }

    public void loadPlayers() {
        if (config.contains("players")) {
            for (String uuidString : config.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                PlayerPermissions playerPermissions = new PlayerPermissions(uuidString);
                playerPermissions.setPrefix(config.getString("players." + uuidString + ".prefix"));
                playerPermissions.setPermissions(new HashSet<>(config.getStringList("players." + uuidString + ".permissions")));
                playerPermissions.setGroups(new HashSet<>(config.getStringList("players." + uuidString + ".groups")));
                players.put(uuid, playerPermissions);
            }
        }
    }

    public void saveGroups() {
        for (Map.Entry<String, Group> entry : groups.entrySet()) {
            String groupName = entry.getKey();
            Group group = entry.getValue();
            config.set("groups." + groupName + ".prefix", group.getPrefix());
            config.set("groups." + groupName + ".weight", group.getWeight());
            config.set("groups." + groupName + ".permissions", new ArrayList<>(group.getPermissions()));
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayers() {
        for (Map.Entry<UUID, PlayerPermissions> entry : players.entrySet()) {
            String uuidString = entry.getKey().toString();
            PlayerPermissions playerPermissions = entry.getValue();
            config.set("players." + uuidString + ".prefix", playerPermissions.getPrefix());
            config.set("players." + uuidString + ".permissions", new ArrayList<>(playerPermissions.getPermissions()));
            config.set("players." + uuidString + ".groups", new ArrayList<>(playerPermissions.getGroups()));
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerPrefix(Player player) {
        PlayerPermissions playerPermissions = players.get(player.getUniqueId());
        if (playerPermissions == null) return "";

        String highestPrefix = playerPermissions.getPrefix() != null ? playerPermissions.getPrefix() : "";
        int highestWeight = -1;

        for (String groupName : playerPermissions.getGroups()) {
            Group group = groups.get(groupName);
            if (group != null && group.getWeight() > highestWeight) {
                highestWeight = group.getWeight();
                highestPrefix = group.getPrefix() != null ? group.getPrefix() : "";
            }
        }

        return highestPrefix;
    }

    public void updatePlayerPrefix(Player player) {
        String prefix = getPlayerPrefix(player);
        player.displayName(MiniMessage.miniMessage().deserialize(prefix + player.getName()));
    }

    public void updatePlayerPermissions(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerPermissions playerPermissions = players.get(uuid);
        if (playerPermissions == null) return;

        PermissionAttachment attachment = permissionAttachments.get(uuid);
        if (attachment != null) {
            attachment.getPermissions().keySet().forEach(attachment::unsetPermission);
        } else {
            attachment = player.addAttachment(Notion.getInstance());
            permissionAttachments.put(uuid, attachment);
        }

        for (String permission : playerPermissions.getPermissions()) {
            attachment.setPermission(permission, true);
        }

        for (String groupName : playerPermissions.getGroups()) {
            Group group = groups.get(groupName);
            if (group != null) {
                for (String permission : group.getPermissions()) {
                    attachment.setPermission(permission, true);
                }
            }
        }

        player.recalculatePermissions();
    }
}