package me.lofro.game.games.hideseek.listeners;

import me.lofro.game.games.hideseek.HideSeekManager;
import me.lofro.game.games.hideseek.enums.HideGameState;
import me.lofro.game.games.hideseek.events.ChangeHideStateEvent;
import me.lofro.game.global.enums.PvPState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record HideSeekListener(HideSeekManager hSManager) implements Listener {

    @EventHandler
    public void onHideGameStateChange(ChangeHideStateEvent e) {
        var state = e.getHideGameState();
        var gData = hSManager.getGManager().gameData();
        if (state == HideGameState.HIDE) {
            gData.setPvPState(PvPState.NONE);
        } else {
            gData.setPvPState(PvPState.ONLY_GUARDS);
        }
    }

}
