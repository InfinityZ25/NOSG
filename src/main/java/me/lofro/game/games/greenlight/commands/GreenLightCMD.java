package me.lofro.game.games.greenlight.commands;

import co.aikar.commands.annotation.*;
import com.google.common.collect.ImmutableList;
import me.lofro.game.SquidGame;
import me.lofro.game.global.utils.Strings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import me.lofro.game.games.greenlight.GreenLightManager;
import org.bukkit.entity.Player;

@CommandAlias("greenLight")
@CommandPermission("admin.perm")
public class GreenLightCMD extends BaseCommand {

    private final GreenLightManager gLightManager;

    public GreenLightCMD(GreenLightManager gLightManager) {
        this.gLightManager = gLightManager;

        SquidGame.getInstance().getCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Subcommand("run")
    @CommandCompletion("seconds greenLowestTimeBound greenHighestTimeBound redLowestTimeBound redHighestTimeBound")
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

    @Subcommand("shoot")
    @CommandCompletion("@players")
    public void shoot(CommandSender sender, @Flags("other") Player player) {
        gLightManager.shoot(player);
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
    @CommandCompletion("@location @location")
    public void setCube(CommandSender sender, Location cubeLower, Location cubeUpper) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar el cubo mientras el juego está siendo ejecutado."));
        } else {
            var gLightData = gLightManager.getGManager().gData().greenLightData();

            gLightData.setCubeLower(cubeLower);
            gLightData.setCubeUpper(cubeUpper);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl cubo de juego ha sido actualizado correctamente."));
        }
    }

    @Subcommand("addCannon")
    @CommandCompletion("@location")
    public void addCannon(CommandSender sender, Location cannon) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar los cañones mientras el juego está siendo ejecutado."));
        } else {
            var gLightData = gLightManager.getGManager().gData().greenLightData();

            gLightData.getCannonLocations().add(cannon);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl cañón ha sido añadido correctamente."));
        }
    }

    @Subcommand("removeCannon")
    @CommandCompletion("@location")
    public void removeCannon(CommandSender sender, Location cannon) {
        if (gLightManager.isRunning()) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cNo puedes modificar los cañones mientras el juego está siendo ejecutado."));
        } else {
            var gLightData = gLightManager.getGManager().gData().greenLightData();

            gLightData.getCannonLocations().remove(cannon);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl cañón ha sido eliminado correctamente."));
        }
    }

}
