package ru.baronessdev.editor.expressions.delete;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.baronessdev.editor.Core;
import ru.baronessdev.editor.expressions.ExampleExpression;

import java.io.File;

public class ConfirmExpression extends ExampleExpression {
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();
        String[] massive = (String[]) obj;

        String confirm = massive[0];
        String projectName = massive[1];

        if (!confirm.equals(p.getName())) {
            p.sendMessage(getMessage("delete_cancel"));
            return;
        }

        File file = new File(Core.getCore().getDataFolder() + File.separator + "projects" + File.separator + projectName + ".yml");
        if (file.delete()) {
            p.sendMessage(getMessage("delete_finish"));
        }
    }
}
