package us.jcedeno.game.global.utils.extras;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;

public class BukkitTimer extends JTimer {
    private @Getter final BossBar bossBar;
    private final List<Audience> audience = new ArrayList<>();
    private @Getter boolean active = false;

    public BukkitTimer(final int time, final BossBar bossbar) {
        super(time);
        this.bossBar = bossbar;
    }

    public BukkitTimer(final int time) {
        super(time);
        this.bossBar = BossBar.bossBar(formatTime(time), 1, Color.WHITE, Overlay.PROGRESS);
    }

    @Override
    public CompletableFuture<JTimer> start() {
        if (active)
            throw new IllegalStateException("Timer is already running");
        this.active = true;
        bossBar.name(formatTime(time()));
        addAllViewers();
        return super.start();
    }

    @Override
    protected int tick() {
        // Progress the bar before the time is updated.
        bossBar.progress(progress());
        var tick = super.tick();
        // Update bar's name.
        bossBar.name(formatTime(tick));

        return tick;
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        // Remove bar from players
        audience.forEach(a -> a.hideBossBar(bossBar));
        this.active = false;
    }

    public void addViewer(Audience player) {
        if (!audience.contains(player)) {
            audience.add(player);
            player.showBossBar(bossBar);
        }
    }

    public void addAllViewers() {
        Bukkit.getOnlinePlayers().forEach(this::addViewer);
    }

    public void removeViewer(Audience player) {
        if (audience.contains(player)) {
            audience.remove(player);
            player.hideBossBar(bossBar);
        }
    }

    public void removeViewers() {
        Bukkit.getOnlinePlayers().forEach(this::removeViewer);
    }

    private static Component formatTime(final int time) {
        return Component.text(timeConvert(time));
    }

    private static String timeConvert(int t) {
        int hours = t / 3600;

        int minutes = (t % 3600) / 60;
        int seconds = t % 60;

        return (hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds));
    }
}
