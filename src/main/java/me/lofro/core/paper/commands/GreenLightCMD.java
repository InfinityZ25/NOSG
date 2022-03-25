package me.lofro.core.paper.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.utils.strings.Strings;

@CommandAlias("greenLight")
@CommandPermission("admin.perm")
public class GreenLightCMD extends BaseCommand {

    private final Game game;

    public GreenLightCMD(Main instance) {
        this.game = instance.getGame();
    }

    @Subcommand("run")
    @CommandCompletion("30|60|90|120|150|180|210|240|270|300|330|360")
    public void run(CommandSender sender, int seconds) {
        if (game.getGreenLightGame().isRunning()) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego ya está siendo ejecutado."));
        } else {
            game.getGreenLightGame().runGame(seconds);
            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido iniciado con éxito."));
        }
    }

    @Subcommand("preStart")
    public void preStart(CommandSender sender) {
        if (game.getGreenLightGame().isRunning()) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego ya está siendo ejecutado."));
        } else {
            game.getGreenLightGame().preStart();
            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido preparado con éxito."));
        }
    }

    @Subcommand("stop")
    public void stop(CommandSender sender) {
        GreenLightGame greenLightGame = game.getGreenLightGame();

        if (greenLightGame.isRunning()) {

            greenLightGame.stopGame();

            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("shoot")
    @CommandCompletion("@players")
    public void shoot(CommandSender sender, @Flags("other") Player player) {
        if (game.isPlayer(player)) {
            if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)) {
                sender.sendMessage(Strings.format(game.getName() + "&cEl jugador indicado no puede ser disparado."));
                return;
            }
            game.getGreenLightGame().shoot(player);
        }
    }
}
