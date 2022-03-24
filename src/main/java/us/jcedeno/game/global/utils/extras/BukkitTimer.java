package us.jcedeno.game.global.utils.extras;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;

public class BukkitTimer extends JTimer {
    private @Getter final BossBar bossBar;
    private List<Audience> audience = new ArrayList<>();

    public BukkitTimer(final int time, final BossBar bossbar) {
        super(time);
        this.bossBar = bossbar;
    }

    public BukkitTimer(final int time) {
        super(time);
        this.bossBar = BossBar.bossBar(getTime(time), 1, Color.WHITE, Overlay.PROGRESS);
    }

    @Override
    protected int tick() {
        var tick = super.tick();
        // Update bar's name
        bossBar.name(getTime(tick));
        bossBar.progress(progress());

        return tick;
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        // Remove bar from players
        audience.forEach(a -> a.hideBossBar(bossBar));
    }

    public void addViewer(Audience player) {
        if (!audience.contains(player)) {
            audience.add(player);
            player.showBossBar(bossBar);
        }
    }

    public void removeViewer(Audience player) {
        if (audience.contains(player)) {
            audience.remove(player);
            player.hideBossBar(bossBar);
        }
    }

    private static Component getTime(final int time) {
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
