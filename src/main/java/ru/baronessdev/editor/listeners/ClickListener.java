package ru.baronessdev.editor.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.data.Project;
import ru.baronessdev.editor.expressions.click.ClickLoreExpression;
import ru.baronessdev.editor.expressions.click.ClickRenameExpression;

import java.util.ArrayList;
import java.util.List;

import static ru.baronessdev.editor.Core.*;

public class ClickListener implements Listener {

    public ClickListener() {
        MainListener.activate(this);
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        ClickType clickType = e.getClick();
        if (!clickType.isRightClick() && !clickType.isShiftClick()) return;
        if (e.getClickedInventory().getItem(e.getSlot()) == null) return;

        Player p = (Player) e.getWhoClicked();
        Project project = Executor.getData().getData().get(p);
        if (project == null) return;

        if (!e.getClickedInventory().equals(project.getInventory())) return;

        if (clickType.isRightClick()) {
            p.closeInventory();
            p.getOpenInventory().close();
            e.setCancelled(true);
            p.sendMessage(getMessage("click_name"));
            new ChatListener(p, new ClickRenameExpression(), e.getSlot());
            return;

        }

        e.setCancelled(true);
        p.closeInventory();
        p.getOpenInventory().close();

        List<String> l = new ArrayList<>();
        l.add(e.getSlot() + "");
        new ChatListener(p, new ClickLoreExpression(), l);
        p.sendMessage(String.format(getMessage("click_lore"), 0));
        p.updateInventory();
    }
}
