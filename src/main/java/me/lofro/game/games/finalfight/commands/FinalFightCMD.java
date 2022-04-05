package me.lofro.game.games.finalfight.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.Getter;
import me.lofro.game.SquidGame;
import me.lofro.game.games.finalfight.FinalFightManager;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("finalFight")
@CommandPermission("admin.perm")
public class FinalFightCMD extends BaseCommand {

    private final @Getter FinalFightManager finalFightManager;

    public FinalFightCMD(FinalFightManager finalFightManager) {
        this.finalFightManager = finalFightManager;
    }

    @Subcommand("start")
    public void runGame(CommandSender sender) {
        if (!finalFightManager.isRunning()) {
            finalFightManager.runGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido iniciado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego ya está siendo ejecutado."));
        }
    }

    @Subcommand("stop")
    public void stopGame(CommandSender sender) {
        if (finalFightManager.isRunning()) {
            finalFightManager.endGame();

            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl juego ha sido desactivado con éxito."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl juego no está siendo ejecutado."));
        }
    }

}
