package me.lofro.game.games.deathnote.listeners;

import me.lofro.game.games.deathnote.DeathNoteManager;
import me.lofro.game.global.utils.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record DeathNoteListener(DeathNoteManager dNManager) implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var name = player.getName();
        var loc = player.getLocation();

        if (!dNManager.playerManager().isPlayer(player) || !dNManager.isRunning() || dNManager.getWinners().contains(player))
            return;

        var squidPlayer = dNManager.playerManager().pData().getPlayer(name);
        var id = squidPlayer.getId();

        var winners = dNManager.getWinners().size();

        if (dNManager.inArea(loc)) {
            if (winners <= dNManager.getWinnerLimit()) {
                dNManager.getWinners().add(player);

                winners = dNManager.getWinners().size();

                if (winners == dNManager.getWinnerLimit()) {
                    Bukkit.broadcast(Component.text(Strings.format("&bEl jugador &3#" + id + " " + name + " &bha pasado la ronda.")));
                    Bukkit.broadcast(Component.text(Strings.format("&cNo quedan plazas restantes.")));

                    dNManager.endGame();
                } else {
                    Bukkit.broadcast(Component.text(Strings.format("&bEl jugador &3#" + id + " " + name + " &bha pasado la ronda.")));
                    Bukkit.broadcast(Component.text(Strings.format("&eQuedan &6" + (dNManager.getWinnerLimit() - winners) + "&e plazas restantes.")));
                }
            }
        }
    }

}
