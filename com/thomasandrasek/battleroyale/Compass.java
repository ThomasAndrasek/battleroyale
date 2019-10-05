package com.thomasandrasek.battleroyale;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Compass {
    private Player player;
    private int uses;

    public static ArrayList<Compass> compasses = new ArrayList<>();

    public Compass(Player player)
    {
        this.player = player;
        this.uses = 4;

        compasses.add(this);
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public int getUses()
    {
        return this.uses;
    }

    public void decrementUses()
    {
        this.uses--;
    }

    public static Compass getCompass(Player player)
    {
        for(int i = 0; i < compasses.size(); i++)
        {
            if(compasses.get(i).getPlayer().equals(player))
            {
                return compasses.get(i);
            }
        }

        return null;
    }
}
