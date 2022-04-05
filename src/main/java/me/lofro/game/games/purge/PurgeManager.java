package me.lofro.game.games.purge;

import lombok.Getter;
import me.lofro.game.data.types.GameData;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.purge.types.PurgeData;
import me.lofro.game.global.enums.PvPState;
import me.lofro.game.global.item.CustomItems;
import me.lofro.game.global.utils.Locations;
import me.lofro.game.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

public class PurgeManager {

    private final @Getter GameManager gManager;

    private @Getter boolean isRunning = false;

    private @Getter ArmorStand foodPlate;

    private final @Getter World world;

    public PurgeManager(GameManager gManager, World world) {
        this.gManager = gManager;
        this.world = world;
    }

    public void runGame() {
        this.isRunning = true;

        gData().setPvPState(PvPState.ALL);

        disableLight(true);
    }

    public void endGame() {
        this.isRunning = false;

        gData().setPvPState(PvPState.ONLY_GUARDS);

        disableLight(false);
    }

    public void disableLight(Boolean bool) {
        var locations = Locations.getBlocksInsideCube(areaLower(), areaUpper());
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "sfx.lights", 1f, 1f));
        if (bool) {
            locations.forEach(loc -> {
                var block = loc.getBlock();

                if (block.getType().equals(Material.LIGHT)) block.setType(Material.AIR);
            });
        } else {
            locations.forEach(loc -> {
                var block = loc.getBlock();

                if (block.getType().equals(Material.AIR)) block.setType(Material.LIGHT);
            });
        }
    }

    public void spawnFoodPlate() {
        if (foodPlate != null) removeFoodPlate();
        this.foodPlate = (ArmorStand) world.spawnEntity(purgeData().getFoodLocation(), EntityType.ARMOR_STAND);

        foodPlate.addDisabledSlots(EquipmentSlot.HEAD);
        foodPlate.setInvulnerable(true);
        this.foodPlate.getEquipment().setHelmet(CustomItems.Decoration.FOOD.get());
    }

    public void removeFoodPlate() {
        this.foodPlate.remove();
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public GameData gData() {
        return gManager.gameData();
    }

    public PurgeData purgeData() {
        return gData().purgeData();
    }

    public Location areaLower() {
        return purgeData().getAreaLower();
    }

    public Location areaUpper() {
        return purgeData().getAreaUpper();
    }

}
