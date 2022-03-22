package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import us.jcedeno.game.players.enums.Role;

public class SquidPlayer extends SquidParticipant {

    private @Setter @Getter int id;

    public SquidPlayer(@Nonnull String name, int id) {
        super(name, Role.PLAYER);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead) {
        super(name, Role.PLAYER, dead);
        this.id = id;
    }

    public SquidPlayer(@Nonnull String name, int id, boolean dead, @Nonnull Player player) {
        super(name, Role.PLAYER, dead, player);
        this.id = id;
    }

}
