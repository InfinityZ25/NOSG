package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import lombok.Getter;

public class SquidPlayer extends SquidParticipant {

    private @Getter int id;
    private @Getter boolean dead;

    public SquidPlayer(@Nonnull String name, int id) {
        super(name);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead) {
        super(name);
        this.id = id;
        this.dead = dead;
    }

}
