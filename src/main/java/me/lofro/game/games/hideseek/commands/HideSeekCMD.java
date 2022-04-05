package me.lofro.game.games.hideseek.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.game.SquidGame;
import me.lofro.game.games.hideseek.HideSeekManager;
import me.lofro.game.games.hideseek.enums.HideGameState;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("hideSeekGame")
@CommandPermission("admin.perm")
public class HideSeekCMD extends BaseCommand {

    private final HideSeekManager hSManager;

    public HideSeekCMD(HideSeekManager hSManager) {
        this.hSManager = hSManager;
    }

    @Subcommand("start")
    @CommandCompletion("hideTime seekTime")
    public void startGame(CommandSender sender, int hideTime, int seekTime) {
        if (!hSManager.isRunning()) {
            hSManager.runGame(hideTime, seekTime);

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (hSManager.isRunning()) {
            hSManager.stopGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("updateTime")
    @CommandCompletion(" time")
    public void updateTime(CommandSender sender, HideGameState hideGameState, int time) {
        if (hSManager.isRunning()) {
            switch (hideGameState) {
                case HIDE -> {
                    hSManager.updateHideTime(time);
                    sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl tiempo para &3" + hideGameState + "&b ha sido actualizado con éxito"));
                }
                case SEEK -> {
                    hSManager.updateSeekTime(time);
                    sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl tiempo para &3" + hideGameState + "&b ha sido actualizado con éxito"));
                }
                default -> sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl modo de juego escogido no puede ser actualizado."));
            }
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

}
