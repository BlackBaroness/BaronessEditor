package ru.baronessdev.free.editor.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class SmartMessagesBuilder {

    private final TextComponent message;

    public SmartMessagesBuilder(String message) {
        this.message = new TextComponent(message);
    }

    public SmartMessagesBuilder setHoverEvent(HoverEvent e) {
        message.setHoverEvent(e);
        return this;
    }

    public SmartMessagesBuilder setClickEvent(ClickEvent e) {
        message.setClickEvent(e);
        return this;
    }

    public void send(Player p) {
        p.spigot().sendMessage(message);
    }
}
