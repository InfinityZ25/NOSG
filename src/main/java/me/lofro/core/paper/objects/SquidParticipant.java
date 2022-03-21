package me.lofro.core.paper.objects;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Game.Role;
import me.lofro.core.paper.Main;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SquidParticipant {

    private @Getter @Setter String name;
    private @Getter Role role;
    private @Getter boolean dead;
    private @Getter Player player;
    private final @Getter Main instance;

    public SquidParticipant(@Nonnull String name, @Nonnull Role role, @Nonnull Main instance) {
        this.name = name;
        this.role = role;
        this.instance = instance;
        instance.getGame().getParticipantRoles().putIfAbsent(name, role);
    }

    public SquidParticipant(@Nonnull String name, @Nonnull Role role, boolean dead, @Nonnull Main instance) {
        this.name = name;
        this.role = role;
        this.dead = dead;
        this.instance = instance;
        instance.getGame().getParticipantRoles().putIfAbsent(name, role);
        instance.getGame().getParticipantDeadStates().putIfAbsent(name, dead);
    }

    public SquidParticipant(@Nonnull String name, @Nonnull Role role, boolean dead, @Nonnull Player player, @Nonnull Main instance) {
        this.name = name;
        this.role = role;
        this.dead = dead;
        this.player = player;
        this.instance = instance;
        instance.getGame().getParticipantRoles().putIfAbsent(name, role);
        instance.getGame().getParticipantDeadStates().putIfAbsent(name, dead);
    }

    public void setRole(Role role) {
        this.role = role;
        instance.getGame().getParticipantRoles().put(this.name, role);
        if (player != null) instance.getGame().loadParticipant(player);
    }

    public void setDead(boolean dead) {
        this.dead = dead;
        instance.getGame().getParticipantDeadStates().put(this.name, dead);
        instance.getGame().getParticipants().put(name, this);
    }
}
