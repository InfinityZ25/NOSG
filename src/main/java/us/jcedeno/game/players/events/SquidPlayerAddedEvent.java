package us.jcedeno.game.players.events;

import org.bukkit.Bukkit;

import us.jcedeno.game.players.events.types.BaseEvent;
import us.jcedeno.game.players.objects.SquidPlayer;

public class SquidPlayerAddedEvent extends BaseEvent {

    private final SquidPlayer squidPlayer;
    private final long time;

    public SquidPlayerAddedEvent(final SquidPlayer squidPlayer) {
        super(!Bukkit.isPrimaryThread());
        this.squidPlayer = squidPlayer;
        this.time = System.nanoTime();
    }

    public SquidPlayer getSquidPlayer() {
        return squidPlayer;
    }

}
