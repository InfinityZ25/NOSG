package me.lofro.game.global.item;

import com.google.common.collect.ImmutableMap;
import me.lofro.game.global.utils.Strings;
import me.lofro.game.global.utils.rapidinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CustomItems {

    public interface ItemStackGetter {
        ItemStack get();
    }

    private static <T extends Enum<? extends ItemStackGetter> & ItemStackGetter> ImmutableMap<String, ImmutableMap<String, ItemStack>> convertIntoItemStacks(@SuppressWarnings("rawtypes") Class... groups) {
        HashMap<String, ImmutableMap<String, ItemStack>> tempGroups = new HashMap<>(0, 1);
        for (@SuppressWarnings("unchecked") Class<T> group : groups) {
            HashMap<String, ItemStack> tempDirectory = new HashMap<>(0, 1);

            T[] values = group.getEnumConstants();
            for (T value : values) {
                tempDirectory.put(value.name(), value.get());
            }

            tempGroups.put(group.getSimpleName(), ImmutableMap.copyOf(tempDirectory));
        }
        return ImmutableMap.copyOf(tempGroups);
    }
    public static final ImmutableMap<String, ImmutableMap<String, ItemStack>> groups = convertIntoItemStacks(
            Weapons.class
    );

    public enum Weapons implements ItemStackGetter {
        SHOTGUN(new ItemBuilder(Material.CROSSBOW)
                .name(Strings.format("&cShotgun"))
                .setUnbreakable(true)
                .enchant(Enchantment.QUICK_CHARGE)
                .flags(ItemFlag.HIDE_ENCHANTS)
                .setCustomModelData(0)
                .build()),

        REVOLVER(new ItemBuilder(Material.CROSSBOW)
                .name(Strings.format("&eRevolver"))
                .setUnbreakable(true)
                .enchant(Enchantment.QUICK_CHARGE, 2)
                .flags(ItemFlag.HIDE_ENCHANTS)
                .setCustomModelData(1)
                .build());

        private final ItemStack itemStack;
        public ItemStack get() {
            return itemStack;
        }
        Weapons(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }

}
