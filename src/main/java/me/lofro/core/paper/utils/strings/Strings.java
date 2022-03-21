package me.lofro.core.paper.utils.strings;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

public class Strings {

    /**
     * Function to translate the given text into ChatColor format.
     *
     * @param text to translate.
     * @return ChatColor format text.
     */
    public static String format(String text) {return ChatColor.translateAlternateColorCodes('&', text);}

    /**
     * Function to translate the given component text into ChatColor format.
     *
     * @param text to translate.
     * @return ChatColor format component.
     */
    public static Component componentFormat(String text) {return Component.text(ChatColor.translateAlternateColorCodes('&', text));}

}
