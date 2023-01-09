package com.thomasandrasek.battleroyale;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/*
    Manages all the config files
 */

public class ConfigManager {

    private Main plugin = Main.getPlugin(Main.class);

    // Files & File Configs Here
    public FileConfiguration arenaConfig;
    public File arenaFile;
    public FileConfiguration arenaChestConfig;
    public File arenaChestFile;
    public FileConfiguration lobbyConfig;
    public File lobbyFile;
    // --------------------------

    /**
     * Sets up the config files
     */
    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        arenaFile = new File(plugin.getDataFolder(), "arena.yml");
        arenaChestFile = new File(plugin.getDataFolder(), "arenachest.yml");
        lobbyFile = new File(plugin.getDataFolder(), "lobby.yml");

        if (!arenaFile.exists() || (!arenaChestFile.exists())) {
            try {
                arenaFile.createNewFile();
                arenaChestFile.createNewFile();
                lobbyFile.createNewFile();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The arena.yml file has been created");
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The arenachest.yml file has been created");
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The lobby.yml file has been created");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the arena.yml file");
            }
        }

        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        arenaConfig.options().copyDefaults();
        saveArena();
        arenaChestConfig = YamlConfiguration.loadConfiguration(arenaChestFile);
        arenaChestConfig.options().copyDefaults();
        saveArenaChest();
        lobbyConfig = YamlConfiguration.loadConfiguration(lobbyFile);
        lobbyConfig.options().copyDefaults();
        saveLobby();
    }

    /**
     * Gets the arena config file
     * @return the arena config file
     */
    public FileConfiguration getArena() {
        return arenaConfig;
    }

    /**
     * Gets the arena chest config file
     * @return arena chest config file
     */
    public FileConfiguration getArenaChest() { return arenaChestConfig; }

    /**
     * Gets the lobby config file
     * @return lobby config file
     */
    public FileConfiguration getLobby() { return lobbyConfig; }

    /**
     * Saves the arena file
     */
    public void saveArena() {
        try {
            arenaConfig.save(arenaFile);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The arena.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the arena.yml file");
        }
    }

    /**
     * Saves the arena chest file
     */
    public void saveArenaChest() {
        try {
            arenaChestConfig.save(arenaChestFile);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The arenachest.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the arenachest.yml file");
        }
    }

    /**
     * Saves the lobby config file
     */
    public void saveLobby() {
        try {
            lobbyConfig.save(lobbyFile);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The lobby.yml file has been saved");

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the lobby.yml file");
        }
    }

    /**
     * Reloads the arena file
     */
    public void reloadArena() {
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The arena.yml file has been reload");

    }

    /**
     * Reloads the arena chest file
     */
    public void reloadArenaChest() {
        arenaChestConfig = YamlConfiguration.loadConfiguration(arenaChestFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The arenachest.yml file has been reload");

    }

    /**
     * Reloads the lobby config file
     */
    public void reloadLobby() {
        lobbyConfig = YamlConfiguration.loadConfiguration(lobbyFile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The lobby.yml file has been reload");

    }
}