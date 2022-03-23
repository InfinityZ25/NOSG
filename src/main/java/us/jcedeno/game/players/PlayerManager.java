package us.jcedeno.game.players;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import us.jcedeno.game.Squid;
import us.jcedeno.game.players.enums.Role;
import us.jcedeno.game.players.events.SquidPlayerAddedEvent;
import us.jcedeno.game.players.objects.SquidGuard;
import us.jcedeno.game.players.objects.SquidParticipant;
import us.jcedeno.game.players.objects.SquidPlayer;

/**
 * A class to manage the players in the game, their roles & status, and interact
 * with the dataset.
 * 
 * @author jcedeno
 * 
 */
public class PlayerManager {

    private volatile Map<String, SquidParticipant> participants;
    protected Squid instance;

    public PlayerManager(final Squid instance) {
        // Use concurrent hashmap for thread safety.
        this.participants = new ConcurrentHashMap<>();
        this.instance = instance;
    }


    //TODO Cleanup code below.

    public SquidPlayer addPlayer(final Player player) {
        // Check if player is already a participant.
        if (this.participants.containsKey(player.getName()))
            throw new IllegalArgumentException("Player is already a participant.");

        var squidPlayer = new SquidPlayer(player.getName(), (getPlayers() + 1));
        // Put player in map
        participants.put(player.getName(), squidPlayer);
        // Call Event
        callEvent(new SquidPlayerAddedEvent(squidPlayer));
        //

        return squidPlayer;
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

    /**
     * Short-hand static function to reduce boiler-plate.
     * 
     * @param event {@link Event} to be called
     */
    private static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}
