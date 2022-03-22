package us.jcedeno.game.players;

import java.util.Map;

import us.jcedeno.game.players.enums.Role;
import us.jcedeno.game.players.objects.SquidGuard;
import us.jcedeno.game.players.objects.SquidParticipant;
import us.jcedeno.game.players.objects.SquidPlayer;

public class PlayerManager {

    private Map<String, SquidParticipant> participants;

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

        if (player != null && player.getRole() == role)
            throw new IllegalArgumentException("Player is already " + role.name());
        
        

    }

}
