package us.jcedeno.game.data.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.DataManager;
import us.jcedeno.game.data.enums.SquidDataType;
import us.jcedeno.game.global.utils.Strings;

@CommandAlias("data")
public class DataCMD extends BaseCommand {

    private final DataManager dManager;

    public DataCMD(DataManager dManager) {
        this.dManager = dManager;
    }

    @Subcommand("save")
    public void save(CommandSender sender, SquidDataType squidDataType) {
        dManager.save(squidDataType);
        sender.sendMessage(Strings.format(SquidGame.prefix + "&bEl data config de &3" + squidDataType.name() + " &bha sido guardado con éxito."));
    }
}
