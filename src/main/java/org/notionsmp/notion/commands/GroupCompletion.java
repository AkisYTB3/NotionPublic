package org.notionsmp.notion.commands;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import org.notionsmp.notion.Notion;
import org.notionsmp.notion.permissions.PermissionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupCompletion implements CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> {

    private final PermissionManager permissionManager = Notion.getInstance().getPermissionManager();

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) {
        List<String> groups = new ArrayList<>(permissionManager.getGroups().keySet());
        return groups;
    }
}