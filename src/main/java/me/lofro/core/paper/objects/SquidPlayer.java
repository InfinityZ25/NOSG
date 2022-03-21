package me.lofro.core.paper.objects;

import lombok.Getter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SquidPlayer extends SquidParticipant {

    private @Getter int id;

    public SquidPlayer(@Nonnull String name, int id, @Nonnull Main instance) {
        super(name, Game.Role.PLAYER, instance);
        this.id = id;
        getInstance().getGame().getPlayerIds().putIfAbsent(name, id);
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead, @Nonnull Main instance) {
        super(name, Game.Role.PLAYER, dead, instance);
        this.id = id;
        getInstance().getGame().getPlayerIds().putIfAbsent(name, id);
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead, @Nonnull Player player, @Nonnull Main instance) {
        super(name, Game.Role.PLAYER, dead, player, instance);
        this.id = id;
        getInstance().getGame().getPlayerIds().putIfAbsent(name, id);
    }

    @Override
    public void setRole(Game.Role role) {
        super.setRole(role);
        getInstance().getGame().getPlayers().put(getName(), this);
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead(dead);
        getInstance().getGame().getPlayers().put(getName(), this);
    }

    public void setId(int id) {
        this.id = id;
        getInstance().getGame().getPlayerIds().put(getName(), id);
        getInstance().getGame().getPlayers().put(getName(), this);
    }

}
