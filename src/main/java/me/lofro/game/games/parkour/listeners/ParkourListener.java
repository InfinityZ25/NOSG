package me.lofro.game.games.parkour.listeners;

import me.lofro.game.games.parkour.ParkourManager;
import me.lofro.game.global.utils.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record ParkourListener(ParkourManager pkManager) implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var name = player.getName();
        var loc = player.getLocation();

        if (!pkManager.playerManager().isPlayer(player) || !pkManager.isRunning() || pkManager.getWinners().contains(player)
                || player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        var squidPlayer = pkManager.playerManager().pData().getPlayer(name);
        var id = squidPlayer.getId();

        var winners = pkManager.getWinners().size();
        var winnerLimit = pkManager.getWinnerLimit();

        if (pkManager.inGoal(loc)) {
            if (winners <= winnerLimit) {
                pkManager.getWinners().add(player);

                winners = pkManager.getWinners().size();

                Bukkit.broadcast(Component.text(Strings.format("&bEl jugador &3#" + id + " " + name + " &bha pasado el parkour.")));
                if (winners == winnerLimit) {
                    Bukkit.broadcast(Component.text(Strings.format("&cNo quedan plazas restantes.")));

                    pkManager.endGame();
                } else {
                    Bukkit.broadcast(Component.text(Strings.format("&eQuedan &6" + (pkManager.getWinnerLimit() - winners) + "&e plazas restantes.")));
                }
            }
        }
    }

}
