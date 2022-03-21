package me.lofro.core.paper.utils.strings;

import org.bukkit.ChatColor;

public class Strings {

    /**
     * Function to translate the given text into ChatColor format.
     *
     * @param text to translate.
     */
    public static String format(String text) {return ChatColor.translateAlternateColorCodes('&', text);}

}
