package ru.baronessdev.editor.expressions.create;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.baronessdev.editor.Executor;
import ru.baronessdev.editor.expressions.ExampleExpression;
import ru.baronessdev.editor.listeners.ChatListener;

public class SetSizeExpression extends ExampleExpression {

    @Override
    public void execute(Object o, Object obj) {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) o;
        int size = Integer.parseInt(e.getMessage());
        Player p = e.getPlayer();

        if (size % 9 != 0) {
            p.sendMessage(getMessage("create_wrong_size"));
            Executor.create(p);
            return;
        }

        p.sendMessage(getMessage("create_set_name"));
        new ChatListener(p, new SetNameExpression(), size);
    }
}
