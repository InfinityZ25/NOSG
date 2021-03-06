package me.lofro.core.paper.objects;

import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SquidGuard extends SquidParticipant {

    public SquidGuard(@Nonnull String name, Main instance) {
        super(name, Game.Role.GUARD, instance);
    }

    public SquidGuard(@Nonnull String name, boolean dead, @Nonnull Main instance) {
        super(name, Game.Role.GUARD, dead, instance);
    }

    public SquidGuard(@Nonnull String name, boolean dead, @Nonnull Player player, @Nonnull Main instance) {
        super(name, Game.Role.GUARD, dead, player, instance);
    }

    @Override
    public void setRole(Game.Role role) {
        super.setRole(role);
        getInstance().getGame().getGuards().put(getName(), this);
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead(dead);
        getInstance().getGame().getGuards().put(getName(), this);
    }

}
