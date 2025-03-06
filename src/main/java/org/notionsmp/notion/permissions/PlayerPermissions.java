package org.notionsmp.notion.permissions;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PlayerPermissions {
    private final String uuid;
    private String prefix;
    private Set<String> permissions = new HashSet<>();
    private Set<String> groups = new HashSet<>();

    public PlayerPermissions(String uuid) {
        this.uuid = uuid;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    public void addGroup(String group) {
        groups.add(group);
    }

    public void removeGroup(String group) {
        groups.remove(group);
    }
}