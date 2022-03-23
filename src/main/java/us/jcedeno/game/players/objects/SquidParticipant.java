package us.jcedeno.game.players.objects;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import us.jcedeno.game.players.enums.Role;

public class SquidParticipant {

    private @Getter @Setter String name;
    private @Setter @Getter Role role;
    private @Setter @Getter boolean dead;

    public SquidParticipant(@Nonnull String name, @Nonnull Role role) {
        this.name = name;
        this.role = role;
    }

    public SquidParticipant(@Nonnull String name, @Nonnull Role role, boolean dead) {
        this.name = name;
        this.role = role;
        this.dead = dead;
    }

    /**
     * @return the player associated with this participant if online, otherwise null
     */
    public Player player() {
        return Bukkit.getPlayer(name);
    }

}
