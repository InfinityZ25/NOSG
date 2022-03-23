package us.jcedeno.game.players;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.lofro.core.paper.data.LocationSerializer;
import us.jcedeno.game.Squid;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.global.interfaces.Restorable;
import us.jcedeno.game.players.enums.Role;
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
public class PlayerManager extends Restorable {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private volatile Map<String, SquidParticipant> participants;
    private volatile Stack<Integer> stack = new Stack<>();
    private volatile AtomicInteger currentId;

    protected Squid instance;

    public PlayerManager(final Squid instance) {
        this.instance = instance;
        // Use concurrent hashmap for thread safety.
        this.participants = new ConcurrentHashMap<>();
    }

    public PlayerManager() {
        this.participants = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        var pManager = new PlayerManager();

        pManager.addPlayer("JCEDENO");

        try {
            pManager.save(new JsonConfig("test.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(pManager.getP());

    }

    public String getP() {
        return gson.toJson(participants.values());
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        var jsonObject = jsonConfig.getJsonObject();
        var jsonArray = jsonObject.getAsJsonArray("participants");
        //Populate map
        var participantList = gson.fromJson(jsonArray, SquidParticipant.class);

    }

    @Override
    protected void save(JsonConfig jsonConfig) {
        var json = new JsonObject();

        json.addProperty("currentId", currentId);
        json.add("participants", gson.toJsonTree(participants.values()));
        
        jsonConfig.setJsonObject(json);

        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // TODO Cleanup code below.

    public SquidPlayer addPlayer(final String player) {
        // Check if player is already a participant.
        if (this.participants.containsKey(player))
            throw new IllegalArgumentException("Player is already a participant.");

        var squidPlayer = new SquidPlayer(player, (getPlayers() + 1));
        // Put player in map
        participants.put(player, squidPlayer);
        // Call Event
        // callEvent(new SquidPlayerAddedEvent(squidPlayer));
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

    public boolean isPlayer(Player player) {
        return getPlayer(player.getName()) != null;
    }

    public boolean isGuard(Player player) {
        return getGuard(player.getName()) != null;
    }

    public boolean isDead(Player player) {
        return getPlayer(player.getName()).isDead();
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
