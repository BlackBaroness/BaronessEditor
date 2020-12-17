package ru.baronessdev.editor.expressions.delete;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.baronessdev.editor.Core;
import ru.baronessdev.editor.expressions.ExampleExpression;
import ru.baronessdev.editor.listeners.ChatListener;

import java.io.File;

public class DeleteExpression extends ExampleExpression {

    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();

        File file = new File(Core.getCore().getDataFolder() + File.separator + "projects" + File.separator + e.getMessage() + ".yml");
        if (!file.exists()) {
            p.sendMessage(getMessage("delete_not_found"));
            return;
        }

        p.sendMessage(String.format(getMessage("delete_confirm"), p.getName()));
        new ChatListener(p, new ConfirmExpression(), new String[]{p.getName(), e.getMessage()});
    }
}
