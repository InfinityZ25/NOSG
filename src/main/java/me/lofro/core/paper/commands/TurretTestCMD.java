package me.lofro.core.paper.commands;

import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.core.paper.utils.turrets.Turrets;
import me.lofro.core.paper.utils.turrets.exceptions.TurretListEmptyException;

@CommandAlias("turrets")
public class TurretTestCMD extends BaseCommand {
    private Turrets turrets;

    public TurretTestCMD() {
        this.turrets = new Turrets();
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

    @Subcommand("reser")
    public void reset(CommandSender sender) {
        this.turrets.getTurretLocations().clear();
        sender.sendMessage("Cleaned all locations.");
    }

}
