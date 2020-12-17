package ru.baronessdev.editor.expressions.open;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.baronessdev.editor.Core;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.data.Project;
import ru.baronessdev.editor.expressions.ExampleExpression;

import java.io.File;

public class OpenExpression extends ExampleExpression {
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();

        File file = new File(Core.getCore().getDataFolder() + File.separator + "projects" + File.separator + e.getMessage() + ".yml");
        if (!file.exists()) {
            p.sendMessage(getMessage("open_not_found"));
            return;
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Project project = new Project();
        project.setName(e.getMessage());

        int size = cfg.getInt("size");
        Inventory inv = Bukkit.createInventory(null, size, cfg.getString("title"));

        for (int i = 0; i < size; i++) {
            ItemStack item = cfg.getItemStack(String.valueOf(i));
            if (item != null) inv.setItem(i, item);
        }

        project.setInventory(inv);
        Executor.getData().getData().put(p, project);
        p.sendMessage(getMessage("open_opened"));
    }
}
