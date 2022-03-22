package me.lofro.core.paper.data;

import java.util.List;

import com.google.gson.Gson;

import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.objects.SquidGuard;
import me.lofro.core.paper.objects.SquidPlayer;
import me.lofro.core.paper.utils.JsonConfig;

public class PData {
    List<SquidPlayer> players;
    List<SquidGuard> guards;

    public PData(List<SquidPlayer> players, List<SquidGuard> guards) {
        this.players = players;
        this.guards = guards;
    }

    public PData fromJson(final JsonConfig config) {
        return Main.getGson().fromJson(config.getJsonObject(), PData.class);
    }

    public void backupData(final Game game) {
        final var jsonFile = game.getInstance().getParticipantData();

        jsonFile.setJsonObject(Main.getGson().toJsonTree(this).getAsJsonObject());

        try {
            jsonFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
