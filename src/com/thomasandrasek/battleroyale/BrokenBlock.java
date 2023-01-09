package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

/*
    When players break blocks in the arena the block information is stored here
    When the BattleRoyale game is over, all broken blocks are replaced with their original block
 */

public class BrokenBlock {
    public static ArrayList<BrokenBlock> brokenBlocks = new ArrayList<>();

    public static ArrayList<Material> breakableTypes = new ArrayList<>();

    private Location location;
    private Material material;

    /**
     * Constructor for the BrokenBlock object
     * @param location location at which the block was broken
     * @param material material of which the broken block is made out of
     */
    public BrokenBlock(Location location, Material material)
    {
        this.location = location;
        this.material = material;

        brokenBlocks.add(this);
    }

    /**
     * Gets the location at which the block was broken
     * @return the location of the broken blockj
     */
    public Location getLocation()
    {
        return this.location;
    }

    /**
     * Gets the material of the broken block
     * @return material of broken block
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * Fixes all of the broken blocks in the arena
     */
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

    /**
     * Determines whether or not the broken is breakable
     * @param material type of block to see if breakable
     * @return whether or not the block is breakable
     */
    public static boolean isBreakable(Material material)
    {
        for(int i = 0; i < breakableTypes.size(); i++)
        {
            if(breakableTypes.get(i).equals(material))
                return true;
        }

        return false;
    }

    /**
     * Breaks the block the user is breaking, denies user if block is not breakable
     * @param material material of block that is trying to be broken
     * @param location location of block trying to be broken
     * @param event the block break event
     */
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

    /**
     * Replaces all the blocks that have been broken with the original block
     */
    public static void replaceBrokenBlocks()
    {
        for(int i = 0; i < brokenBlocks.size(); i++)
        {
            brokenBlocks.get(i).getLocation().getBlock().setType(brokenBlocks.get(i).getMaterial());
        }

        brokenBlocks.clear();
    }
}
