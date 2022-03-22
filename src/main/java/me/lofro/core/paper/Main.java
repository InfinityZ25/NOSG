package me.lofro.core.paper;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.lofro.core.paper.commands.GameCMD;
import me.lofro.core.paper.commands.GreenLightCMD;
import me.lofro.core.paper.commands.HideSeekCMD;
import me.lofro.core.paper.commands.SquidCMD;
import me.lofro.core.paper.commands.TurretTestCMD;
import me.lofro.core.paper.listeners.GlobalListener;
import me.lofro.core.paper.objects.SquidParticipant;
import me.lofro.core.paper.utils.JsonConfig;
import me.lofro.core.paper.utils.NegativeSpaces;
import me.lofro.core.paper.utils.TCT.BukkitTCT;
import me.lofro.core.paper.utils.date.Date;
import me.lofro.core.paper.utils.location.Locations;
import me.lofro.core.paper.utils.rapidinv.RapidInvManager;
import me.lofro.core.paper.utils.strings.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Main extends JavaPlugin {

    private static @Getter Main instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;
    private final @Getter static MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonConfig participantData;
    private @Getter JsonConfig gameData;

    {
        try {
            this.gameData = new JsonConfig("gameData.json",
                    System.getProperty("user.dir") + File.separatorChar + Bukkit.getPluginsFolder() + File.separatorChar
                            + this.getName());
            this.participantData = new JsonConfig("participantData.json",
                    System.getProperty("user.dir") + File.separatorChar + Bukkit.getPluginsFolder() + File.separatorChar
                            + this.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEnable() {
        instance = this;

        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        game.getTimer().addPlayers();

        registerListeners(
                new GlobalListener(this));

        commandManager = new PaperCommandManager(this);

        registerCommands(commandManager,
                new SquidCMD(this),
                new GameCMD(this),
                new GreenLightCMD(this),
                new HideSeekCMD(this),
                new TurretTestCMD(game.getGreenLightGame().getTurrets()));

        loadData();

        Bukkit.getLogger().info(Strings.format(game.getName() + "&aEl plugin ha sido iniciado correctamente."));

    }

    @Override
    public void onDisable() {
        saveData();

        game.getTimer().removePlayers();

        Bukkit.getLogger().info(Strings.format(game.getName() + "&aEl plugin ha sido desactivado correctamente."));
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public void unregisterListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void registerCommands(PaperCommandManager manager, BaseCommand... commandExecutors) {
        for (BaseCommand commandExecutor : commandExecutors) {
            manager.registerCommand(commandExecutor);
        }
    }

    public void saveData() {
        saveParticipantData();
        saveGameData();
    }

    public void saveParticipantData() {
        String participantDataJson = gson.toJson(game.getParticipantJObject());
        JsonObject participantDataJsonObject = gson.fromJson(participantDataJson, JsonObject.class);

        this.participantData.setJsonObject(participantDataJsonObject);
        try {
            this.participantData.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGameData() {
        String gameDataJson = gson.toJson(game.getGameDataJsonObject());
        JsonObject gameDataJsonObject = gson.fromJson(gameDataJson, JsonObject.class);

        this.gameData.setJsonObject(gameDataJsonObject);
        try {
            this.gameData.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        loadParticipantData();
        loadGameData();

        game.loadParticipants();
        game.loadDay();

        game.getGreenLightGame().loadGameCube();
    }

    public void loadParticipantData() {
        try {
            JsonObject dataJsonObject = this.participantData.getJsonObject();

            if (dataJsonObject == null)
                return;

            JsonArray playerJsonArray = dataJsonObject.getAsJsonArray("PLAYERS");
            JsonArray guardJsonArray = dataJsonObject.getAsJsonArray("GUARDS");

            if (playerJsonArray != null) {
                for (int i = 0; i < playerJsonArray.size(); i++) {
                    JsonObject playerJsonArrayObject = (JsonObject) playerJsonArray.get(i);

                    JsonElement element = playerJsonArrayObject.get("NAME");
                    String name = element.getAsString();

                    game.getParticipantRoles().put(name, Game.Role.PLAYER);

                    int id;
                    boolean dead;

                    for (Map.Entry<String, JsonElement> entry : playerJsonArrayObject.entrySet()) {
                        String entryKey = entry.getKey();
                        JsonElement entryValue = entry.getValue();

                        switch (entryKey) {
                            case "ID" -> {
                                id = entryValue.getAsInt();
                                game.getPlayerIds().put(name, id);
                            }
                            case "DEAD" -> {
                                dead = entryValue.getAsBoolean();
                                game.getParticipantDeadStates().put(name, dead);

                                SquidParticipant squidParticipant = (Bukkit.getPlayer(name) != null)
                                        ? new SquidParticipant(name, Game.Role.PLAYER, dead,
                                                Objects.requireNonNull(Bukkit.getPlayer(name)), instance)
                                        : new SquidParticipant(name, Game.Role.PLAYER, dead, instance);
                                game.getParticipants().put(name, squidParticipant);
                            }
                        }
                    }
                }
            }

            if (guardJsonArray != null) {
                for (int i = 0; i < guardJsonArray.size(); i++) {
                    JsonObject guardJsonArrayObject = (JsonObject) guardJsonArray.get(i);

                    JsonElement element = guardJsonArrayObject.get("NAME");
                    String name = element.getAsString();

                    game.getParticipantRoles().put(name, Game.Role.GUARD);

                    for (Map.Entry<String, JsonElement> entry : guardJsonArrayObject.entrySet()) {
                        String entryKey = entry.getKey();
                        JsonElement entryValue = entry.getValue();

                        boolean dead;

                        if ("DEAD".equals(entryKey)) {
                            dead = entryValue.getAsBoolean();
                            game.getParticipantDeadStates().put(name, dead);

                            SquidParticipant squidParticipant = (Bukkit.getPlayer(name) != null)
                                    ? new SquidParticipant(name, Game.Role.GUARD, dead,
                                            Objects.requireNonNull(Bukkit.getPlayer(name)), instance)
                                    : new SquidParticipant(name, Game.Role.GUARD, dead, instance);
                            game.getParticipants().put(name, squidParticipant);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGameData() {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        });

        loadDateData();

        loadGreenLightData();
    }

    public void loadDateData() {
        JsonObject gameDataJsonObject = this.gameData.getJsonObject();

        if (gameDataJsonObject == null)
            return;

        String date = (gameDataJsonObject.get("START_DATE") != null)
                ? gameDataJsonObject.get("START_DATE").getAsString()
                : Date.getDateForDayZero();

        game.setStartDate(LocalDate.parse(date));
        game.setStartDateString(date);

    }

    public void loadGreenLightData() {

        // TODO HACER CON ARRAYS + REFACTOR PERRO.

        JsonObject gameDataJsonObject = this.gameData.getJsonObject();

        if (gameDataJsonObject == null)
            return;

        JsonArray greenLightLoc1Array = gameDataJsonObject.getAsJsonArray("GREEN_LIGHT_LOCATION1");
        JsonArray greenLightLoc2Array = gameDataJsonObject.getAsJsonArray("GREEN_LIGHT_LOCATION2");

        if (greenLightLoc1Array == null || greenLightLoc2Array == null)
            return;

        JsonElement firstIndex1 = greenLightLoc1Array.get(0);
        JsonElement secondIndex1 = greenLightLoc1Array.get(1);
        JsonElement thirdIndex1 = greenLightLoc1Array.get(2);

        JsonElement firstIndex2 = greenLightLoc2Array.get(0);
        JsonElement secondIndex2 = greenLightLoc2Array.get(1);
        JsonElement thirdIndex2 = greenLightLoc2Array.get(2);

        Location firstLocation = new Location(game.getGreenLightGame().getWorld(), firstIndex1.getAsInt(),
                secondIndex1.getAsInt(), thirdIndex1.getAsInt());
        Location secondLocation = new Location(game.getGreenLightGame().getWorld(), firstIndex2.getAsInt(),
                secondIndex2.getAsInt(), thirdIndex2.getAsInt());
        Location gunLocation = Locations.getCubeCenter(game.getGreenLightGame().getWorld(), firstLocation,
                secondLocation);

        game.getGreenLightGame().setCubeLocation1(firstLocation);
        game.getGreenLightGame().setCubeLocation2(secondLocation);

        game.getGreenLightGame().setCubeCenter2D(gunLocation);
    }

    public void guardMessage(Component text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (game.isGuard(player))
                player.sendMessage(text);
        });
    }

    public void adminMessage(Component text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.isOp())
                player.sendMessage(text);
        });
    }

}