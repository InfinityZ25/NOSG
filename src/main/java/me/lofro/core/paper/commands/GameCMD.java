package me.lofro.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("game")
@CommandPermission("admin.perm")
public class GameCMD extends BaseCommand {

    private final @NonNull Main instance;

    public GameCMD(@NotNull Main instance) {
        this.instance = instance;
    }

    @Subcommand("getDay")
    public void getDay(CommandSender sender) {
        var game = instance.getGame();
        if (instance.getGame().getDay() == null) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl día no está definido."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&bEl día actual es &3" + game.getDay() + "&b."));
        }
    }

    @Subcommand("setDay")
    public void setDay(CommandSender sender, Game.Day day) {
        instance.getGame().setDay(day);
        sender.sendMessage(Strings.format(instance.getGame().getName() + "&bEl día actual ha sido actualizado a &3" + day + "&b."));
    }

    @Subcommand("pvp")
    public void pvp(CommandSender sender, Game.PvPState pvpState) {
        instance.getGame().setPvPState(pvpState);
        sender.sendMessage(Strings.format(instance.getGame().getName() + "&bEl modo de PVP actual ha sido actualizado a &3" + pvpState + "&b."));
    }

}
