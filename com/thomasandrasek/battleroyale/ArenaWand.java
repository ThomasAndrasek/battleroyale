package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ArenaWand
{
    private static ArrayList<ArenaWand> arenaWands = new ArrayList<ArenaWand>();

    private Player player;
    private Location firstLocation;
    private Location secondLocation;

    public ArenaWand(Player player)
    {
        this.player = player;
        arenaWands.add(this);
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public void setFirstLocation(Location location)
    {
        this.firstLocation = location;
    }

    public void setSecondLocation(Location location)
    {
        this.secondLocation = location;
    }

    public Location getFirstLocation()
    {
        return this.firstLocation;
    }

    public Location getSecondLocation()
    {
        return this.secondLocation;
    }


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
