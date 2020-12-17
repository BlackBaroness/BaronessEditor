package ru.baronessdev.editor;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.baronessdev.editor.data.MainData;
import ru.baronessdev.editor.data.Project;
import ru.baronessdev.editor.expressions.create.SetSizeExpression;
import ru.baronessdev.editor.expressions.delete.DeleteExpression;
import ru.baronessdev.editor.expressions.open.OpenExpression;
import ru.baronessdev.editor.expressions.rename.MenuRenameExpression;
import ru.baronessdev.editor.expressions.rename.ProjectRenameExpression;
import ru.baronessdev.editor.listeners.ChatListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.baronessdev.editor.Core.*;

public class Executor implements CommandExecutor {

    @Getter
    private static MainData data;

    public Executor(MainData data) {
        Executor.data = data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("player_only"));
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            help(p);
            return true;
        }

        if (data.getData().containsKey(p)) {
            /* У игрока есть открытый проект */
            if (args[0].equals("edit")) {
                p.openInventory(data.getData().get(p).getInventory());
                return true;
            }

            if (args[0].equals("rename")) {
                if (args.length == 2) {
                    String s = args[1];
                    if (s.equals("project")) {
                        p.sendMessage(getMessage("rename_name_project"));
                        new ChatListener(p, new ProjectRenameExpression(), null);
                        return true;
                    }
                    if (s.equals("menu")) {
                        p.sendMessage(getMessage("rename_name_menu"));
                        new ChatListener(p, new MenuRenameExpression(), null);
                        return true;
                    }
                }
                TextComponent message1 = new TextComponent(getMessage("rename_choose_project"));
                message1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMessage("rename_hover_project")).create()));
                message1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/beditor rename project"));

                TextComponent message2 = new TextComponent(getMessage("rename_choose_menu"));
                message2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMessage("rename_hover_menu")).create()));
                message2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/beditor rename menu"));

                p.spigot().sendMessage(message1);
                p.spigot().sendMessage(message2);
                return true;
            }

            if (args[0].equals("save")) {
                Project project = data.getData().get(p);
                File file = new File(Core.getCore().getDataFolder() + File.separator + "projects" + File.separator + project.getName() + ".yml");

                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

                cfg.set("size", project.getInventory().getSize());
                cfg.set("title", project.getTitle());

                for (int i = 0; i < project.getInventory().getSize(); i++) {
                    ItemStack item = project.getInventory().getItem(i);
                    if (item != null) cfg.set(String.valueOf(i), item);
                }

                try {
                    cfg.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                p.sendMessage(String.format(getMessage("save"), project.getName()));
                return true;
            }

            if (args[0].equals("close")) {
                data.getData().remove(p);
                p.sendMessage(getMessage("close"));
                return true;
            }

            help(p);
            return true;
        }

        /* У игрока нет открытого проекта */
        if (args[0].equals("browse")) {
            File[] files = new File(Core.getCore().getDataFolder() + File.separator + "projects").listFiles();
            if (files == null || files.length == 0) {
                p.sendMessage(getMessage("browse_no_projects"));
                return true;
            }
            p.sendMessage(getMessage("browse_head"));

            AtomicInteger integer = new AtomicInteger(0);
            Arrays.stream(files).forEach(file -> {
                integer.set(integer.get() + 1);
                p.sendMessage(ChatColor.AQUA + "" +
                        integer.get() +
                        ". " +
                        ChatColor.GRAY +
                        file.getName().replace(".yml", ""));
            });
            return true;
        }

        if (args[0].equals("create")) {
            create(p);
            return true;
        }

        if (args[0].equals("delete")) {
            p.sendMessage(getMessage("delete_type"));
            new ChatListener(p, new DeleteExpression(), null);
            return true;
        }
        if (args[0].equals("open")) {
            p.sendMessage(getMessage("open_choose"));
            new ChatListener(p, new OpenExpression(), null);
            return true;
        }

        help(p);
        return true;
    }

    public static void create(Player p) {
        p.sendMessage(getMessage("create_set_size"));
        new ChatListener(p, new SetSizeExpression(), null);
    }

    private void help(Player p) {
        if (data.getData().containsKey(p)) {
            /* У игрока есть открытый проект */
            String[] help = {
                    getMessage("help_6"),
                    getMessage("help_7"),
                    getMessage("help_8"),
                    getMessage("help_9"),
                    getMessage("help_10")
            };
            help[0] = String.format(help[0], data.getData().get(p).getName());

            Arrays.stream(help).forEach(p::sendMessage);
            return;
        }

        /* У игрока нет открытого проекта */
        String[] help = {
                getMessage("help_1"),
                getMessage("help_2"),
                getMessage("help_3"),
                getMessage("help_4"),
                getMessage("help_5"),
        };
        Plugin plugin = Bukkit.getPluginManager().getPlugin("BaronessEditor");
        if (!help[0].contains("{plugin}")) {
            p.sendMessage(ChatColor.RED + "[BaronessEditor] Where is {plugin} tag?");
            System.out.println(ChatColor.RED + "[BaronessEditor] Where is {plugin} tag?");
            return;
        }
        help[0] = help[0].replace("{plugin}", plugin.getName() + " " + plugin.getDescription().getVersion());

        Arrays.stream(help).forEach(p::sendMessage);
    }

}

