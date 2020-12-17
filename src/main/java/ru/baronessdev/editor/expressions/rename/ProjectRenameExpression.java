package ru.baronessdev.editor.expressions.rename;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.expressions.ExampleExpression;

public class ProjectRenameExpression extends ExampleExpression {
    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        Executor.getData().getData().get(e.getPlayer()).setName(e.getMessage());

        e.getPlayer().sendMessage(getMessage("rename_changed_project"));
    }
}
