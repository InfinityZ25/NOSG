package me.lofro.core.paper.objects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import me.lofro.core.paper.Game;

public class SquidGuard extends SquidParticipant {

    public SquidGuard(@Nonnull String name) {
        super(name, Game.Role.GUARD);
    }

    public SquidGuard(@Nonnull String name, boolean dead) {
        super(name, Game.Role.GUARD, dead);
    }

    public SquidGuard(@Nonnull String name, boolean dead, @Nonnull Player player) {
        super(name, Game.Role.GUARD, dead, player);
    }

}
