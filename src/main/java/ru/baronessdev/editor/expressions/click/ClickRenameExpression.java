package ru.baronessdev.editor.expressions.click;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.expressions.ExampleExpression;

public class ClickRenameExpression extends ExampleExpression {
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();

        if (e.getMessage().equals("cancel")) return;

        ItemStack i = Executor.getData().getData().get(p).getInventory().getItem((int) obj);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(e.getMessage().replace('&', ChatColor.COLOR_CHAR));
        i.setItemMeta(meta);

        p.sendMessage(getMessage("click_name_done"));
    }
}
