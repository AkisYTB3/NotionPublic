package org.notionsmp.notion.permissions;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Group {
    private final String name;
    private String prefix;
    private int weight;
    private Set<String> permissions = new HashSet<>();

    public Group(String name, String prefix, int weight) {
        this.name = name;
        this.prefix = prefix;
        this.weight = weight;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }
}