package me.lofro.game.global.listeners;

import me.lofro.game.SquidGame;
import me.lofro.game.players.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lofro.game.games.GameManager;
import me.lofro.game.global.utils.Strings;
import me.lofro.game.global.utils.datacontainers.Data;
import me.lofro.game.global.utils.datacontainers.PlayerIsNotOnlineException;
import me.lofro.game.players.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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
        player.playSound(player.getLocation(), "sfx.server_join", 1, 1);

        if (pManager.pData().getParticipant(name) == null) {
            if (player.isOp()) {
                pManager.pData().addGuard(name);
                player.sendMessage(Strings.format(SquidGame.prefix + "&bTu rol ha sido asignado automáticamente a &3GUARDIA&b debido a que tienes permisos de administrador."));
            } else {
                pManager.pData().addPlayer(name);
                player.setGameMode(GameMode.ADVENTURE);
            }
        }

        var timer = gManager.getTimer();
        if (timer.isActive())
            timer.addPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        e.quitMessage(null);

        pManager.guardMessage(
                Component.text(Strings.format("&7El jugador &8" + name + " &7ha abandonado el servidor.")));

        var timer = gManager.getTimer();
        timer.removePlayer(player);
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        var player = e.getPlayer();

        e.setCancelled(true);

        if (pManager.isGuard(player))
            pManager.guardMessage(Strings.componentFormat("&cGUARDS &8| &7" + player.getName() + " &8| &8&l>> &7"
                    + PlainTextComponentSerializer.plainText().serialize(e.message())));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        var deathLocation = player.getLocation();

        try {
            PersistentDataContainer dataContainer = Data.getData(player);
            Data.set(dataContainer, "DEATH_LOCATION", gManager.getSquidInstance(), PersistentDataType.INTEGER_ARRAY,
                    new int[] { deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ() });
            Data.set(dataContainer, "DEATH_LOCATION_ROTATION", gManager.getSquidInstance(), PersistentDataType.FLOAT,
                    player.getLocation().getYaw());
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }

        e.deathMessage(null);

        if (!pManager.isPlayer(player) || player.getGameMode().equals(GameMode.SPECTATOR))
            return;

        var squidPlayer = pManager.pData().getPlayer(name);
        int playerID = squidPlayer.getId();

        player.setGameMode(GameMode.SPECTATOR);
        if (squidPlayer.isDead()) return;

        squidPlayer.setDead(true);

        Bukkit.getOnlinePlayers().forEach(online -> online.playSound(online.getLocation(), "sfx.elimination", 1, 1));

        Bukkit.broadcast(Component.text(Strings.format("&bEl jugador &3#" + playerID + " " + name + " &bha sido eliminado.")));

        setSkullOnGround(player);
    }

    private void setSkullOnGround(Player player) {
        var location = player.getLocation();

        for (int y = location.getBlockY(); y > -64; y--) {

            if(location.clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR) continue;

            Block skullBlock = location.getBlock();
            skullBlock.setType(Material.PLAYER_HEAD);

            BlockState state = skullBlock.getState();
            Skull skull = (Skull) state;
            UUID uuid = player.getUniqueId();

            skull.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(uuid));

            Rotatable skullRotation = (Rotatable) skull.getBlockData();
            skullRotation.setRotation(getCardinalDirectionFace(player.getLocation()).getOppositeFace());
            skull.setBlockData(skullRotation);

            skull.update();

            break;
        }
    }

    private BlockFace getCardinalDirectionFace(Location location) {
        var yaw = location.getYaw();
        double rotation = (yaw) % 360.0F;

        if (rotation < 0.0D) rotation += 360.0D;

        int index = (int) (rotation / 45);

        ArrayList<BlockFace> allFaces = new ArrayList<>(
                Arrays.asList(BlockFace.SOUTH, BlockFace.SOUTH_WEST,
                        BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST));

        return allFaces.get(index);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        var player = e.getPlayer();

        try {
            PersistentDataContainer persistentDataContainer = Data.getData(player);
            int[] locationBlocks = Data.get(persistentDataContainer, "DEATH_LOCATION", gManager.getSquidInstance(),
                    PersistentDataType.INTEGER_ARRAY);
            float yaw = Data.get(persistentDataContainer, "DEATH_LOCATION_ROTATION", gManager.getSquidInstance(),
                    PersistentDataType.FLOAT);
            Location respawnLocation = new Location(player.getWorld(), locationBlocks[0], locationBlocks[1],
                    locationBlocks[2]);
            respawnLocation.setYaw(yaw);

            e.setRespawnLocation(respawnLocation);
        } catch (PlayerIsNotOnlineException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        var player = (Player) e.getEntity();
        if (!player.hasPotionEffect(PotionEffectType.SATURATION) && !player.hasPotionEffect(PotionEffectType.HUNGER)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        var player = e.getPlayer();

        if (pManager.isPlayer(player)) {
            if (e.getCause().equals(PlayerGameModeChangeEvent.Cause.COMMAND) || e.getCause().equals(PlayerGameModeChangeEvent.Cause.DEFAULT_GAMEMODE)) {
                e.setCancelled(true);
                if (player.isOp()) player.sendMessage(Strings.format("&cTu modo de juego no ha sido actualizado ya que tu rol es PLAYER."));
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        var entity = e.getEntity();
        var damager = e.getDamager();

        var pvPState = gManager.gData().getPvPState();

        if (entity instanceof Player player) {
            var name = player.getName();

            var pSquidParticipant = pManager.pData().getParticipant(name);

            var pRole = pManager.pData().getRole(pSquidParticipant);

            if (damager instanceof Player playerDamager) {
                var playerDamagerName = playerDamager.getName();

                var dSquidParticipant = pManager.pData().getParticipant(playerDamagerName);

                var dRole = pManager.pData().getRole(dSquidParticipant);

                switch (pvPState) {
                    case ONLY_GUARDS -> {
                        //Player to player.
                        if (pRole == Role.PLAYER && dRole == pRole) {
                            e.setCancelled(true);
                            break;
                        }
                        //Guard to guard.
                        if (pRole == Role.GUARD && dRole == pRole) {
                            e.setCancelled(true);
                            break;
                        }
                        //Player to guard.
                        if (dRole == Role.PLAYER && pRole == Role.GUARD) {
                            e.setCancelled(true);
                        }
                    }
                    case NONE -> e.setCancelled(true);
                }
            } else if (damager instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Player pShooter) {
                    String pShooterName = pShooter.getName();

                    var pShooterSquidParticipant = pManager.pData().getParticipant(pShooterName);

                    var pShooterRole = pManager.pData().getRole(pShooterSquidParticipant);

                    switch (pvPState) {
                        case ONLY_GUARDS -> {
                            //Player to player.
                            if (pRole == Role.PLAYER && pShooterRole == pRole) {
                                e.setCancelled(true);
                                break;
                            }
                            //Guard to guard.
                            if (pRole == Role.GUARD && pShooterRole == pRole) {
                                e.setCancelled(true);
                                break;
                            }
                            //Player to guard.
                            if (pShooterRole == Role.PLAYER && pRole == Role.GUARD) {
                                e.setCancelled(true);
                                break;
                            }

                            e.setDamage(1000);
                        }
                        case NONE -> e.setCancelled(true);
                        default -> e.setDamage(1000);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        var player = e.getPlayer();
        var name = player.getName();

        var block = e.getClickedBlock();

        var squidParticipant = pManager.pData().getParticipant(name);

        var role = pManager.pData().getRole(squidParticipant);

        if (role == Role.GUARD) {
            if (e.getHand() == EquipmentSlot.HAND) {
                if (block != null && block.getType().equals(Material.IRON_DOOR)) {
                    // 1ms delay fixing visual bug.
                    Bukkit.getScheduler().runTask(SquidGame.getInstance(), task -> openDoors(block));
                }
            }
        }
    }

    /**
     * Function that opens both door hinges at the same time.
     *
     * @param block Block to open.
     */
    private void openDoors(Block block) {
        if (block.getBlockData() instanceof Door door) {
            ArrayList<BlockFace> mainFaces = new ArrayList<>(Arrays.asList(BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH));

            door.setOpen(!door.isOpen());
            block.setBlockData(door);

            var hinge = door.getHinge();
            var index = mainFaces.indexOf(door.getFacing());
            var face = mainFaces.get(hinge == Door.Hinge.RIGHT ? ( index == 0 ? 3 : index -1 ) : (index == 3 ? 0 : index + 1));

            var relative = block.getRelative(face);
            if (relative.getBlockData() instanceof Door secondDoor) {
                if (secondDoor.isOpen() == door.isOpen()) return;
                if (hinge == secondDoor.getHinge()) return;
                secondDoor.setOpen(!secondDoor.isOpen());
                relative.setBlockData(secondDoor);
            }
        }
    }

}
