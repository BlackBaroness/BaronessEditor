package ru.baronessdev.free.editor.util.logging;

import org.bukkit.ChatColor;

public class Logger {

    public static void log(LogType type, String s) {
        System.out.println(
                ChatColor.AQUA + "[BaronessEditor] " +
                        getPrefix(type) + " " + s
        );
    }

    private static String getPrefix(LogType type) {
        switch (type) {
            case INFO:
                return ChatColor.YELLOW + "[INFO]" + ChatColor.WHITE;
            case ERROR:
                return ChatColor.DARK_RED + "[ERROR]" + ChatColor.RED;
            default:
                return "";
        }
    }
}
