package us.jcedeno.game.global.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import us.jcedeno.game.games.GameManager;
import us.jcedeno.game.global.utils.Strings;
import us.jcedeno.game.global.utils.datacontainers.Data;
import us.jcedeno.game.global.utils.datacontainers.PlayerIsNotOnlineException;
import us.jcedeno.game.players.PlayerManager;

public class GlobalListener implements Listener {

    private final PlayerManager pManager;
    private final GameManager gManager;

    public GlobalListener(PlayerManager pManager, GameManager gManager) {
        this.pManager = pManager;
        this.gManager = gManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        e.joinMessage(null);

        pManager.guardMessage(Component.text(Strings.format("&7El jugador &6" + name + " &7ha entrado al servidor.")));

        if (pManager.pData().getParticipant(name) == null) {
            if (player.isOp()) {
                pManager.pData().addGuard(name);
            } else {
                pManager.pData().addPlayer(name);
            }
        }

        var timer = gManager.getTimer();
        timer.addPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        e.quitMessage(null);

        pManager.guardMessage(Component.text(Strings.format("&7El jugador &8" + name + " &7ha abandonado el servidor.")));

        var timer = gManager.getTimer();
        timer.removePlayer(player);
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();

        e.setCancelled(true);

        if (pManager.isGuard(player)) pManager.guardMessage(Strings.componentFormat("&cGUARDS &8| &7" + player.getName() + " &8| &8&l>> &7"
                + PlainTextComponentSerializer.plainText().serialize(e.message())));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        var deathLocation = player.getLocation();

        try {
            PersistentDataContainer dataContainer = Data.getData(player);
            Data.set(dataContainer, "DEATH_LOCATION", gManager.getSquidInstance(), PersistentDataType.INTEGER_ARRAY, new int[]{deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ()});
            Data.set(dataContainer, "DEATH_LOCATION_ROTATION", gManager.getSquidInstance(), PersistentDataType.FLOAT, player.getLocation().getYaw());
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }

        e.deathMessage(null);

        if (!pManager.isPlayer(player) || player.getGameMode().equals(GameMode.SPECTATOR)) return;

        var squidPlayer = pManager.pData().getPlayer(name);
        int playerID = squidPlayer.getId();

        player.setGameMode(GameMode.SPECTATOR);
        if (squidPlayer.isDead()) return;

        squidPlayer.setDead(true);

        Bukkit.getOnlinePlayers().forEach(online -> online.playSound(online.getLocation(), "sfx.elimination", 1,1));

        Bukkit.broadcast(Component.text(me.lofro.core.paper.utils.strings.Strings.format("&bEl jugador &3#" + playerID + " " + name + " &bha sido eliminado.")));

        //TODO CABEZA.
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        var player = e.getPlayer();

        try {
            PersistentDataContainer persistentDataContainer = Data.getData(player);
            int[] locationBlocks = Data.get(persistentDataContainer,"DEATH_LOCATION",gManager.getSquidInstance(), PersistentDataType.INTEGER_ARRAY);
            float yaw = Data.get(persistentDataContainer, "DEATH_LOCATION_ROTATION", gManager.getSquidInstance(), PersistentDataType.FLOAT);
            Location respawnLocation = new Location(player.getWorld(), locationBlocks[0], locationBlocks[1], locationBlocks[2]);
            respawnLocation.setYaw(yaw);

            e.setRespawnLocation(respawnLocation);
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        if(!player.hasPotionEffect(PotionEffectType.SATURATION) && !player.hasPotionEffect(PotionEffectType.HUNGER)){
            e.setCancelled(true);
        }
    }


}
