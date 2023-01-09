package com.thomasandrasek.battleroyale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    public static ConfigManager configManager;
    public static Main plugin;

    @Override
    public void onEnable()
    {
        loadConfigManager();
        new PListener(this);
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        ChestManager.fillTiers();
        BrokenBlock.fillBreakableBlocks();
        PlacedBlock.fillPlaceableBlocks();
        SupplyDropManager.fillSupplyDropItems();
        LlamaManager.fillLlamaItems();
        //LobbyManager.manageLobby();
    }

    public void loadConfigManager()
    {
        configManager = new ConfigManager();
        configManager.setup();
    }

    @Override
    public void onDisable()
    {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        return CommandExecution.executeCommand(sender, cmd, label, args);
    }
}
