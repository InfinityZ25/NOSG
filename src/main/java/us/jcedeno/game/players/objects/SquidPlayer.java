package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import lombok.Getter;
import us.jcedeno.game.players.enums.Role;

public class SquidPlayer extends SquidParticipant {

    private @Getter int id;

    public SquidPlayer(@Nonnull String name, int id) {
        super(name, Role.PLAYER);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead) {
        super(name, Role.PLAYER, dead);
        this.id = id;
    }

}
