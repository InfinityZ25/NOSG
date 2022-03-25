package me.lofro.game.games.hideseek;

import lombok.Getter;
import me.lofro.game.games.GameManager;

public class HideSeekManager {

    private final @Getter GameManager gManager;

    private @Getter boolean isRunning;

    public HideSeekManager(GameManager gManager) {
        this.gManager = gManager;
    }

}
