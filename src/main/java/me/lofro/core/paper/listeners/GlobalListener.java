package me.lofro.core.paper.listeners;

import me.lofro.core.paper.Game;
import me.lofro.core.paper.events.GameTickEvent;
import me.lofro.core.paper.objects.SquidPlayer;
import me.lofro.core.paper.objects.Timer;
import me.lofro.core.paper.utils.persistentDataContainers.Data;
import me.lofro.core.paper.utils.persistentDataContainers.PlayerIsNotOnlineException;
import me.lofro.core.paper.utils.strings.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.lofro.core.paper.Main;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GlobalListener implements Listener {
    
    private final Main instance;
    private final Game game;

    public GlobalListener(Main instance){
        this.instance = instance;
        this.game = instance.getGame();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String name = player.getName();

        e.joinMessage(null);

        instance.adminMessage(Strings.format("&7El jugador &8" + name + " &7ha entrado al servidor."));

        game.loadParticipant(player);
        player.playSound(player.getLocation(), "sfx.server_join", 1, 1);

        Timer timer = game.getTimer();
        timer.addPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String name = player.getName();

        e.quitMessage(null);

        instance.adminMessage(Strings.format("&7El jugador &8" + name + " &7ha abandonado el servidor."));

        Timer timer = game.getTimer();
        timer.removePlayer(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        String name = player.getName();

        Location deathLocation = player.getLocation();

        try {
            PersistentDataContainer dataContainer = Data.getData(player);
            Data.set(dataContainer, "DEATH_LOCATION", PersistentDataType.INTEGER_ARRAY, new int[]{deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ()});
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }

        e.deathMessage(null);

        if (!(game.isPlayer(player)) || player.getGameMode().equals(GameMode.SPECTATOR)) return;

        SquidPlayer squidPlayer = game.getPlayers().get(name);
        int id = squidPlayer.getId();

        player.setGameMode(GameMode.SPECTATOR);
        squidPlayer.setDead(true);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), "sfx.elimination", 1,1);
        }

        Bukkit.broadcast(Component.text(Strings.format("&bEl jugador &3#" + id + " " + name + " &bha sido eliminado.")));
        Bukkit.getScheduler().runTaskLater(instance, () -> player.banPlayer(Strings.format("&bHAS SIDO &3ELIMINADO&b.")), 20*5);

        setSkullOnGround(player, deathLocation);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        try {
            PersistentDataContainer persistentDataContainer = Data.getData(player);
            int[] locationBlocks = Data.get(persistentDataContainer,"DEATH_LOCATION", PersistentDataType.INTEGER_ARRAY);
            Location respawnLocation = new Location(player.getWorld(), locationBlocks[0], locationBlocks[1], locationBlocks[2]);

            e.setRespawnLocation(respawnLocation);
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e){
        Player player = (Player) e.getEntity();
        if(!player.hasPotionEffect(PotionEffectType.SATURATION) && !player.hasPotionEffect(PotionEffectType.HUNGER)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTick(GameTickEvent e) {
        Game game = instance.getGame();
        Bukkit.getScheduler().runTask(instance, () -> {

            Timer timer = game.getTimer();
            if (timer.isActive()) {
                int currentTime = (int) game.getGameTime();
                timer.refreshTime(currentTime);
            }

        });
    }

    public void setSkullOnGround(Player player, Location location) {

        for (int y = location.getBlockY(); y > -64; y--) {

            if(location.subtract(0, 1, 0).getBlock().getType() == Material.AIR) continue;

            location.add(0, 1, 0);

            Block skullBlock = location.getBlock();
            skullBlock.setType(Material.PLAYER_HEAD);

            BlockState state = skullBlock.getState();
            Skull skull = (Skull) state;
            UUID uuid = player.getUniqueId();

            skull.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(uuid));

            Rotatable skullRotation = (Rotatable) skull.getBlockData();
            skullRotation.setRotation(getCardinalDirection(player));
            skull.setBlockData(skullRotation);

            skull.update();

            break;
        }
    }

    //TODO OPTIMIZAR POR FAVOR.
    private BlockFace getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return BlockFace.EAST;
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return BlockFace.SOUTH_EAST;
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return BlockFace.SOUTH;
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return BlockFace.SOUTH_WEST;
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return BlockFace.WEST;
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return BlockFace.NORTH_WEST;
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return BlockFace.NORTH;
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return BlockFace.NORTH_EAST;
        }
        return BlockFace.NORTH;
    }

}
