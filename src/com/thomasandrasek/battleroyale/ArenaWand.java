package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/*
    Arena Wand acts as a selection tool for the player to select blocks for the arena to be created and managed in
    Left click with the wand to select the first position
    Right click with the wand to select the second position
 */

public class ArenaWand
{
    private static ArrayList<ArenaWand> arenaWands = new ArrayList<ArenaWand>();

    private Player player;
    private Location firstLocation;
    private Location secondLocation;

    /**
     * ArenaWand Object
     * @param player owner of the wand
     */
    public ArenaWand(Player player)
    {
        this.player = player;
        arenaWands.add(this);
    }

    /**
     * Returns the player who owns the wand
     * @return player who owns the wand
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Sets the first location
     * @param location first location
     */
    public void setFirstLocation(Location location)
    {
        this.firstLocation = location;
    }

    /**
     * Sets the second location
     * @param location second location
     */
    public void setSecondLocation(Location location)
    {
        this.secondLocation = location;
    }

    /**
     * Gets the first location stored by the wand
     * @return first location
     */
    public Location getFirstLocation()
    {
        return this.firstLocation;
    }

    /**
     * Gets the second location stored by the wand
     * @return second location
     */
    public Location getSecondLocation()
    {
        return this.secondLocation;
    }


    /**
     * Creates an arena wand and gives it to the user
     * @param player player to give wand to
     */
    public static void givePlayerWand(Player player)
    {
        ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = wand.getItemMeta();
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.LOYALTY, 3, true);
        meta.setDisplayName("BR WAND");
        wand.setItemMeta(meta);
        player.getInventory().addItem(wand);
    }

    /**
     * Gets the ArenaWand object from the owner of the wand
     * @param player owner of the wand
     * @return wand of the player
     */
    public static ArenaWand getArenaWand(Player player)
    {
        for(int i = 0; i < arenaWands.size(); i++)
        {
            if(arenaWands.get(i).getPlayer().equals(player))
            {
                return arenaWands.get(i);
            }
        }

        return null;
    }
}
