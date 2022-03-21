package me.lofro.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandAlias("game")
@CommandPermission("admin.perm")
public class GameCMD extends BaseCommand {

    private final @NonNull Main instance;
    private final Game game;

    public GameCMD(@NotNull Main instance) {
        this.instance = instance;
        this.game = instance.getGame();
    }

    @Subcommand("getDay")
    public void getDay(CommandSender sender) {
        if (game.getDay() == null) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl día no está definido."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&bEl día actual es &3" + game.getDay() + "&b."));
        }
    }

    @Subcommand("setDay")
    public void setDay(CommandSender sender, Game.Day day) {
        instance.getGame().setDay(day);
        instance.saveGameData();
        sender.sendMessage(Strings.format(game.getName() + "&bEl día actual ha sido actualizado a &3" + day + "&b."));
    }

}
