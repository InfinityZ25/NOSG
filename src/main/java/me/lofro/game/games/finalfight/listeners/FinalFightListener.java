package me.lofro.game.games.finalfight.listeners;

import me.lofro.game.games.finalfight.FinalFightManager;
import me.lofro.game.global.utils.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public record FinalFightListener(FinalFightManager finalFightManager) implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        var player = e.getPlayer();
        var name = player.getName();
        var killer = player.getKiller();

        if (!finalFightManager.playerManager().isPlayer(player) || killer == null) return;

        var nameKiller = killer.getName();

        var squidPlayer = finalFightManager.playerManager().pData().getPlayer(name);
        var id = squidPlayer.getId();

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.playSound(p.getLocation(), "sfx.winner", 1f, 1f);
            p.showTitle(Title.title(Component.text(Strings.format("&6¡GANADOR!")), Component.text(Strings.format("&e #" + id + " " + nameKiller))));
            p.sendMessage(Component.text(Strings.format("&eEl participante &6#" + id + " " + name + " &eha ganado los &6SQUID OTAKU GAMES.")));
        });
        finalFightManager.endGame();
    }

}
