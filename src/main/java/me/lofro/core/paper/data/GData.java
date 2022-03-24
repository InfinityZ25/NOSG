package me.lofro.core.paper.data;

import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Location;

import me.lofro.core.paper.Game;
import me.lofro.core.paper.utils.JsonConfig;
import us.jcedeno.game.data.LocationSerializer;

public class GData {

    private Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer()).setPrettyPrinting()
            .serializeNulls()
            .create();

    /**
     * InnerGData
     */
    public static class InnerGData {
        String date;
        GreenLightData greenLightData;
        int time = 10;
        double health = 20f;

        public InnerGData(String date, GreenLightData greenLightData) {
            this.date = date;
            this.greenLightData = greenLightData;
        }
    }

    /**
     * GreenLightData
     */
    public static class GreenLightData {
        Location cubeUpperLocation, cuberLowerLocation;
        List<Location> turretLocations;

        public GreenLightData(Location cubeUpperLocation, Location cuberLowerLocation, List<Location> turretLocations) {
            this.cubeUpperLocation = cubeUpperLocation;
            this.cuberLowerLocation = cuberLowerLocation;
            this.turretLocations = turretLocations;
        }
    }

    public void backupData(final Game game) {

        final var greenL = game.getGreenLightGame();

        var gLData = new GreenLightData(greenL.getCubeLocation1(), greenL.getCubeLocation2(),
                greenL.getTurrets().getTurretLocations());

        game.getInstance().getGameData()
                .setJsonObject(gson.toJsonTree(new InnerGData(LocalDate.now().toString(), gLData)).getAsJsonObject());

        try {
            game.getInstance().getGameData().save();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public InnerGData loadData(final JsonConfig config) {

        var restoredFile = gson.fromJson(config.getJsonObject(), InnerGData.class);
        System.out.println("Inner: " + gson.toJson(restoredFile));

        return restoredFile;

    }

}
