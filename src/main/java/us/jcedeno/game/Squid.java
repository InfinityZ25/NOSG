package us.jcedeno.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.lofro.core.paper.data.LocationSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Squid extends JavaPlugin {

    private @Getter static Squid instance;

    private final @Getter static MiniMessage miniMessage = MiniMessage.miniMessage();
    private @Getter static final Gson gson = new GsonBuilder()
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

}
