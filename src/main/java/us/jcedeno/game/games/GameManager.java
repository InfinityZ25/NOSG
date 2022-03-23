package us.jcedeno.game.games;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import us.jcedeno.game.Squid;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 * 
 * @author jcedeno
 */
public class GameManager {

    private final @Getter Squid squidInstance;

    public GameManager(Squid squidInstance) {
        this.squidInstance = squidInstance;
    }

}
