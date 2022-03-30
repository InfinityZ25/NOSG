package me.lofro.game.games;

import com.google.common.collect.ImmutableList;
import me.lofro.game.games.backrooms.BackRoomsManager;
import me.lofro.game.games.backrooms.commands.BackRoomsCMD;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.games.commands.GameManagerCMD;
import me.lofro.game.games.glass.GlassGameManager;
import me.lofro.game.games.glass.commands.GlassGameCMD;
import me.lofro.game.games.glass.types.GlassGameData;
import me.lofro.game.games.hideseek.HideSeekManager;
import me.lofro.game.games.hideseek.commands.HideSeekCMD;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;
import me.lofro.game.SquidGame;
import me.lofro.game.data.types.GData;
import me.lofro.game.data.utils.JsonConfig;
import me.lofro.game.games.greenlight.GreenLightManager;
import me.lofro.game.games.greenlight.commands.GreenLightCMD;
import me.lofro.game.games.greenlight.types.GLightData;
import me.lofro.game.global.interfaces.Restorable;
import me.lofro.game.games.greenlight.utils.timer.GameTimer;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 *
 */
public class GameManager extends Restorable<SquidGame> {

    private final @Getter SquidGame squidInstance;

    private final @Getter GameTimer timer;

    private GData gData;

    private @Getter final GreenLightManager greenLightManager;
    private @Getter final HideSeekManager hideSeekManager;
    private @Getter final BackRoomsManager backRoomsManager;
    private @Getter final GlassGameManager glassGameManager;

    public GameManager(final SquidGame squidInstance) {
        this.squidInstance = squidInstance;
        // restore data from dManager json files.
        this.restore(squidInstance.getDManager().gDataConfig());
        // initialize the games.
        this.greenLightManager = new GreenLightManager(this, Bukkit.getWorlds().get(0));
        this.hideSeekManager = new HideSeekManager(this);
        this.backRoomsManager = new BackRoomsManager(this, Bukkit.getWorlds().get(0));
        this.glassGameManager = new GlassGameManager(this, Bukkit.getWorlds().get(0));
        // initialize the Timer.
        this.timer = new GameTimer();
        // Run the task
        this.timer.runTaskTimerAsynchronously(squidInstance, 20L, 20L);
        // register game commands.
        squidInstance.registerCommands(squidInstance.getCommandManager(),
                new GameManagerCMD(this),
                new GreenLightCMD(greenLightManager),
                new HideSeekCMD(hideSeekManager),
                new BackRoomsCMD(backRoomsManager),
                new GlassGameCMD(glassGameManager)
                );

        SquidGame.getInstance().getCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.gData = new GData(new GLightData(), new BackRoomsData(), new GlassGameData());
        } else {
            this.gData = SquidGame.gson().fromJson(jsonConfig.getJsonObject(), GData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(SquidGame.gson().toJsonTree(gData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the gData object.
     */
    public GData gData() {
        return this.gData;
    }
}
