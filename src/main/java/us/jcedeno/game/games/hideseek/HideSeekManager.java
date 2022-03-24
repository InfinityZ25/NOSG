package us.jcedeno.game.games.hideseek;

import lombok.Getter;
import us.jcedeno.game.games.GameManager;

public class HideSeekManager {

    private final @Getter GameManager gManager;

    private @Getter boolean isRunning;

    public HideSeekManager(GameManager gManager) {
        this.gManager = gManager;
    }

}
