package me.lofro.core.paper.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.NonNull;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.objects.SquidParticipant;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("squid")
@CommandPermission("admin.perm")
public class SquidCMD extends BaseCommand {

    private final @NonNull Main instance;
    private final Game game;

    public SquidCMD(@NotNull Main instance) {
        this.instance = instance;
        this.game = instance.getGame();
    }

    @Subcommand("setRole")
    @CommandCompletion("@players")
    public void setRole(CommandSender sender, @Flags("other") Player player, Game.Role role) {
        String name = player.getName();

        if (!(game.getParticipants().containsKey(name))) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl jugador introducido no es un participante."));
            return;
        }

        SquidParticipant squidParticipant = game.getParticipants().get(name);
        squidParticipant.setRole(role);

        game.loadParticipant(player);
        instance.saveParticipantData();
        sender.sendMessage(Strings.format(game.getName() + "&bEl rol del jugador &3" + player.getName() + " &bha sido asignado a &3" + role.name() + "&b."));
    }

    @Subcommand("getRole")
    @CommandCompletion("@players")
    public void getRole(CommandSender sender, @Flags("other") Player player) {
        String name = player.getName();

        if (!(game.getParticipants().containsKey(name))) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl jugador introducido no es un participante."));
            return;
        }

        SquidParticipant squidParticipant = game.getParticipants().get(name);
        Game.Role role = squidParticipant.getRole();

        sender.sendMessage(Strings.format(game.getName() + "&bEl participante &3" + name + " &btiene el rol de &3" + role.name() + "&b."));
    }

    @Subcommand("revive")
    @CommandCompletion("@players")
    public void revive(CommandSender sender, @Flags("other") Player player) {
        String name = player.getName();

        if (!(game.getParticipants().containsKey(name))) {
            sender.sendMessage(Strings.format(game.getName() + "&cEl jugador introducido no es un participante."));
            return;
        }

        SquidParticipant squidParticipant = game.getParticipants().get(name);
        if (squidParticipant.isDead()) {

            squidParticipant.setDead(false);

            game.loadParticipant(player);
            instance.saveParticipantData();

            player.setGameMode(GameMode.SURVIVAL);
            sender.sendMessage(Strings.format(game.getName() + "&bEl participante &3" + name + " &bha sido revivido."));
        } else {
            sender.sendMessage(Strings.format(game.getName() + "&bEl participante &3" + name + " &bya está vivo."));
        }
    }

}
