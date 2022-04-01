package me.lofro.game.games.deathnote.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.deathnote.DeathNoteManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("deathNote")
@CommandPermission("admin.perm")
public class DeathNoteCMD extends BaseCommand {

    private final DeathNoteManager dNManager;

    public DeathNoteCMD(DeathNoteManager dNManager) {
        this.dNManager = dNManager;
    }

    @Subcommand("start")
    @CommandCompletion("time winnerLimit")
    public void runGame(CommandSender sender, int time, int winnerLimit) {
        if (!dNManager.isRunning()) {
            dNManager.runGame(time, winnerLimit);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (dNManager.isRunning()) {
            dNManager.stopGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

}
