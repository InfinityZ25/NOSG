package me.lofro.core.paper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.events.GameTickEvent;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.games.HideSeekGame;
import me.lofro.core.paper.objects.SquidGuard;
import me.lofro.core.paper.objects.SquidParticipant;
import me.lofro.core.paper.objects.SquidPlayer;
import me.lofro.core.paper.objects.Timer;
import me.lofro.core.paper.utils.date.Date;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Game extends BukkitRunnable {

    private final @Getter String name = Strings.format("&f&lSquid&d&lOtaku&f&lGame &7>> &r");

    private @Getter Day day;
    private @Getter @Setter LocalDate startDate;
    private @Getter @Setter String startDateString;

    private Main instance;

    private long gameTime = 0;
    private long startTime;

    private @Getter HashMap<String, Role> participantRoles = new HashMap<>();
    private @Getter HashMap<String, Boolean> participantDeadStates = new HashMap<>();
    private @Getter HashMap<String, Integer> playerIds = new HashMap<>();

    private @Getter HashMap<String, SquidPlayer> players = new HashMap<>();
    private @Getter HashMap<String, SquidGuard> guards = new HashMap<>();
    private @Getter HashMap<String, SquidParticipant> participants = new HashMap<>();

    private @Getter @Setter GreenLightGame greenLightGame;
    private @Getter @Setter HideSeekGame hideSeekGame;

    private Timer timer;

    private @Getter @Setter PvPState pvPState = PvPState.GUARDS;

    public Game(Main instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        this.timer = new Timer(instance, (int) gameTime);

        this.greenLightGame = new GreenLightGame(instance, Bukkit.getWorld("world"), this);
        this.hideSeekGame = new HideSeekGame(instance, this);
    }

    public JsonObject getParticipantJObject() {
        JsonObject jsonObject = new JsonObject();

        JsonArray players = new JsonArray();
        JsonArray guards = new JsonArray();
        for (Map.Entry<String, Role> entry : participantRoles.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue().equals(Role.PLAYER)) {
                JsonObject o = new JsonObject();
                o.add("NAME", new JsonPrimitive(key));
                o.add("ID", new JsonPrimitive((playerIds.get(key) != null) ? playerIds.get(key) : playerIds.size()+1));
                o.add("DEAD", new JsonPrimitive(participantDeadStates.get(key)));

                players.add(o);
            }
            if (entry.getValue().equals(Role.GUARD)) {
                JsonObject o = new JsonObject();
                o.add("NAME", new JsonPrimitive(key));
                o.add("DEAD", new JsonPrimitive(participantDeadStates.get(key)));
                guards.add(o);
            }
        }

        jsonObject.add("PLAYERS", players);
        jsonObject.add("GUARDS", guards);

        return jsonObject;
    }

    // TODO REFACTOR.
    public JsonObject getGameDataJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("START_DATE", new JsonPrimitive((startDateString != null) ? startDateString : Date.getDateForDayZero()));

        JsonArray greenLightCubeLocation1Array = new JsonArray();
        greenLightCubeLocation1Array.add((greenLightGame.getCubeLocation1() != null) ? greenLightGame.getCubeLocation1().getX() : -20);
        greenLightCubeLocation1Array.add((greenLightGame.getCubeLocation1() != null) ? greenLightGame.getCubeLocation1().getY() : -29);
        greenLightCubeLocation1Array.add((greenLightGame.getCubeLocation1() != null) ? greenLightGame.getCubeLocation1().getZ() : -35);

        JsonArray greenLightCubeLocation2Array = new JsonArray();
        greenLightCubeLocation2Array.add((greenLightGame.getCubeLocation2() != null) ? greenLightGame.getCubeLocation2().getX() : -146);
        greenLightCubeLocation2Array.add((greenLightGame.getCubeLocation2() != null) ? greenLightGame.getCubeLocation2().getY() : 3);
        greenLightCubeLocation2Array.add((greenLightGame.getCubeLocation2() != null) ? greenLightGame.getCubeLocation2().getZ() : 18);

        jsonObject.add("GREEN_LIGHT_LOCATION1", greenLightCubeLocation1Array);
        jsonObject.add("GREEN_LIGHT_LOCATION2", greenLightCubeLocation2Array);

        return jsonObject;
    }

    public void loadDay() {
        LocalDate nowDate = LocalDate.now();
        int day = (startDate != null) ? (int) ChronoUnit.DAYS.between(startDate, nowDate) : 0;

        switch (day) {
            case 0 -> this.day = Day.FIRST;
            case 1 -> this.day = Day.SECOND;
            default -> this.day = Day.LAST;
        }
    }

    public void setDay(Day day) {
        this.day = day;

        LocalDate add;
        switch (day) {
            case SECOND -> add = LocalDate.now().minusDays(1);
            case LAST -> add = LocalDate.now().minusDays(2);
            default -> add = LocalDate.now();
        }
        int month = add.getMonthValue();
        int dayOfMonth = add.getDayOfMonth();

        String s;

        if (month < 10) {
            s = add.getYear() + "-0" + month + "-";
        } else {
            s = add.getYear() + "-" + month + "-";
        }

        if (dayOfMonth < 10) {
            s = s + "0" + dayOfMonth;
        } else {
            s = s + dayOfMonth;
        }
        startDateString = s;
        instance.saveGameData();
    }

    public void loadParticipants() {
        setDefaultRoles();
        if (Bukkit.getOnlinePlayers().size() < 1) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();

            boolean dead = this.participantDeadStates.get(name);
            Role role = this.participantRoles.get(name);

            switch(role) {
                case PLAYER -> {

                    if (!dead) player.setGameMode(GameMode.ADVENTURE);

                    if (this.players.containsKey(name)) return;

                    SquidParticipant squidParticipant = new SquidParticipant(name, Role.PLAYER, dead, player, instance);
                    SquidPlayer squidPlayer = new SquidPlayer(name, (this.playerIds.get(name) != null) ? this.playerIds.get(name) : this.playerIds.size()+1, dead, player, instance);

                    this.guards.remove(name);
                    this.players.put(name,squidPlayer);
                    this.participants.put(name, squidParticipant);
                }
                case GUARD -> {

                    if (this.guards.containsKey(name)) return;

                    SquidParticipant squidParticipant = new SquidParticipant(name, Role.GUARD, dead, player, instance);
                    SquidGuard squidGuard = new SquidGuard(name, dead, player, instance);

                    this.players.remove(name);
                    this.guards.put(name, squidGuard);
                    this.participants.put(name, squidParticipant);
                }
            }
        }
    }

    public void loadParticipant(Player player) {
        setDefaultRole(player);
        String name = player.getName();

        boolean dead = this.participantDeadStates.get(name);
        Role role = this.participantRoles.get(name);

        switch(role) {
            case PLAYER -> {

                if (!dead) player.setGameMode(GameMode.ADVENTURE);

                if (this.players.containsKey(name)) return;

                SquidParticipant squidParticipant = new SquidParticipant(name, Role.PLAYER, dead, player, instance);
                SquidPlayer squidPlayer = new SquidPlayer(name, (this.playerIds.get(name) != null) ? this.playerIds.get(name) : this.playerIds.size()+1, dead, player, instance);

                this.guards.remove(name);
                this.players.put(name,squidPlayer);
                this.participants.put(name, squidParticipant);
            }
            case GUARD -> {

                if (this.guards.containsKey(name)) return;

                SquidParticipant squidParticipant = new SquidParticipant(name, Role.GUARD, dead, player, instance);
                SquidGuard squidGuard = new SquidGuard(name, dead, player, instance);

                this.players.remove(name);
                this.guards.put(name, squidGuard);
                this.participants.put(name, squidParticipant);
            }
        }
    }

    public void setDefaultRoles() {
        if (Bukkit.getOnlinePlayers().size() < 1) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();

            if (this.participants.containsKey(name)) return;

            if (player.isOp()) {
                SquidParticipant squidParticipant = new SquidParticipant(name, Role.GUARD, false, player, instance);
                SquidGuard squidGuard = new SquidGuard(name, false, player, instance);

                guards.put(name, squidGuard);
                participants.put(name, squidParticipant);

                player.setGameMode(GameMode.CREATIVE);
            } else {
                SquidParticipant squidParticipant = new SquidParticipant(name,Role.PLAYER, false, player, instance);
                SquidPlayer squidPlayer = new SquidPlayer(name, (this.playerIds.get(name) != null) ? this.playerIds.get(name) : this.playerIds.size()+1, false, player, instance);

                players.put(name, squidPlayer);
                participants.put(name, squidParticipant);

                player.setGameMode(GameMode.ADVENTURE);
            }
        }
        instance.saveParticipantData();
    }

    public void setDefaultRole(Player player) {
        String name = player.getName();

        if (this.participants.containsKey(name)) return;

        if (player.isOp()) {
            SquidParticipant squidParticipant = new SquidParticipant(name, Role.GUARD, false, player, instance);
            SquidGuard squidGuard = new SquidGuard(name, false, player, instance);

            guards.put(name, squidGuard);
            participants.put(name, squidParticipant);
            instance.saveParticipantData();

            player.setGameMode(GameMode.CREATIVE);

            player.sendMessage(Strings.format(this.name + "&bTu rol ha sido asignado automáticamente a &3GUARDIA&b debido a que tienes permisos de administrador."));
        } else {
            SquidParticipant squidParticipant = new SquidParticipant(name,Role.PLAYER, false, player, instance);
            SquidPlayer squidPlayer = new SquidPlayer(name, (this.playerIds.get(name) != null) ? this.playerIds.get(name) : this.playerIds.size()+1, false, player, instance);

            players.put(name, squidPlayer);
            participants.put(name, squidParticipant);
            instance.saveParticipantData();

            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    public void playSoundDistance(Location loc, Integer distance, String sound, Float volume, Float pitch) {
        loc.getNearbyPlayers(distance).forEach(player -> player.playSound(loc, sound, volume, pitch));
    }

    public boolean isPlayer(Player player) {
        return this.participantRoles.get(player.getName()) == Role.PLAYER;
    }

    public boolean isGuard(Player player) {
        return this.participantRoles.get(player.getName()) == Role.GUARD;
    }

    public boolean isDead(Player player) {return this.participantDeadStates.get(player.getName());}

    @Override
    public void run() {
        var newTime = (int) (Math.floor((System.currentTimeMillis() - startTime) / 1000.0));
        gameTime = newTime;

        Bukkit.getPluginManager().callEvent(new GameTickEvent(newTime, true));
    }

    public enum Role {
        PLAYER, GUARD
    }

    public enum Day {
        FIRST, SECOND, LAST
    }

    public enum PvPState {
        ALL, NONE, GUARDS
    }
}