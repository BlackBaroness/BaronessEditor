package ru.baronessdev.free.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.baronessdev.free.editor.data.Project;
import ru.baronessdev.free.editor.handlers.ProjectHandler;
import ru.baronessdev.free.editor.handlers.QueryHandler;
import ru.baronessdev.free.editor.util.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.baronessdev.free.editor.BaronessEditor.getMessage;

public class ClickListener implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        ClickType clickType = e.getClick();
        if (!clickType.isRightClick() && !clickType.isShiftClick()) return;
        if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
        Player p = (Player) e.getWhoClicked();

        Optional<Project> optionalProject = ProjectHandler.getProject(p);
        if (!optionalProject.isPresent()) return;
        Project project = optionalProject.get();

        if (!e.getClickedInventory().equals(project.getInventory())) return;
        e.setCancelled(true);
        p.closeInventory();

        List<String> l = new ArrayList<>();
        l.add(e.getSlot() + "");
        QueryHandler.addCache(p, l);

        if (clickType.isRightClick()) {
            p.sendMessage(getMessage("click_name"));
            QueryHandler.addQuery(p, Query.CLICK_RENAME);
            return;
        }

        p.sendMessage(String.format(getMessage("click_lore"), 0));
        QueryHandler.addQuery(p, Query.CLICK_LORE);
    }
}
