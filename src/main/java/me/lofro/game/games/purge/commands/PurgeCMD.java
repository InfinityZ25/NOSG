package me.lofro.game.games.purge.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.purge.PurgeManager;
import me.lofro.game.global.utils.Strings;
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

    @Subcommand("spawnFoodPlate")
    public void spawnFoodPlate(CommandSender sender) {
        purgeManager.spawnFoodPlate();
    }

    @Subcommand("removeFoodPlate")
    public void removeFoodPlate(CommandSender sender) {
        if (purgeManager.getFoodPlate() != null) purgeManager.removeFoodPlate();
    }

}
