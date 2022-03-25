package us.jcedeno.game.global.commands;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import us.jcedeno.game.global.utils.extras.BukkitTimer;

@CommandAlias("timer")
public class TimerCMD extends BaseCommand {

    @Syntax("<seconds>")
    @Default
    public void startTimer(CommandSender sender, int time) {
        var timer = BukkitTimer.bTimer(time);

        timer.addAllViewers();

        timer.start().whenComplete((t, th) -> {
            if (th != null) {
                sender.sendMessage("Error: " + th.getMessage());
            } else {
                sender.sendMessage("Timer ended");
            }

        });

    }

}
