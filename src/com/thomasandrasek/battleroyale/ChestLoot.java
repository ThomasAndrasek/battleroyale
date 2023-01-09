package com.thomasandrasek.battleroyale;

import org.bukkit.Material;

/**
 * Information about loot that can naturally spawn in chests
 * The type of item that can spawn, and the amount that can spawn
 */

public class ChestLoot {
    private int max;
    private Material material;

    /**
     * Constructor for the ChestLoot object
     * @param material type of item
     * @param max max amount that can spawn
     */
    public ChestLoot(Material material, int max)
    {
        this.max = max;
        this.material = material;
    }

    /**
     * Gets the type of item
     * @return type of item
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * Gets the max amount of the item that can spawn
     * @return max amount of spawn in a single chest
     */
    public int getMax()
    {
        return this.max;
    }
}
