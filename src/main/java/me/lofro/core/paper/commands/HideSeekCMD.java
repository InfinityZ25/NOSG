package me.lofro.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.games.HideSeekGame;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("hideGame")
@CommandPermission("admin.perm")
public class HideSeekCMD extends BaseCommand {

    private final Main instance;
    private final Game game;

    public HideSeekCMD(Main instance) {
        this.instance = instance;
        this.game = instance.getGame();
    }

    @Subcommand("run")
    public void run(CommandSender sender, int hideTime, int seekTime) {
        if (!game.getHideSeekGame().isRunning()) {

            game.getHideSeekGame().runGame(hideTime, seekTime);

            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (game.getHideSeekGame().isRunning()) {

            game.getHideSeekGame().stopGame();

            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("updateTime")
    public void updateTime(CommandSender sender, HideSeekGame.HideGameState hideGameState, int time) {
        if (game.getHideSeekGame().isRunning()) {
            switch (hideGameState) {
                case HIDE -> {
                    game.getHideSeekGame().updateHideTime(time);
                    sender.sendMessage(Strings.format(game.getName() + "&bEl tiempo para &3" + hideGameState + "&b ha sido actualizado con éxito"));
                }
                case SEEK -> {
                    game.getHideSeekGame().updateSeekTime(time);
                    sender.sendMessage(Strings.format(game.getName() + "&bEl tiempo para &3" + hideGameState + "&b ha sido actualizado con éxito"));
                }
                default -> sender.sendMessage(Strings.format(game.getName() + "&cEl modo de juego escogido no puede ser actualizado."));
            }
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego no está siendo ejecutado."));
        }
    }

}
