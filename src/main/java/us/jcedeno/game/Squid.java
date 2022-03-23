package us.jcedeno.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.lofro.core.paper.data.LocationSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Entrypoint for Not Otaku SquidGame plugin.
 * 
 * 
 * @author <a href="https://github.com/zLofro">lofro</a> - Developer.
 * 
 * @author <a href="https://jcedeno.us/github">jcedeno</a> - Consultant &
 *         Collaborator.
 */
public class Squid extends JavaPlugin {
    private @Getter static Squid instance;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    /**  GSON instance with custom serializers & config */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {

    }

    /**
     * A static method to aceess the Gson object globally.
     * 
     * @return gson with all adapter types registered.
     */
    public static Gson gson() {
        return gson;
    }

    /**
     * A static method to access the MiniMessage object globally.
     * 
     * @return MiniMessage object.
     */
    public static MiniMessage miniMessage() {
        return miniMessage;
    }

}
