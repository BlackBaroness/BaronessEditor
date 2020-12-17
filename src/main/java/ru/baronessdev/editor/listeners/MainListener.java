package ru.baronessdev.editor.listeners;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainListener {

    /*
    Супер дупер ультра крутой главный слушатель, нужный для работы супер мега крутых слушателей
    Настоящий русский богатырь
     */

    @Setter
    private static JavaPlugin plugin;

    public static void activate(Listener l) {
        Bukkit.getPluginManager().registerEvents(l, plugin);
    }

    public static void deactivate(Listener l) {
        HandlerList.unregisterAll(l);
    }
}
