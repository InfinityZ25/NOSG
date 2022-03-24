package us.jcedeno.game.players.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.jcedeno.game.SquidGame;
import us.jcedeno.game.global.utils.Strings;
import us.jcedeno.game.players.PlayerManager;
import us.jcedeno.game.players.enums.Role;
import us.jcedeno.game.players.objects.SquidPlayer;

/**
 * A command to manually change & query the roles of players.
 * 
 * @author jcedeno
 */
@CommandAlias("role")
public class RoleManagerCMD extends BaseCommand {

    private final PlayerManager pManager;

    public RoleManagerCMD(PlayerManager pManager) {
        this.pManager = pManager;
    }

    @Subcommand("setRole")
    @CommandCompletion("@players")
    public void setRole(CommandSender sender, @Flags("other") Player player, Role role) {
        var name = player.getName();

        var squidParticipant = pManager.pData().getParticipant(name);

        boolean already = !pManager.pData().changeRoles(squidParticipant, role);

        if (already) {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl rol del jugador " + name + " ya es " + role + "."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl rol del jugador &3" + player.getName() + " &bha sido asignado a &3" + role.name() + "&b."));
        }
    }

    @Subcommand("getRole")
    @CommandCompletion("@players")
    public void getRole(CommandSender sender, @Flags("other") Player player) {
        var name = player.getName();

        var role = (pManager.pData().getParticipant(name) instanceof SquidPlayer) ? Role.PLAYER : Role.GUARD;

        sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl participante &3" + name + " &btiene el rol de &3" + role.name() + "&b."));
    }

    @Subcommand("revive")
    @CommandCompletion("@players")
    public void revive(CommandSender sender, @Flags("other") Player player) {
        var name = player.getName();

        if (!pManager.isPlayer(player)) return;

        var squidPlayer = pManager.pData().getPlayer(name);

        if (squidPlayer.isDead()) {
            squidPlayer.setDead(false);
            player.setGameMode(GameMode.ADVENTURE);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl jugador &3" + name + " &bha sido revivido."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl jugador " + name + " no esta muerto."));
        }
    }

}
