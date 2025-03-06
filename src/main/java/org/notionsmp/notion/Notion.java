package org.notionsmp.notion;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.notionsmp.notion.commands.GroupCompletion;
import org.notionsmp.notion.commands.NotionCommand;
import org.notionsmp.notion.listeners.ChatListener;
import org.notionsmp.notion.listeners.PlayerJoinListener;
import org.notionsmp.notion.permissions.PermissionManager;

import java.io.File;

@Getter
public final class Notion extends JavaPlugin {

    @Getter
    private static Notion instance;
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final PlainTextComponentSerializer PLAIN_TEXT_SERIALIZER = PlainTextComponentSerializer.plainText();
    public static final String NAME = "<gradient:#663399:#7069ff>ɴᴏᴛɪᴏɴ</gradient>";
    private static final String PREFIX = NAME + " <gray>| <white>";
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        instance = this;

        log("Starting plugin...");
        loadConfigs();
        initializePermissionManager();
        registerCommands();
        registerListeners();
        initializeCommandManager();
        log("Plugin enabled!");
    }

    private void loadConfigs() {
        log("Loading configurations...");
        loadConfig("permissions/permissions.yml");
        log("Configurations loaded!");
    }

    private void loadConfig(String name) {
        File configFile = new File(getDataFolder(), name);
        if (!configFile.exists()) {
            saveResource(name, false);
            log("<yellow>" + name + " not found, creating it.");
        } else {
            log("<green>" + name + " loaded successfully.");
        }
    }

    private void initializePermissionManager() {
        File permissionsConfig = new File(getDataFolder(), "permissions/permissions.yml");
        permissionManager = new PermissionManager(permissionsConfig);
        log("PermissionManager initialized!");
    }

    private void initializeCommandManager() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("groups", new GroupCompletion());
        manager.registerCommand(new NotionCommand());
    }

    private void registerListeners() {
        log("Loading listeners...");
        registerListener(new PlayerJoinListener());
        registerListener(new ChatListener());
        log("Listeners loaded!");
    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerCommands() {
        log("Loading commands...");

        log("Commands loaded!");
    }

    private void log(String message) {
        Component component = MINI_MESSAGE.deserialize(PREFIX + message);
        String plainText = PLAIN_TEXT_SERIALIZER.serialize(component);
        getLogger().info(plainText);
    }
}