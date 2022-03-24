package us.jcedeno.game.games.greenlight.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import us.jcedeno.game.SquidGame;
import us.jcedeno.game.games.greenlight.GreenLightManager;
import us.jcedeno.game.global.utils.Strings;

@CommandAlias("greenLight")
public class GreenLightCMD extends BaseCommand {

    private final GreenLightManager gLightManager;

    public GreenLightCMD(GreenLightManager gLightManager) {
        this.gLightManager = gLightManager;
    }

    @Subcommand("run")
    @CommandCompletion("@runGLightGame")
    public void runGame(CommandSender sender, int seconds, int greenLowestTimeBound, int greenHighestTimeBound,
            int redLowestTimeBound, int redHighestTimeBound) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        } else {
            gLightManager.setGreenLowestTimeBound(greenLowestTimeBound);
            gLightManager.setGreenHighestTimeBound(greenHighestTimeBound);

            gLightManager.setRedLowestTimeBound(redLowestTimeBound);
            gLightManager.setRedHighestTimeBound(redHighestTimeBound);

            gLightManager.runGame(seconds);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (gLightManager.isRunning()) {
            gLightManager.stopGame();
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("preStart")
    public void preStart(CommandSender sender) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        } else {
            gLightManager.preStart();
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido preparado con éxito."));
        }
    }

    @Subcommand("setCube")
    public void setCube(CommandSender sender, Location cubeLower, Location cubeUpper) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar el cubo mientras el juego está siendo ejecutado."));
        } else {
            var gData = gLightManager.getGManager().gData().gLightData();

            gData.setCubeLower(cubeLower);
            gData.setCubeUpper(cubeUpper);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl cubo de juego ha sido actualizado correctamente."));
        }
    }
}
