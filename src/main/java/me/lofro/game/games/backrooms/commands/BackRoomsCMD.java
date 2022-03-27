package me.lofro.game.games.backrooms.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.backrooms.BackRoomsManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("backrooms")
@CommandPermission("admin.perm")
public class BackRoomsCMD extends BaseCommand {

    private final BackRoomsManager bRManager;

    public BackRoomsCMD(BackRoomsManager bRManager) {
        this.bRManager = bRManager;
    }

    @Subcommand("start")
    @CommandCompletion("safeSeconds winnerLimit")
    public void runGame(CommandSender sender, int safeSeconds, int winnerLimit) {
        if (!bRManager.isRunning()) {
            bRManager.runGame(safeSeconds, winnerLimit);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stop(CommandSender sender) {
        if (bRManager.isRunning()) {
            bRManager.stopGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("preGame")
    public void preGame(CommandSender sender) {
        if (!bRManager.isRunning()) {
            bRManager.preGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido preparado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

}
