package me.vennlmao.cup;

import org.bukkit.plugin.java.JavaPlugin;

public class CupPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new MiningListener(this), this);
    }

    @Override
    public void onDisable() {
    }
}
