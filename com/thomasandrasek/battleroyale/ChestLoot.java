package com.thomasandrasek.battleroyale;

import org.bukkit.Material;

public class ChestLoot {
    private int max;
    private Material material;

    public ChestLoot(Material material, int max)
    {
        this.max = max;
        this.material = material;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public int getMax()
    {
        return this.max;
    }
}
