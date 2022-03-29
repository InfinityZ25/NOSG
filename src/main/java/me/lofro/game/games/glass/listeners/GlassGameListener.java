package me.lofro.game.games.glass.listeners;

import me.lofro.game.games.glass.GlassGameManager;
import me.lofro.game.games.glass.enums.GlassGameState;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GlassGameListener implements Listener {

    private final GlassGameManager glassGManager;

    public GlassGameListener(GlassGameManager glassGManager) {
        this.glassGManager = glassGManager;
    }

    @EventHandler
    public void onStep(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var loc = player.getLocation();

        if (player.getGameMode() != GameMode.ADVENTURE || !e.hasExplicitlyChangedBlock()) return;
        if (!glassGManager.inArea(loc) || !glassGManager.getGlassGameState().equals(GlassGameState.RUNNING)) return;

        var relativeDown = e.getTo().getBlock().getRelative(BlockFace.DOWN);

        glassGManager.breakGlass(relativeDown, true);
    }

}
