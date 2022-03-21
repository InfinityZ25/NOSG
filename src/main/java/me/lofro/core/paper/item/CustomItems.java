package me.lofro.core.paper.item;

import com.google.common.collect.ImmutableMap;
import me.lofro.core.paper.utils.rapidinv.ItemBuilder;
import me.lofro.core.paper.utils.strings.Strings;
import org.bukkit.Material;
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
                .name(Strings.format("&3Shotgun"))
                .setUnbreakable(true)
                .setCustomModelData(2000)
                .build()),

        REVOLVER(new ItemBuilder(Material.CROSSBOW)
                .name(Strings.format("&3Revolver"))
                .setUnbreakable(true)
                .setCustomModelData(2001)
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
