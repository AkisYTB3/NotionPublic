package org.notionsmp.plugins;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Notion extends JavaPlugin {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final PlainTextComponentSerializer PLAIN_TEXT_SERIALIZER = PlainTextComponentSerializer.plainText();
    private static final String PLUGIN_PREFIX = "<gradient:#663399:#7069ff>Notion</gradient> <gray>| <white>";

    @Getter
    private static Notion instance;

    @Override
    public void onEnable() {
        instance = this;

        log("Starting plugin...");
        registerCommands();
        registerListeners();
        log("Plugin enabled!");
    }

    private void registerListeners() {
        log("Loading listeners...");

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
        Component component = MINI_MESSAGE.deserialize(PLUGIN_PREFIX + message);
        String plainText = PLAIN_TEXT_SERIALIZER.serialize(component);
        getLogger().info(plainText);
    }
}