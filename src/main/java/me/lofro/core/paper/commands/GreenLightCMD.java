package me.lofro.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("greenLight")
@CommandPermission("admin.perm")
public class GreenLightCMD extends BaseCommand {

    private final Main instance;
    private final Game game;

    public GreenLightCMD(Main instance) {
        this.instance = instance;
        this.game = instance.getGame();
    }

    @Subcommand("run")
    @CommandCompletion("10|20|30|40|60|70|80|90|100|110|120")
    public void run(CommandSender sender, int seconds) {
        if (game.getGreenLightGame().isRunning()) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego ya está siendo ejecutado."));
        } else {
            game.getGreenLightGame().runGame(seconds);
            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido iniciado con éxito."));
        }
    }

    @Subcommand("stop")
    public void stop(CommandSender sender) {
        GreenLightGame greenLightGame = game.getGreenLightGame();

        if (greenLightGame.isRunning()) {

            Bukkit.getScheduler().cancelTask(greenLightGame.getRunLaterTaskID());

            game.getTimer().end();

            greenLightGame.cancel();

            instance.unregisterListener(greenLightGame.getGreenLightListener());

            GreenLightGame newGreenLightGame = new GreenLightGame(greenLightGame.getInstance(), greenLightGame.getWorld(), greenLightGame.getGame());
            newGreenLightGame.loadGameCube();
            newGreenLightGame.setRunning(false);

            game.setGreenLightGame(newGreenLightGame);

            instance.loadGreenLightData();
            sender.sendMessage(Strings.format(game.getName() + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&cEl juego no está siendo ejecutado."));
        }
    }

}
