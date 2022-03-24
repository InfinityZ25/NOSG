package us.jcedeno.game.players;

import org.bukkit.entity.Player;

import us.jcedeno.game.Squid;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.global.interfaces.Restorable;
import us.jcedeno.game.players.types.PData;

/**
 * A class to manage the players in the game, their roles & status, and interact
 * with the dataset.
 * 
 * @author jcedeno
 * 
 */
public class PlayerManager extends Restorable {
    protected transient Squid instance;
    private PData playerData;

    public PlayerManager(final Squid instance) {
        this.instance = instance;
    }

    public PlayerManager() {
        this.playerData = new PData();
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        this.playerData = Squid.gson().fromJson(jsonConfig.getJsonObject(), PData.class);
    }

    @Override
    protected void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(Squid.gson().toJsonTree(playerData).getAsJsonObject());
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

}
