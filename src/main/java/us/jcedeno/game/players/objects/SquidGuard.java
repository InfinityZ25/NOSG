package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import us.jcedeno.game.players.enums.Role;

public class SquidGuard extends SquidParticipant {

    public SquidGuard(@Nonnull String name) {
        super(name, Role.GUARD);
    }

    public SquidGuard(@Nonnull String name, boolean dead) {
        super(name, Role.GUARD, dead);
    }

    public SquidGuard(@Nonnull String name, boolean dead, @Nonnull Player player) {
        super(name, Role.GUARD, dead, player);
    }

}
