package ru.baronessdev.editor.expressions.create;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.data.Project;
import ru.baronessdev.editor.expressions.ExampleExpression;

public class SetNameExpression extends ExampleExpression {

    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        int size = (int) obj;
        Player p = e.getPlayer();
        String title = e.getMessage().replace('&', ChatColor.COLOR_CHAR);

        Project project = new Project();
        project.setName(title);
        project.setTitle(title);
        project.setInventory(Bukkit.createInventory(null, size, title));
        Executor.getData().getData().put(p, project);

        p.sendMessage(getMessage("create_created"));
    }
}
