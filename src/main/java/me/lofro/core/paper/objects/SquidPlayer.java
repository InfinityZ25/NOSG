package me.lofro.core.paper.objects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Game;

public class SquidPlayer extends SquidParticipant {

    private @Setter @Getter int id;

    public SquidPlayer(@Nonnull String name, int id) {
        super(name, Game.Role.PLAYER);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead) {
        super(name, Game.Role.PLAYER, dead);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead, @Nonnull Player player) {
        super(name, Game.Role.PLAYER, dead, player);
        this.id = id;
    }

}
