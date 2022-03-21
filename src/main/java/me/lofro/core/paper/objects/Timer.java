package me.lofro.core.paper.objects;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Timer {

    private final Main instance;

    private int startTime;
    private int seconds;

    private final @Getter BossBar bossBar;

    private @Setter @Getter boolean isActive = false;

    private String time = "";

    public Timer(Main instance, int currentTime) {
        this.instance = instance;
        this.seconds = 0;
        this.startTime = currentTime;
        this.bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "TIMER"), "", BarColor.WHITE, BarStyle.SOLID);
        this.bossBar.setVisible(false);
    }

    private static String timeConvert(int t) {
        int hours = t / 3600;

        int minutes = (t % 3600) / 60;
        int seconds = t % 60;

        return (hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds));
    }

    public int getTime(int currentTime) {
        return (startTime + seconds) - currentTime;
    }

    public void setPreStart(int time) {
        this.time = timeConvert(time);
        this.bossBar.setVisible(true);
        this.bossBar.setTitle(this.time);
    }

    public void refreshTime(int currentTime) {
        var time = (startTime + seconds) - currentTime;

        if (time < 0) {
            this.time = "00:00";
        } else {
            this.time = timeConvert(time);
            bossBar.setTitle(this.time);
        }
        if (time < -5) {
            end();
        }
    }

    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }

    public void addPlayers() {
        Bukkit.getOnlinePlayers().forEach(this.bossBar::addPlayer);
    }

    public void removePlayer(Player player) {
        this.bossBar.removePlayer(player);
    }

    public void removePlayers() {
        Bukkit.getOnlinePlayers().forEach(this.bossBar::removePlayer);
    }

    public void updateTime(int seconds) {
        this.time = timeConvert(seconds);
        this.seconds = seconds;
        this.startTime = (int) instance.getGame().getGameTime();
    }

    public void start(int seconds) {
        setPreStart(seconds);
        updateTime(seconds);
        this.isActive = true;
        this.bossBar.setVisible(true);
        addPlayers();
    }

    public void end() {
        this.bossBar.setVisible(false);
        this.isActive = false;
        removePlayers();
    }

}
