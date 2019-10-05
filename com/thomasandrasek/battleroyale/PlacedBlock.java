package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class PlacedBlock {
    public static ArrayList<PlacedBlock> placedBlocks = new ArrayList<>();

    public static ArrayList<Material> placeableTypes = new ArrayList<>();

    private Location location;
    private Material material;

    public PlacedBlock(Location location, Material material)
    {
        this.location = location;
        this.material = material;

        placedBlocks.add(this);
    }

    public Location getLocation()
    {
        return this.location;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public static void fillPlaceableBlocks()
    {
        for(String material : Main.plugin.getConfig().getStringList("placeable-blocks"))
        {
            material = material.toUpperCase();
            Material blockToAdd = Material.getMaterial(material);
            if(blockToAdd != null)
            {
                placeableTypes.add(blockToAdd);
            }
        }
    }

    public static boolean isPlaceable(Material material)
    {
        for(int i = 0; i < placeableTypes.size(); i++)
        {
            if(placeableTypes.get(i).equals(material))
                return true;
        }

        return false;
    }

    public static void placeBlock(Material material, Location location, BlockPlaceEvent event)
    {
        if(isPlaceable(material))
        {
            new PlacedBlock(location, material);
        }
        else
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot place that block.");
        }
    }

    public static void replacePlacedBlocks()
    {
        for(int i = 0; i < placedBlocks.size(); i++)
        {
            placedBlocks.get(i).getLocation().getBlock().setType(Material.AIR);
        }

        placedBlocks.clear();
    }
}
