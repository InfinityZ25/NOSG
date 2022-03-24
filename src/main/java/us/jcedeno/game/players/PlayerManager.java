package us.jcedeno.game.players;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.types.PData;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.global.interfaces.Restorable;
import us.jcedeno.game.players.commands.RoleManagerCMD;

/**
 * A class to manage the players in the game, their roles & status, and interact
 * with the dataset.
 * 
 * @author jcedeno
 * 
 */
public class PlayerManager extends Restorable {
    protected transient SquidGame instance;
    private PData playerData;

    public PlayerManager(final SquidGame instance) {
        this.instance = instance;
        // restore state
        restore(instance.getDManager().pDataConfig());
        instance.registerCommands(instance.getCommandManager(), new RoleManagerCMD(this));
    }

    public PlayerManager() {
        this.playerData = new PData();
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        this.playerData = SquidGame.gson().fromJson(jsonConfig.getJsonObject(), PData.class);
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(SquidGame.gson().toJsonTree(playerData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The Pdata object.
     */
    public PData pData() {
        return playerData;
    }

    public boolean isPlayer(Player player) {
        return playerData.getPlayer(player.getName()) != null;
    }

    public boolean isGuard(Player player) {
        return playerData.getGuard(player.getName()) != null;
    }

    public boolean isDead(Player player) {
        return playerData.getPlayer(player.getName()).isDead();
    }

    public void guardMessage(Component text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isGuard(player))
                player.sendMessage(text);
        });
    }

    public void adminMessage(Component text) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.isOp())
                player.sendMessage(text);
        });
    }

}
