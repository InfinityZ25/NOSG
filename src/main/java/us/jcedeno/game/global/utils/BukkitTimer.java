package us.jcedeno.game.global.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;

public class BukkitTimer extends Timer {
    private final BossBar bossBar;

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

        System.out.println(bossBar.name());

        return tick;
    }

    /**
     * sample function to create a bossbar
     */
    private static BossBar createBar(String text) {
        return BossBar.bossBar(Component.text(text), 1, Color.WHITE, Overlay.PROGRESS);

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

    public static void main(String[] args) {
        var nTimer = BukkitTimer.bTimer(10).start();

        while (!nTimer.isDone()) {

        }
    }

}
