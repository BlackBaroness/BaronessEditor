package ru.baronessdev.free.editor.handlers;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.baronessdev.free.editor.BaronessEditor;
import ru.baronessdev.free.editor.data.Project;
import ru.baronessdev.free.editor.util.Query;
import ru.baronessdev.free.editor.util.SmartMessagesBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static ru.baronessdev.free.editor.BaronessEditor.getMessage;

@SuppressWarnings("SpellCheckingInspection")
@CommandAlias("beditor")
@CommandPermission("baronesseditor.use")
public class CommandHandler extends BaseCommand {

    @HelpCommand
    @CatchUnknown
    @Default
    @CommandCompletion("@beditorHelp")
    public void unknown(Player p) {
        // проект открыт
        Optional<Project> project = ProjectHandler.getProject(p);
        if (project.isPresent()) {
            for (int i = 6; i < 11; i++) {
                p.sendMessage(String.format(getMessage("help_" + i), project.get().getName()));
            }
            return;
        }

        // проект закрыт
        for (int i = 1; i < 6; i++) {
            p.sendMessage(getMessage("help_" + i));
        }
    }

    @Subcommand("open")
    public void open(Player p) {
        p.sendMessage(getMessage("open_choose"));
        QueryHandler.addQuery(p, Query.OPEN);
    }

    @Subcommand("create")
    public void create(Player p) {
        p.sendMessage(getMessage("create_set_size"));
        QueryHandler.addQuery(p, Query.CREATE_1);
    }

    @Subcommand("browse")
    public void browse(Player p) {
        File[] files = new File(BaronessEditor.getInstance().getDataFolder() + File.separator + "projects").listFiles();
        if (files == null || files.length == 0) {
            p.sendMessage(getMessage("browse_no_projects"));
            return;
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
    }

    @Subcommand("delete")
    public void delete(Player p) {
        p.sendMessage(getMessage("delete_type"));
        QueryHandler.addQuery(p, Query.DELETE_1);
    }

    @Subcommand("edit")
    public void edit(Player p) {
        projectRequired(p, project -> p.openInventory(project.getInventory()));
    }

    @Subcommand("rename")
    public void rename(Player p, String[] args) {
        projectRequired(p, project -> {
            if (args.length == 1) {
                String s = args[0];
                if (s.equals("project")) {
                    p.sendMessage(getMessage("rename_name_project"));
                    QueryHandler.addQuery(p, Query.RENAME_PROJECT);
                    return;
                }
                if (s.equals("menu")) {
                    p.sendMessage(getMessage("rename_name_menu"));
                    QueryHandler.addQuery(p, Query.RENAME_MENU);
                    return;
                }
            }

            new SmartMessagesBuilder(getMessage("rename_choose_project"))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMessage("rename_hover_project")).create()))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/beditor rename project"))
                    .send(p);

            new SmartMessagesBuilder(getMessage("rename_choose_menu"))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMessage("rename_hover_menu")).create()))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/beditor rename menu"))
                    .send(p);
        });
    }

    @Subcommand("save")
    public void save(Player p) {
        projectRequired(p, project -> {
            File file = new File(BaronessEditor.getInstance().getDataFolder() + File.separator + "projects" + File.separator + project.getName() + ".yml");

            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            cfg.set("size", project.getInventory().getSize());
            cfg.set("name", project.getTitle());

            for (int i = 0; i < project.getInventory().getSize(); i++) {
                ItemStack item = project.getInventory().getItem(i);
                cfg.set(String.valueOf(i), item);
            }

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage(String.format(getMessage("save"), project.getName()));
        });
    }

    @Subcommand("close")
    public void close(Player p) {
        projectRequired(p, project -> {
            ProjectHandler.closeProject(p);
            p.sendMessage(getMessage("close"));
        });
    }


    private void projectRequired(Player p, Consumer<? super Project> task) {
        Optional<Project> project = ProjectHandler.getProject(p);
        if (!project.isPresent()) {
            p.sendMessage(getMessage("projectClosed"));
            return;
        }

        task.accept(project.get());
    }
}
