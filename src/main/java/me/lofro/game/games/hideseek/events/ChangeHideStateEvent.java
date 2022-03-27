package me.lofro.game.games.hideseek.events;

import lombok.Getter;
import me.lofro.game.games.hideseek.enums.HideGameState;
import me.lofro.game.global.events.types.BaseEvent;

public class ChangeHideStateEvent extends BaseEvent {

    private final @Getter HideGameState hideGameState;

    public ChangeHideStateEvent(HideGameState hideGameState, boolean async) {
        super(async);
        this.hideGameState = hideGameState;
    }

    public ChangeHideStateEvent(HideGameState hideGameState) {
        this.hideGameState = hideGameState;
    }

}
