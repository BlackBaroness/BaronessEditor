package ru.baronessdev.free.editor;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.free.editor.handlers.CommandHandler;
import ru.baronessdev.free.editor.handlers.ProjectHandler;
import ru.baronessdev.free.editor.handlers.QueryHandler;
import ru.baronessdev.free.editor.util.Metrics;

import java.io.File;

public final class BaronessEditor extends JavaPlugin {

    private static FileConfiguration cfg;
    @Getter
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Metrics(this, 9674);
        cfg = getConfig();
        instance = this;

        Bukkit.getPluginManager().registerEvents(new QueryHandler(), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);

        PaperCommandManager manager = new PaperCommandManager(this);
        registerCommands(manager);
        registerCompletions(manager);

        if (new File(getDataFolder() + File.separator + "projects").mkdirs()) {
            System.out.println(ChatColor.AQUA + "[BaronessEditor] Projects dir created.");
        }

        System.out.println(ChatColor.AQUA + "[BaronessEditor] Version " + getDescription().getVersion() + " enabled.");
    }

    private void registerCommands(PaperCommandManager manager) {
        manager.registerCommand(new CommandHandler());
    }

    private void registerCompletions(PaperCommandManager manager) {
        manager.getCommandCompletions().registerAsyncCompletion("beditorHelp", o ->
                (ProjectHandler.getProject(o.getPlayer()).isPresent())
                        ? ImmutableList.of("edit", "rename", "save", "close")
                        : ImmutableList.of("open", "create", "browse", "delete"));
    }


    public static String getMessage(String path) {
        return cfg.getString(path).replace('&', ChatColor.COLOR_CHAR);
    }
}
