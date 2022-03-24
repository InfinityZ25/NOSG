package us.jcedeno.game.global.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

public class Strings {

    public static String prefix = Strings.format("&f&lSquid&d&lOtaku&f&lGame &7>> &r");

    /**
     * Function to translate the given text into ChatColor format.
     *
     * @param text to translate.
     * @return ChatColor format string.
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
