package ru.baronessdev.editor;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.baronessdev.editor.data.MainData;
import ru.baronessdev.editor.listeners.ClickListener;
import ru.baronessdev.editor.listeners.MainListener;

import java.io.*;
import java.util.logging.Level;

public final class Core extends JavaPlugin {

    private static YamlConfiguration messages;
    private static File messagesFile;
    @Getter
    private static Core core;

    @Override
    public void onEnable() {
        // загрузка сообщений
        saveResource("messages.yml");
        messagesFile = new File(getDataFolder() + File.separator + "messages.yml");
        load();
        getCommand("beditor").setExecutor(new Executor(new MainData()));

        // заряд супер дупер ультра крутого главного слушателя
        MainListener.setPlugin(this);
        core = this;
        new ClickListener();

        // папочка с проектами
        if (new File(getDataFolder() + File.separator + "projects").mkdirs())
            System.out.println(ChatColor.AQUA + "[BaronessEditor] Projects dir created.");

        // аплодисменты
        System.out.println(ChatColor.AQUA + "[BaronessEditor] Version " + getDescription().getVersion() + " enabled.");

        new Metrics(this, 9674);
    }

    public static void load() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static String getMessage(String path) {
        return messages.getString(path).replace('&', ChatColor.COLOR_CHAR);
    }

    @SuppressWarnings("all")
    public void saveResource(String resourcePath) {
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }
}
