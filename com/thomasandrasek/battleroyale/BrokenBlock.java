package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BrokenBlock {
    public static ArrayList<BrokenBlock> brokenBlocks = new ArrayList<>();

    public static ArrayList<Material> breakableTypes = new ArrayList<>();

    private Location location;
    private Material material;

    public BrokenBlock(Location location, Material material)
    {
        this.location = location;
        this.material = material;

        brokenBlocks.add(this);
    }

    public Location getLocation()
    {
        return this.location;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public static void fillBreakableBlocks()
    {
        for(String material : Main.plugin.getConfig().getStringList("breakable-blocks"))
        {
            material = material.toUpperCase();
            Material blockToAdd = Material.getMaterial(material);
            if(blockToAdd != null)
            {
                breakableTypes.add(blockToAdd);
            }
        }
    }

    public static boolean isBreakable(Material material)
    {
        for(int i = 0; i < breakableTypes.size(); i++)
        {
            if(breakableTypes.get(i).equals(material))
                return true;
        }

        return false;
    }

    public static void breakBlock(Material material, Location location, BlockBreakEvent event)
    {
        if(isBreakable(material))
        {
            new BrokenBlock(location, material);
        }
        else
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot break that block.");
        }
    }

    public static void replaceBrokenBlocks()
    {
        for(int i = 0; i < brokenBlocks.size(); i++)
        {
            brokenBlocks.get(i).getLocation().getBlock().setType(brokenBlocks.get(i).getMaterial());
        }

        brokenBlocks.clear();
    }
}
