package ru.baronessdev.free.editor;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.free.editor.handlers.CommandHandler;
import ru.baronessdev.free.editor.handlers.ProjectHandler;
import ru.baronessdev.free.editor.handlers.QueryHandler;
import ru.baronessdev.free.editor.util.logging.LogType;
import ru.baronessdev.free.editor.util.logging.Logger;
import ru.baronessdev.free.editor.util.util.UpdateCheckerUtil;

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
            Logger.log(LogType.INFO, "Projects dir created.");
        }

        Logger.log(LogType.INFO, "Version " + getDescription().getVersion() + " enabled.");
        checkUpdates();
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

    private void checkUpdates() {
        try {
            int i = UpdateCheckerUtil.check(this);
            if (i != -1) {
                Logger.log(LogType.INFO, "New version found: v" + ChatColor.YELLOW + i + ChatColor.GRAY + " (Current: v" + getDescription().getVersion() + ")");
                Logger.log(LogType.INFO, "Update now: " + ChatColor.AQUA + "market.baronessdev.ru/shop/licenses/");
            }
        } catch (UpdateCheckerUtil.UpdateCheckException e) {
            Logger.log(LogType.ERROR, "Could not check for updates: " + e.getRootCause());
            Logger.log(LogType.ERROR, "Please contact Baroness's Dev if this isn't your mistake.");
        }
    }
}
