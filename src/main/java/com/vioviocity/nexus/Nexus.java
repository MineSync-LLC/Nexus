package com.vioviocity.nexus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Nexus extends JavaPlugin {
    
    private NexusCommands myExecutor;
    static Logger log = Logger.getLogger("Nexus");
    
    static FileConfiguration commandConfig = null;
    static File commandConfigFile = null;
    static FileConfiguration spawnConfig = null;
    static File spawnConfigFile = null;
    
    public void onDisable() {
        log.info(this + " is now disabled.");
    }

    public void onEnable() {
        // register events
        getServer().getPluginManager().registerEvents(new NexusPlayerListener(), this);
        
        // setup config files
        loadCommandConfig();
        saveCommandConfig();
        loadSpawnConfig();
        saveSpawnConfig();
        
        // register commands based on config
        myExecutor = new NexusCommands(this);
        if (commandConfig.getBoolean("nexus.commands.time"))
            getCommand("time").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.weather"))
        getCommand("weather").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.spawn"))
        getCommand("spawn").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.mode"))
        getCommand("mode").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.online"))
        getCommand("online").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.kick"))
        getCommand("kick").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.ban")) {
            getCommand("ban").setExecutor(myExecutor);
            getCommand("unban").setExecutor(myExecutor);
        }
        if (commandConfig.getBoolean("nexus.commands.t["))
        getCommand("tp").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.tpr"))
        getCommand("tpr").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.back"))
        getCommand("back").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.heal"))
        getCommand("heal").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.kill"))
        getCommand("kill").setExecutor(myExecutor);
        if (commandConfig.getBoolean("nexus.commands.level"))
        getCommand("level").setExecutor(myExecutor);
        
        // plugin enabled
        log.info(this + " is now enabled.");
    }
    
    public FileConfiguration loadCommandConfig() {
        if (commandConfig == null) {
            if (commandConfigFile == null)
                commandConfigFile = new File(this.getDataFolder(), "commands.yml");
            if (commandConfigFile.exists()) {
                commandConfig = YamlConfiguration.loadConfiguration(commandConfigFile);
            } else {
                InputStream defConfigStream = getResource("commands.yml");
                commandConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            }
        }
        return commandConfig;
    }
    
    public void saveCommandConfig() {
        if (commandConfig == null || commandConfigFile == null)
            return;
        try {
            commandConfig.save(commandConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save command config to " + commandConfigFile + ".");
        }
    }
    
    public FileConfiguration loadSpawnConfig() {
        if (spawnConfig == null) {
            if (spawnConfigFile == null)
                spawnConfigFile = new File(this.getDataFolder(), "spawn.yml");
            if (spawnConfigFile.exists()) {
                spawnConfig = YamlConfiguration.loadConfiguration(spawnConfigFile);
            } else {
                InputStream defConfigStream = getResource("spawn.yml");
                spawnConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                
                Location spawn = getServer().getWorld("world").getSpawnLocation();
                spawnConfig.set("nexus.spawn.world", spawn.getWorld().getName());
                spawnConfig.set("nexus.spawn.x", spawn.getX());
                spawnConfig.set("nexus.spawn.y", spawn.getWorld().getHighestBlockAt(spawn).getY());
                spawnConfig.set("nexus.spawn.z", spawn.getZ());
            }
        }
        return spawnConfig;
    }
    
    static public void saveSpawnConfig() {
        if (spawnConfig == null || spawnConfigFile == null)
            return;
        try {
            spawnConfig.save(spawnConfigFile);
        } catch (IOException e) {
            log.severe("Unable to save spawn config to " + spawnConfigFile + ".");
        }
    }
}