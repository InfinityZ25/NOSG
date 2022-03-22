package me.lofro.core.paper;

import java.time.LocalDate;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.events.GameTickEvent;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.games.HideSeekGame;
import me.lofro.core.paper.objects.SquidGuard;
import me.lofro.core.paper.objects.SquidParticipant;
import me.lofro.core.paper.objects.SquidPlayer;
import me.lofro.core.paper.objects.Timer;
import me.lofro.core.paper.utils.strings.Strings;

public class Game2 extends BukkitRunnable {

    private final @Getter String name = Strings.format("&f&lSquid&d&lOtaku&f&lGame &7>> &r");

    private @Getter Day day;
    private @Getter @Setter LocalDate startDate;
    private @Getter @Setter String startDateString;

    private Main instance;

    private long gameTime = 0;
    private long startTime;

    private @Getter HashMap<String, SquidPlayer> players = new HashMap<>();
    private @Getter HashMap<String, SquidGuard> guards = new HashMap<>();
    private @Getter HashMap<String, SquidParticipant> participants = new HashMap<>();

    private @Getter @Setter GreenLightGame greenLightGame;
    private @Getter @Setter HideSeekGame hideSeekGame;

    private Timer timer;

    private @Getter @Setter PvPState pvPState = PvPState.GUARDS;

    public Game2(Main instance) {
        this.instance = instance;
        this.startTime = System.currentTimeMillis();

        this.timer = new Timer(instance, (int) gameTime);

        this.greenLightGame = new GreenLightGame(instance, Bukkit.getWorld("world"), this);
        this.hideSeekGame = new HideSeekGame(instance, this);
    }

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
