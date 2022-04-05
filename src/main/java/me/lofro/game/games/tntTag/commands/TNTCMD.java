package me.lofro.game.games.tntTag.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.tntTag.TNTManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("tntGame")
public class TNTCMD extends BaseCommand {

    private final TNTManager tntManager;

    public TNTCMD(TNTManager tntManager) {
        this.tntManager = tntManager;
    }

    @Subcommand("start")
    @CommandCompletion("time taggedLimit")
    public void startGame(CommandSender sender, int time, int taggedLimit) {
        if (!tntManager.isRunning()) {
            tntManager.runGame(time, taggedLimit);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (tntManager.isRunning()) {
            tntManager.stopGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

}
