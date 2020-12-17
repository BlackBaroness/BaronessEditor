package ru.baronessdev.editor.expressions.rename;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.data.Project;
import ru.baronessdev.editor.expressions.ExampleExpression;

public class MenuRenameExpression extends ExampleExpression {
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();
        Project project = Executor.getData().getData().get(p);
        Inventory oldInventory = project.getInventory();

        String title = e.getMessage().replace('&', ChatColor.COLOR_CHAR);

        Inventory inv = Bukkit.createInventory(null, oldInventory.getSize(), title);
        for (int i = 0; i < oldInventory.getSize(); i++) {
            ItemStack item = oldInventory.getItem(i);
            if (item != null) inv.setItem(i, item);
        }

        project.setInventory(inv);
        project.setTitle(title);
        p.sendMessage(getMessage("rename_changed_menu"));
    }
}
