package me.lofro.game.games.parkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.parkour.ParkourManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@CommandAlias("parkour")
@CommandPermission("admin.perm")
public class ParkourCMD extends BaseCommand {

    private final ParkourManager pkManager;

    public ParkourCMD(ParkourManager pkManager) {
        this.pkManager = pkManager;
    }

    @Subcommand("start")
    @CommandCompletion("winnerLimit")
    public void startGame(CommandSender sender, int winnerLimit) {
        if (!pkManager.isRunning()) {
            pkManager.runGame(winnerLimit);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (pkManager.isRunning()) {
            pkManager.stopGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("raiseLava")
    public void raiseLava(CommandSender sender) {
        if (!pkManager.isRunning()) {
            pkManager.raiseLava();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bLa lava ha ascendido con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar la lava mientras el juego está siendo ejecutado."));
        }
    }

    @Subcommand("descendLava")
    public void descendLava(CommandSender sender) {
        if (!pkManager.isRunning()) {
            pkManager.descendLava();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bLa lava ha descendido con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar la lava mientras el juego está siendo ejecutado."));
        }
    }

    @Subcommand("setArea")
    @CommandCompletion("@location @location")
    public void setArea(CommandSender sender, Location areaLower, Location areaUpper) {
        if (!pkManager.isRunning()) {
            pkManager.parkourData().setAreaLower(areaLower);
            pkManager.parkourData().setAreaUpper(areaUpper);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl área ha sido modificada con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar el área mientras el juego está siendo ejecutado."));
        }
    }

    @Subcommand("setGoal")
    @CommandCompletion("@location @location")
    public void setGoal(CommandSender sender, Location goalLower, Location goalUpper) {
        if (!pkManager.isRunning()) {
            pkManager.parkourData().setGoalLower(goalLower);
            pkManager.parkourData().setGoalUpper(goalUpper);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bLa meta ha sido modificada con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar la meta mientras el juego está siendo ejecutado."));
        }
    }

    @Subcommand("convertToWater")
    public void convertToWater(CommandSender sender) {
        pkManager.convertToWater();
    }

    @Subcommand("convertToLava")
    public void convertToLava(CommandSender sender) {
        pkManager.convertToLava();
    }
}
