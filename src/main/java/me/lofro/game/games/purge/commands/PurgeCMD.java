package me.lofro.game.games.purge.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.purge.PurgeManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@CommandAlias("purge")
@CommandPermission("admin.perm")
public class PurgeCMD extends BaseCommand {

    private final PurgeManager purgeManager;

    public PurgeCMD(PurgeManager purgeManager) {
        this.purgeManager = purgeManager;
    }

    @Subcommand("start")
    public void runGame(CommandSender sender) {
        if (!purgeManager.isRunning()) {
            purgeManager.runGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (purgeManager.isRunning()) {
            purgeManager.endGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("setArea")
    @CommandCompletion("@location @location")
    public void setArea(CommandSender sender, Location areaLower, Location areaUpper) {
        if (!purgeManager.isRunning()) {
            purgeManager.purgeData().setAreaLower(areaLower);
            purgeManager.purgeData().setAreaUpper(areaUpper);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl área ha sido modificada con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar el área mientras el juego está siendo ejecutado."));
        }
    }

    @Subcommand("setFoodLocation")
    @CommandCompletion("@location")
    public void setFoodLocation(CommandSender sender, Location foodLocation) {
        purgeManager.purgeData().setFoodLocation(foodLocation);
        sender.sendMessage(Strings.format(SquidGame.prefix + "&bLa localización de la comida ha sido modificada con éxito."));
    }

    @Subcommand("spawnFoodPlate")
    public void spawnFoodPlate(CommandSender sender) {
        purgeManager.spawnFoodPlate();
        sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl plato de comida ha sido spawneado con éxito."));
    }

    @Subcommand("removeFoodPlate")
    public void removeFoodPlate(CommandSender sender) {
        if (purgeManager.getFoodPlate() != null) {
            purgeManager.removeFoodPlate();
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl plato de comida ha sido eliminado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl plato de comida no existe."));
        }
    }

}
