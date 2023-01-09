package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

/*
    Manages all of the blocks that get placed in the arena
    Removes all of the placed blocks when the arena match is over
 */

public class PlacedBlock {
    public static ArrayList<PlacedBlock> placedBlocks = new ArrayList<>();

    public static ArrayList<Material> placeableTypes = new ArrayList<>();

    private Location location;
    private Material material;

    /**
     * Constructor for the blocks that are placed in the arena
     * @param location location at which the block is placed
     * @param material the type of block that is placed
     */
    public PlacedBlock(Location location, Material material)
    {
        this.location = location;
        this.material = material;

        placedBlocks.add(this);
    }

    /**
     * Gets the location of the placed block
     * @return location of placed block
     */
    public Location getLocation()
    {
        return this.location;
    }

    /**
     * Gets the material of the block that was placed
     * @return material of placed block
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * Fills the list of placeable blocks form the custom list
     */
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

    /**
     * Determines whether or not the block is placeable based off the custom choices in the config
     * @param material the material of the block that is trying to be placed
     * @return whether or not the block is placeable
     */
    public static boolean isPlaceable(Material material)
    {
        for(int i = 0; i < placeableTypes.size(); i++)
        {
            if(placeableTypes.get(i).equals(material))
                return true;
        }

        return false;
    }

    /**
     * Places the block in the arena
     * @param material the material of the block being placed
     * @param location the location of the block being placed
     * @param event the event of trying to place the block
     */
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

    /**
     * Replaces all the placed blocks in the arena with an air block
     */
    public static void replacePlacedBlocks()
    {
        for(int i = 0; i < placedBlocks.size(); i++)
        {
            placedBlocks.get(i).getLocation().getBlock().setType(Material.AIR);
        }

        placedBlocks.clear();
    }
}
