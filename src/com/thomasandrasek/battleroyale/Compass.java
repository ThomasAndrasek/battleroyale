package com.thomasandrasek.battleroyale;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/*
    Keeps track of the player tracking compasses used by the players
 */

public class Compass {
    private Player player;
    private int uses;

    public static ArrayList<Compass> compasses = new ArrayList<>();

    /**
     * Constructor for the player tracker object
     * @param player the owner of the compass
     */
    public Compass(Player player)
    {
        this.player = player;
        this.uses = 4;

        compasses.add(this);
    }

    /**
     * Gets the player who owns the compass
     * @return owner of the compass
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Gets the uses remaining on the compass
     * @return number of uses let on the compass
     */
    public int getUses()
    {
        return this.uses;
    }

    /**
     * Decreases the number of uses by 1
     */
    public void decrementUses()
    {
        this.uses--;
    }

    /**
     * Gets the compass based on the owner of the compass
     * @param player owner of the compass
     * @return compass of the owner
     */
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
