package ru.baronessdev.free.editor.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.baronessdev.free.editor.BaronessEditor;
import ru.baronessdev.free.editor.data.Project;
import ru.baronessdev.free.editor.util.Query;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static ru.baronessdev.free.editor.BaronessEditor.getMessage;

public class QueryHandler implements Listener {

    private static final HashMap<Player, Query> query = new HashMap<>();
    private static final HashMap<Player, Object> cache = new HashMap<>();

    public static void addQuery(Player p, Query q) {
        query.put(p, q);
    }

    public static void addCache(Player p, Object obj) {
        if (obj == null) {
            cache.remove(p);
        } else {
            cache.put(p, obj);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Query q = query.get(p);
        if (q == null) return;
        e.setCancelled(true);

        boolean clear = false;

        switch (q) {
            case OPEN: {
                clear = open(p, e.getMessage());
                break;
            }
            case CREATE_1: {
                clear = create_1(p, e.getMessage());
                break;
            }
            case CREATE_2: {
                clear = create_2(p, e.getMessage());
                break;
            }
            case DELETE_1: {
                clear = delete_1(p, e.getMessage());
                break;
            }
            case DELETE_2: {
                clear = delete_2(p, e.getMessage());
                break;
            }
            case RENAME_MENU: {
                clear = rename_menu(p, e.getMessage());
                break;
            }
            case RENAME_PROJECT: {
                clear = rename_project(p, e.getMessage());
                break;
            }
            case CLICK_RENAME: {
                clear = click_rename(p, e.getMessage());
                break;
            }
            case CLICK_LORE: {
                clear = click_lore(p, e.getMessage());
            }
        }

        if (clear) {
            query.remove(p);
            cache.remove(p);
        }
    }

    private boolean open(Player p, String s) {
        File file = new File(BaronessEditor.getInstance().getDataFolder() + File.separator + "projects" + File.separator + s + ".yml");
        if (!file.exists()) {
            p.sendMessage(getMessage("open_not_found"));
            return false;
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Project project = new Project();
        project.setName(s);

        int size = cfg.getInt("size");
        Inventory inv = Bukkit.createInventory(null, size, cfg.getString("name"));

        for (int i = 0; i < size; i++) {
            ItemStack item = cfg.getItemStack(String.valueOf(i));
            if (item != null) inv.setItem(i, item);
        }

        project.setInventory(inv);
        ProjectHandler.setProject(p, project);
        p.sendMessage(getMessage("open_opened"));
        return true;
    }

    private boolean create_1(Player p, String s) {
        int size = Integer.parseInt(s);

        if (size % 9 != 0) {
            p.sendMessage(getMessage("create_wrong_size"));
            return false;
        }

        p.sendMessage(getMessage("create_set_name"));
        cache.put(p, size);
        addQuery(p, Query.CREATE_2);
        return false;
    }

    private boolean create_2(Player p, String s) {
        int size = (int) cache.remove(p);
        String title = s.replace('&', ChatColor.COLOR_CHAR);

        Project project = new Project();
        project.setName(title);
        project.setTitle(title);
        project.setInventory(Bukkit.createInventory(null, size, title));
        ProjectHandler.setProject(p, project);

        p.sendMessage(getMessage("create_created"));
        return true;
    }

    private boolean delete_1(Player p, String s) {
        File file = new File(BaronessEditor.getInstance().getDataFolder() + File.separator + "projects" + File.separator + s + ".yml");
        if (!file.exists()) {
            p.sendMessage(getMessage("delete_not_found"));
            return false;
        }

        cache.put(p, s);
        p.sendMessage(String.format(getMessage("delete_confirm"), p.getName()));
        addQuery(p, Query.DELETE_2);
        return false;
    }

    private boolean delete_2(Player p, String s) {
        if (!s.equals(p.getName())) {
            p.sendMessage(getMessage("delete_cancel"));
            return true;
        }

        File file = new File(BaronessEditor.getInstance().getDataFolder() + File.separator + "projects" + File.separator + cache.get(p) + ".yml");
        if (file.delete()) {
            p.sendMessage(getMessage("delete_finish"));
        }
        return true;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private boolean rename_menu(Player p, String s) {
        Project project = ProjectHandler.getProject(p).get();
        Inventory oldInventory = project.getInventory();

        String title = s.replace('&', ChatColor.COLOR_CHAR);

        Inventory inv = Bukkit.createInventory(null, oldInventory.getSize(), title);
        for (int i = 0; i < oldInventory.getSize(); i++) {
            ItemStack item = oldInventory.getItem(i);
            if (item != null) inv.setItem(i, item);
        }

        project.setInventory(inv);
        project.setTitle(title);
        p.sendMessage(getMessage("rename_changed_menu"));
        return true;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private boolean rename_project(Player p, String s) {
        ProjectHandler.getProject(p).get().setName(s);
        p.sendMessage(getMessage("rename_changed_project"));
        return true;
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked"})
    private boolean click_rename(Player p, String s) {
        if (s.equals("cancel")) {
            p.sendMessage(getMessage("click_cancel"));
            return true;
        }

        ItemStack i = ProjectHandler.getProject(p).get().getInventory().getItem(Integer.parseInt(((List<String>) cache.get(p)).get(0)));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(s.replace('&', ChatColor.COLOR_CHAR));
        i.setItemMeta(meta);

        p.sendMessage(getMessage("click_name_done"));
        return true;
    }

    @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
    private boolean click_lore(Player p, String s) {
        if (s.equals("cancel")) {
            p.sendMessage(getMessage("click_cancel"));
            return true;
        }

        List<String> lore = (List<String>) cache.get(p);
        if (s.equals("done")) {
            ItemStack i = ProjectHandler.getProject(p).get().getInventory().getItem(Integer.parseInt(lore.get(0)));
            ItemMeta meta = i.getItemMeta();

            lore.remove(0);
            for (int j = 0; j < lore.size(); j++) {
                lore.set(j, lore.get(j).replace('&', ChatColor.COLOR_CHAR));
            }

            meta.setLore(lore);
            i.setItemMeta(meta);

            p.sendMessage(getMessage("click_name_lore"));
            return true;
        }

        lore.add(s);
        p.sendMessage(String.format(getMessage("click_lore"), lore.size() - 1));
        return false;
    }
}
