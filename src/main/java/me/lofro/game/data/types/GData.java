package me.lofro.game.data.types;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.games.deathnote.types.DeathNoteData;
import me.lofro.game.games.glass.types.GlassGameData;
import me.lofro.game.games.greenlight.types.GLightData;
import me.lofro.game.games.purge.types.PurgeData;
import me.lofro.game.global.enums.Day;
import me.lofro.game.global.enums.PvPState;

/**
 * A class designed to hold all the stateful data for all mini-games.
 *
 */
public class GData {

    private final GLightData greenLightData;
    private final BackRoomsData backRoomsData;
    private final GlassGameData glassGameData;
    private final DeathNoteData deathNoteData;
    private final PurgeData purgeData;

    private @Getter @Setter PvPState pvPState;

    private @Getter @Setter Day day;

    public GData(GLightData greenLightData, BackRoomsData backRoomsData, PurgeData purgeData, GlassGameData glassGameData, DeathNoteData deathNoteData, PvPState pvPState, Day day) {
        this.greenLightData = greenLightData;
        this.backRoomsData = backRoomsData;
        this.purgeData = purgeData;
        this.glassGameData = glassGameData;
        this.deathNoteData = deathNoteData;
        this.pvPState = pvPState;
        this.day = day;
    }

    public GData(GLightData greenLightData, BackRoomsData backRoomsData, PurgeData purgeData, GlassGameData glassGameData, DeathNoteData deathNoteData) {
        this.greenLightData = greenLightData;
        this.backRoomsData = backRoomsData;
        this.purgeData = purgeData;
        this.glassGameData = glassGameData;
        this.deathNoteData = deathNoteData;
        this.pvPState = PvPState.ONLY_GUARDS;
        this.day = Day.FIRST;
    }

    /**
     * @return the greenLightData
     */
    public GLightData greenLightData() {
        return greenLightData;
    }

    /**
     * @return the backRoomsData
     */
    public BackRoomsData backRoomsData() {
        return backRoomsData;
    }

    /**
     * @return the glassGameData
     */
    public GlassGameData glassGameData() {
        return glassGameData;
    }

    /**
     * @return the deathNoteData
     */
    public DeathNoteData deathNoteData() {
        return deathNoteData;
    }

    /**
     * @return the deathNoteData
     */
    public PurgeData purgeData() {
        return purgeData;
    }

}
