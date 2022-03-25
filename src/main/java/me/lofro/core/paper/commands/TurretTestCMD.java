package me.lofro.core.paper.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.data.GData;
import me.lofro.core.paper.data.PData;
import me.lofro.core.paper.utils.turrets.Turrets;
import me.lofro.core.paper.utils.turrets.exceptions.TurretListEmptyException;
import us.jcedeno.game.data.adapters.LocationSerializer;

@CommandAlias("turrets")
public class TurretTestCMD extends BaseCommand {
    private Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer()).setPrettyPrinting()
            .serializeNulls()
            .create();
    private Turrets turrets;

    public TurretTestCMD(Turrets turrets) {
        this.turrets = turrets;
    }

    @Subcommand("add")
    public void addLocation(Player sender) {
        this.turrets.getTurretLocations().add(sender.getLocation());
    }

    @CommandCompletion("@players")
    @Subcommand("shoot")
    public void shoot(CommandSender sender, @Flags("other") Player player) {
        try {
            this.turrets.shootPlayerFromTurrent(player.getLocation());
        } catch (TurretListEmptyException e) {
            sender.sendMessage("No turrets found.");
        }
    }

    @CommandCompletion("{particle}")
    @Subcommand("particle")
    public void setParticle(CommandSender sender, String particle) {
        var parsed = Particle.valueOf(particle.toUpperCase());
        this.turrets.setParticle(parsed);
        // send message to sender
        sender.sendMessage("Set particle to " + parsed.name());
    }

    @Subcommand("remove")
    public void remove(CommandSender sender) {
        // Remove last added location
        this.turrets.getTurretLocations().remove(this.turrets.getTurretLocations().size() - 1);
    }

    @Subcommand("reset")
    public void reset(CommandSender sender) {
        this.turrets.getTurretLocations().clear();
        sender.sendMessage("Cleaned all locations.");
    }

    @Subcommand("list")
    public void listAll(CommandSender sender) {
        sender.sendMessage("Turret Locations: " + gson.toJson(turrets.getTurretLocations()));

    }

    @Subcommand("gdata save")
    public void backupData(CommandSender sender) {

        sender.sendMessage("Attempting to backup data...");

        var gdata = new GData();

        gdata.backupData(Main.getInstance().getGame());

        sender.sendMessage("Should've worked");

    }

    @Subcommand("gdata load")
    public void loadData(CommandSender sender) {

        sender.sendMessage("Attempting to read data...");

    }

    @Subcommand("pdata save")
    public void pData(CommandSender sender) {
        var game = Main.getInstance().getGame();
        var pdata = new PData(game.getPlayers().values().stream().toList(),
                game.getGuards().values().stream().toList());

        pdata.backupData(game);
    }

    @Subcommand("pdata load")
    public void pDataLoad(CommandSender sender) {
        var pdata = new PData(null, null);

        pdata = pdata.fromJson(Main.getInstance().getParticipantData());

        sender.sendMessage("Loaded data: " + Main.getGson().toJson(pdata));
    }

}
