package us.jcedeno.game.players;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import us.jcedeno.game.players.enums.Role;
import us.jcedeno.game.players.objects.SquidGuard;
import us.jcedeno.game.players.objects.SquidParticipant;
import us.jcedeno.game.players.objects.SquidPlayer;

public class PlayerManager {

    private volatile Map<String, SquidParticipant> participants;

    public PlayerManager() {
        // Use concurrent hashmap for thread safety.
        this.participants = new ConcurrentHashMap<>();
    }

    public SquidPlayer addPlayer(final Player player) {
        // Check if player is already a participant.
        if (this.participants.containsKey(player.getName()))
            throw new IllegalArgumentException("Player is already a participant.");
            
        var previous = participants.putIfAbsent(player.getName(),
                new SquidPlayer(player.getName(), (getPlayers() + 1)));

        return null;
    }

    public SquidPlayer getPlayer(String name) {
        var query = participants.get(name);

        if (query instanceof SquidPlayer squidPlayer)
            return squidPlayer;
        else
            return null;
    }

    public int getPlayers() {
        return (int) participants.values().stream().filter(p -> p instanceof SquidPlayer).count();
    }

    public SquidGuard getGuard(String name) {
        var query = participants.get(name);

        if (query instanceof SquidGuard squidGuard)
            return squidGuard;
        else
            return null;
    }

    public void changeRole(String name, Role role) {
        var player = participants.get(name);

        if (player == null)
            throw new NullPointerException("Player not found");

        if (player.getRole() == role)
            throw new IllegalArgumentException("Player is already " + role.name());

    }

}
