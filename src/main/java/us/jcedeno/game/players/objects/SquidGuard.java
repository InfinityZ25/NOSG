package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import us.jcedeno.game.players.enums.Role;

public class SquidGuard extends SquidParticipant {

    public SquidGuard(@Nonnull String name) {
        super(name, Role.GUARD);
    }

    public SquidGuard(@Nonnull String name, boolean dead) {
        super(name, Role.GUARD, dead);
    }

}
