package me.lofro.game.global.item;

import com.google.common.collect.ImmutableMap;
import me.lofro.game.global.utils.Strings;
import me.lofro.game.global.utils.rapidinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

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
            Weapons.class, Decoration.class
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
                .build())
        ,BROKEN_BOTTLE(new ItemBuilder(Material.STONE_SWORD)
                .name(Strings.format("&2Botella rota"))
                .setUnbreakable(true)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_UNBREAKABLE)
                .setCustomModelData(1)
                .build()
        ),KNIFE(new ItemBuilder(Material.STONE_SWORD)
                .name(Strings.format("&cCuchillo"))
                .setUnbreakable(true)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_UNBREAKABLE)
                .setCustomModelData(2)
                .build()
        );

        private final ItemStack itemStack;
        public ItemStack get() {
            return itemStack;
        }
        Weapons(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }

    public enum Decoration implements ItemStackGetter {

        KORO_SENSEI(new ItemBuilder(Material.BRICK)
                .name(Strings.format("&6Koro Sensei"))
                .setCustomModelData(5)
                .build()
        ),
        GUARD_MASK(new ItemBuilder(Material.BRICK)
                .name(Strings.format("&cCabeza de Guardia"))
                .setCustomModelData(6)
                .build()
        ),PLAYER_CHESTPLATE(new ItemBuilder(Material.GOLDEN_CHESTPLATE)
                .name(Strings.format("&bCamisa"))
                .setUnbreakable(true)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_UNBREAKABLE)
                .addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.CHEST))
                .build()
        ),PLAYER_LEGGINGS(new ItemBuilder(Material.GOLDEN_LEGGINGS)
                .name(Strings.format("&bPantalón"))
                .setUnbreakable(true)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .flags(ItemFlag.HIDE_UNBREAKABLE)
                .addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.LEGS))
                .build()
        ),EMPTY_FOOD(new ItemBuilder(Material.BRICK)
                .setCustomModelData(7)
                .build()
        ),FOOD(new ItemBuilder(Material.BRICK)
                .setCustomModelData(8)
                .build()
        ),DEATH_NOTE(new ItemBuilder(Material.BOOK)
                .name(Strings.format("&8Death Note"))
                .build()
        ),BOTTLE(new ItemBuilder(Material.GLASS_BOTTLE)
                .name(Strings.format("&2Botella"))
                .build()
        ),TNT(new ItemBuilder(Material.TNT)
                .name(Strings.format("&cTAG"))
                .build()
        );

        private final ItemStack itemStack;
        public ItemStack get() {
            return itemStack;
        }
        Decoration(ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }

}
