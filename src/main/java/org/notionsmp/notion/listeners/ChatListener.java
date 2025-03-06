package org.notionsmp.notion.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.notionsmp.notion.Notion;
import org.notionsmp.notion.permissions.PermissionManager;

public class ChatListener implements Listener {
    private static final String CHAT_PERMISSION = "notion.chat.format";
    private final PermissionManager permissionManager = Notion.getInstance().getPermissionManager();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component message = event.message();

        if (player.hasPermission(CHAT_PERMISSION)) {
            String messageString = PlainTextComponentSerializer.plainText().serialize(message);
            Component formattedMessage = MiniMessage.miniMessage().deserialize(messageString);
            event.message(formattedMessage);
        }
    }
}