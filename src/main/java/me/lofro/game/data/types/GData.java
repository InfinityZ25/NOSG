package me.lofro.game.data.types;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.games.glass.types.GlassGameData;
import me.lofro.game.games.greenlight.types.GLightData;
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

    private @Getter @Setter PvPState pvPState;

    private @Getter @Setter Day day;

    public GData(GLightData greenLightData, BackRoomsData backRoomsData,GlassGameData glassGameData, PvPState pvPState, Day day) {
        this.greenLightData = greenLightData;
        this.backRoomsData = backRoomsData;
        this.glassGameData = glassGameData;
        this.pvPState = pvPState;
        this.day = day;
    }

    public GData(GLightData greenLightData, BackRoomsData backRoomsData, GlassGameData glassGameData) {
        this.greenLightData = greenLightData;
        this.backRoomsData = backRoomsData;
        this.glassGameData = glassGameData;
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

}
