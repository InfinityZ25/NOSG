package me.lofro.game.games;

import com.google.common.collect.ImmutableList;
import me.lofro.game.games.backrooms.BackRoomsManager;
import me.lofro.game.games.backrooms.commands.BackRoomsCMD;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.games.commands.GameManagerCMD;
import me.lofro.game.games.deathnote.DeathNoteManager;
import me.lofro.game.games.deathnote.commands.DeathNoteCMD;
import me.lofro.game.games.deathnote.types.DeathNoteData;
import me.lofro.game.games.glass.GlassGameManager;
import me.lofro.game.games.glass.commands.GlassGameCMD;
import me.lofro.game.games.glass.types.GlassGameData;
import me.lofro.game.games.hideseek.HideSeekManager;
import me.lofro.game.games.hideseek.commands.HideSeekCMD;
import me.lofro.game.games.purge.PurgeManager;
import me.lofro.game.games.purge.commands.PurgeCMD;
import me.lofro.game.games.purge.types.PurgeData;
import me.lofro.game.games.tntTag.TNTManager;
import me.lofro.game.games.tntTag.commands.TNTCMD;
import org.bukkit.Bukkit;

import lombok.Getter;
import me.lofro.game.SquidGame;
import me.lofro.game.data.types.GData;
import me.lofro.game.data.utils.JsonConfig;
import me.lofro.game.games.greenlight.GreenLightManager;
import me.lofro.game.games.greenlight.commands.GreenLightCMD;
import me.lofro.game.games.greenlight.types.GLightData;
import me.lofro.game.global.interfaces.Restorable;
import me.lofro.game.global.utils.timer.GameTimer;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 *
 */
public class GameManager extends Restorable<SquidGame> {

    private final @Getter SquidGame squidInstance;

    private final @Getter GameTimer timer;

    private GData gData;

    private final @Getter GreenLightManager greenLightManager;
    private final @Getter HideSeekManager hideSeekManager;
    private final @Getter  BackRoomsManager backRoomsManager;
    private final @Getter PurgeManager purgeManager;
    private final @Getter GlassGameManager glassGameManager;
    private final @Getter DeathNoteManager deathNoteManager;
    private final @Getter TNTManager tntManager;

    public GameManager(final SquidGame squidInstance) {
        this.squidInstance = squidInstance;
        // restore data from dManager json files.
        this.restore(squidInstance.getDManager().gDataConfig());
        // initialize the games.
        var baseWorld = Bukkit.getWorlds().get(0);

        this.greenLightManager = new GreenLightManager(this, baseWorld);
        this.hideSeekManager = new HideSeekManager(this);
        this.backRoomsManager = new BackRoomsManager(this, baseWorld);
        this.purgeManager = new PurgeManager(this, baseWorld);
        this.glassGameManager = new GlassGameManager(this, baseWorld);
        this.deathNoteManager = new DeathNoteManager(this, baseWorld);
        this.tntManager = new TNTManager(this);
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
                new GlassGameCMD(glassGameManager),
                new DeathNoteCMD(deathNoteManager),
                new PurgeCMD(purgeManager),
                new TNTCMD(tntManager)
                );

        SquidGame.getInstance().getCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.gData = new GData(new GLightData(), new BackRoomsData(), new PurgeData(), new GlassGameData(), new DeathNoteData());
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
