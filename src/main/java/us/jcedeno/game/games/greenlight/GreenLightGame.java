package us.jcedeno.game.games.greenlight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.lofro.core.paper.utils.strings.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.jcedeno.game.games.GameManager;
import us.jcedeno.game.games.greenlight.enums.LightState;
import us.jcedeno.game.games.greenlight.utils.tasks.PlayerArrayQueueShootTask;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Data
@EqualsAndHashCode(callSuper = false)
public class GreenLightGame extends BukkitRunnable {

    private GameManager gManager;
    private GreenLightManager gLightManager;

    private int lowestTimeBound;
    private int highestTimeBound;

    private int timeBetween;

    public GreenLightGame(GreenLightManager gLightManager) {
        this.gLightManager = gLightManager;
        this.gManager = gLightManager.getGManager();
        this.lowestTimeBound = gLightManager.getLowestTimeBound();
        this.highestTimeBound = gLightManager.getHighestTimeBound();
        this.timeBetween = ThreadLocalRandom.current().nextInt(lowestTimeBound, highestTimeBound);
    }

    @Override
    public void run() {
        if (timeBetween == 0) {
            greenLight(!gLightManager.getLightState().equals(LightState.GREEN_LIGHT));
        }

        if (gLightManager.getGreenLightListener().getMovedList().size() > 0) {
            new PlayerArrayQueueShootTask(gLightManager, gLightManager.getGreenLightListener().getMovedList(), 10, 20);
            gLightManager.getGreenLightListener().getMovedList().clear();
        }

        timeBetween--;
    }

    /**
     *
     * Function that updates the light state of the game. Makes the logic run.
     *
     * @param bool defines whether the light is green or red.
     */
    public void greenLight(Boolean bool) {
        if (bool) {
            greenLightTitle("&a¡LUZ VERDE!", "&aPuedes comenzar a moverte.");

            this.timeBetween = ThreadLocalRandom.current().nextInt(lowestTimeBound, highestTimeBound);
            gLightManager.setLightState(LightState.GREEN_LIGHT);
            gManager.getSquidInstance().unregisterListener(gLightManager.getGreenLightListener());
        } else {
            greenLightTitle("&c¡LUZ ROJA!", "&cNo muevas ni un pelo.");

            Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), () -> {
                this.timeBetween = ThreadLocalRandom.current().nextInt(lowestTimeBound, highestTimeBound);
                gLightManager.setLightState(LightState.RED_LIGHT);
                gManager.getSquidInstance().registerListener(gLightManager.getGreenLightListener());
            }, 15);
        }
    }

    private void greenLightTitle(String title, String subTitle) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.showTitle(Title.title(Component.text(Strings.format(title)),
                    Component.text(Strings.format(subTitle)),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(3))));
            p.playSound(p.getLocation(), "sfx.bell", 1, 1);
        });
    }

}
