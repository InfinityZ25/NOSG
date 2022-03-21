package me.lofro.core.paper.data;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.utils.JsonConfig;

public class GameData {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void parse(JsonConfig jsonConfig, Game game){
        

    }
















    public String obtainGG() {

        var map = new HashMap<>();
        map.put("test", startDate);
        return gson.toJsonTree(map).toString();
    }

    public static void main(String[] args) {
        System.out.println(new GameData("test").obtainGG());
    }

    public GameData(final String filename, final String path) {
        try {
            configFile = new JsonConfig(filename, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(final Game game) {
        this.startDate = game.getStartDateString();

        this.greenLightData = new GreenLightData(Position.fromLoc(game.getGreenLightGame().getCubeLocation1()),
                Position.fromLoc(game.getGreenLightGame().getCubeLocation2()));

        this.turretPositions = game.getGreenLightGame().getTurretLocations().stream().map(Position::fromLoc).toList();

        gson.toJsonTree(this);

    }

    public void restore(final Game game) {

        game.getGreenLightGame().setCubeLocation1(greenLightData.cubeLower.toLoc());
        game.getGreenLightGame().setCubeLocation2(greenLightData.cubeUpper.toLoc());
    }

    public void load() {
    }

    public static class GreenLightData {
        Position cubeUpper, cubeLower;

        public GreenLightData(Position cubeUpper, Position cubeLower) {
            this.cubeUpper = cubeUpper;
            this.cubeLower = cubeLower;
        }
    }

    public static class Position {
        double x, y, z;

        public Position(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Position fromLoc(Location loc) {
            return new Position(loc.getX(), loc.getY(), loc.getZ());
        }

        public Location toLoc() {
            return new Location(Bukkit.getWorlds().get(0), x, y, z);
        }
    }

}
