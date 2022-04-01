package me.lofro.game.games.tntTag.listeners;

import me.lofro.game.games.tntTag.TNTManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TNTListener implements Listener {

    private final TNTManager tntManager;

    public TNTListener(TNTManager tntManager) {
        this.tntManager = tntManager;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!tntManager.isRunning()) return;

        var entity = e.getEntity();
        var damager = e.getDamager();

        if (entity instanceof Player pEntity) {
            if (damager instanceof Player pDamager) {
                e.setDamage(0);
                if (!tntManager.playerManager().isPlayer(pEntity) || !tntManager.playerManager().isPlayer(pDamager)) return;
                if (tntManager.getTaggeds().size() <= tntManager.getTaggedLimit()) {
                    if (tntManager.isTagged(pEntity) || !tntManager.isTagged(pDamager)) return;

                    tntManager.switchTag(pDamager, pEntity);
                }
            }
        }
    }

}
