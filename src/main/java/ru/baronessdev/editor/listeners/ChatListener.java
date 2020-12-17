package ru.baronessdev.editor.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.baronessdev.editor.expressions.ExampleExpression;

public class ChatListener implements Listener {

    /*
    Супер мега крутой слушатель, умеющий подстраиваться под любые ситуации (на самом деле не любые, только чат)
    Настоящий русский мужик
     */

    private final Player p;
    private final ExampleExpression expression;
    private final Object o;

    public ChatListener(Player p, ExampleExpression expression, Object o) {
        this.p = p;
        this.expression = expression;
        this.o = o;
        MainListener.activate(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(p)) MainListener.deactivate(this);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().equals(p)) {
            event.setCancelled(true);
            expression.execute(event, o);
            MainListener.deactivate(this);
        }
    }
}
