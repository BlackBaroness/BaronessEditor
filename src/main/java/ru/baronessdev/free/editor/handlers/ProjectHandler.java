package ru.baronessdev.free.editor.handlers;

import org.bukkit.entity.Player;
import ru.baronessdev.free.editor.data.Project;

import java.util.HashMap;
import java.util.Optional;

public class ProjectHandler {

    private static final HashMap<Player, Project> projects = new HashMap<>();

    public static Optional<Project> getProject(Player p) {
        return Optional.ofNullable(projects.get(p));
    }

    public static void setProject(Player p, Project project) {
        projects.put(p, project);
    }

    public static void closeProject(Player p) {
        projects.remove(p);
    }
}
