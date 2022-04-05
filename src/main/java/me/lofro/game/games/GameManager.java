package me.lofro.game.games;

import com.google.common.collect.ImmutableList;
import me.lofro.game.games.backrooms.BackRoomsManager;
import me.lofro.game.games.backrooms.commands.BackRoomsCMD;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.games.commands.GameManagerCMD;
import me.lofro.game.games.deathnote.DeathNoteManager;
import me.lofro.game.games.deathnote.commands.DeathNoteCMD;
import me.lofro.game.games.deathnote.types.DeathNoteData;
import me.lofro.game.games.finalfight.FinalFightManager;
import me.lofro.game.games.finalfight.commands.FinalFightCMD;
import me.lofro.game.games.glass.GlassGameManager;
import me.lofro.game.games.glass.commands.GlassGameCMD;
import me.lofro.game.games.glass.types.GlassGameData;
import me.lofro.game.games.hideseek.HideSeekManager;
import me.lofro.game.games.hideseek.commands.HideSeekCMD;
import me.lofro.game.games.parkour.ParkourManager;
import me.lofro.game.games.parkour.commands.ParkourCMD;
import me.lofro.game.games.parkour.types.ParkourData;
import me.lofro.game.games.purge.PurgeManager;
import me.lofro.game.games.purge.commands.PurgeCMD;
import me.lofro.game.games.purge.types.PurgeData;
import me.lofro.game.games.tntTag.TNTManager;
import me.lofro.game.games.tntTag.commands.TNTCMD;
import org.bukkit.Bukkit;

import lombok.Getter;
import me.lofro.game.SquidGame;
import me.lofro.game.data.types.GameData;
import me.lofro.game.data.utils.JsonConfig;
import me.lofro.game.games.greenlight.GreenLightManager;
import me.lofro.game.games.greenlight.commands.GreenLightCMD;
import me.lofro.game.games.greenlight.types.GreenLightData;
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

    private GameData gData;

    private final @Getter GreenLightManager greenLightManager;
    private final @Getter HideSeekManager hideSeekManager;
    private final @Getter TNTManager tntManager;
    private final @Getter DeathNoteManager deathNoteManager;
    private final @Getter PurgeManager purgeManager;
    private final @Getter BackRoomsManager backRoomsManager;
    private final @Getter GlassGameManager glassGameManager;
    private final @Getter ParkourManager parkourManager;
    private final @Getter FinalFightManager finalFightManager;

    public GameManager(final SquidGame squidInstance) {
        this.squidInstance = squidInstance;
        // Restore data from dManager json files.
        this.restore(squidInstance.getDManager().gDataConfig());
        // Initialize the games.
        var baseWorld = Bukkit.getWorlds().get(0);

        this.greenLightManager = new GreenLightManager(this, baseWorld);
        this.hideSeekManager = new HideSeekManager(this);
        this.tntManager = new TNTManager(this);
        this.deathNoteManager = new DeathNoteManager(this, baseWorld);
        this.purgeManager = new PurgeManager(this, baseWorld);
        this.backRoomsManager = new BackRoomsManager(this, baseWorld);
        this.glassGameManager = new GlassGameManager(this, baseWorld);
        this.parkourManager = new ParkourManager(this);
        this.finalFightManager = new FinalFightManager(this);
        // Initialize the Timer.
        this.timer = new GameTimer();
        // Run the task.
        this.timer.runTaskTimerAsynchronously(squidInstance, 20L, 20L);
        // Register game commands.
        squidInstance.registerCommands(squidInstance.getCommandManager(),
                new GameManagerCMD(this),
                new GreenLightCMD(greenLightManager),
                new HideSeekCMD(hideSeekManager),
                new TNTCMD(tntManager),
                new DeathNoteCMD(deathNoteManager),
                new PurgeCMD(purgeManager),
                new BackRoomsCMD(backRoomsManager),
                new GlassGameCMD(glassGameManager),
                new ParkourCMD(parkourManager),
                new FinalFightCMD(finalFightManager)
                );

        // Sets the location command completion.
        SquidGame.getInstance().getCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.gData = new GameData(new GreenLightData(), new BackRoomsData(), new PurgeData(), new GlassGameData(), new DeathNoteData(), new ParkourData());
        } else {
            this.gData = SquidGame.gson().fromJson(jsonConfig.getJsonObject(), GameData.class);
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
     * @return the GameData object.
     */
    public GameData gameData() {
        return this.gData;
    }
}
