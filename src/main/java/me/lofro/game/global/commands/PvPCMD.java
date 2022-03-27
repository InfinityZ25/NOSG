package me.lofro.game.global.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.lofro.game.SquidGame;
import me.lofro.game.games.GameManager;
import me.lofro.game.global.enums.PvPState;
import me.lofro.game.global.utils.Strings;
import org.bukkit.command.CommandSender;

@CommandAlias("pvp")
@CommandPermission("admin.perm")
public class PvPCMD extends BaseCommand {

    private final GameManager gManager;

    public PvPCMD(GameManager gManager) {
        this.gManager = gManager;
    }

    @Default
    public void setPvP(CommandSender sender, PvPState pvPState) {
        var gData = gManager.gData();

        if (!pvPState.equals(gData.getPvPState())) {
            gData.setPvPState(pvPState);
            sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl pvp ha sido actualizado a &3" + pvPState.name() + "&b."));
        } else {
            sender.sendMessage(Strings.format(SquidGame.prefix + "&cEl pvp ya es " + pvPState.name() + "."));
        }
    }

}
