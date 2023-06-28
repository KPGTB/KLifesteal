package pl.kpgtb.klifesteal;

import com.github.kpgtb.ktools.util.bstats.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class KLifesteal extends JavaPlugin {

    @Override
    public void onEnable() {

        new Metrics(this, 18903);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
