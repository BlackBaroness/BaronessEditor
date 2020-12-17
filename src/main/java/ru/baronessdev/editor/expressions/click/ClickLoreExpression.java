package ru.baronessdev.editor.expressions.click;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.expressions.ExampleExpression;
import ru.baronessdev.editor.listeners.ChatListener;

import java.util.List;

public class ClickLoreExpression extends ExampleExpression {
    @SuppressWarnings("unchecked")
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Player p = e.getPlayer();

        if (e.getMessage().equals("cancel")) return;

        List<String> lore = (List<String>) obj;

        if (e.getMessage().equals("done")) {
            ItemStack i = Executor.getData().getData().get(p).getInventory().getItem(Integer.parseInt(lore.get(0)));
            ItemMeta meta = i.getItemMeta();

            lore.remove(0);
            for (int j = 0; j < lore.size(); j++) {
                lore.set(j, lore.get(j).replace('&', ChatColor.COLOR_CHAR));
            }

            meta.setLore(lore);
            i.setItemMeta(meta);

            p.sendMessage(getMessage("click_name_lore"));
            return;
        }

        lore.add(e.getMessage());
        new ChatListener(p, new ClickLoreExpression(), lore);
        p.sendMessage(String.format(getMessage("click_lore"), (lore.size() - 1) + ""));
    }
}
