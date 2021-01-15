package com.gmail.bradbouquio.AutoVillageProtect;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AutoVillageProtect extends JavaPlugin {

    public static AutoVillageProtect plugin;


    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("avp").setTabCompleter(new CommandCompleter());
        this.getCommand("avp").setExecutor(new MainCommand());
        this.loadConfiguration();
        this.getLogger().info("AutoVillageProtect Enabled");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("AutoVillageProtect Disabled.");
    }

    public void loadConfiguration() {
        File pluginFolder = this.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }
}
