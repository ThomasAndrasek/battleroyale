package com.thomasandrasek.battleroyale;

/*
Manages the heads that are placed on the ground when players die.
Can be right clicked once for a health and speed boost.
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Head {

    private Player deadPlayer;
    private boolean clickedOn;
    private Location headLocation;

    private static ArrayList<Head> heads = new ArrayList<>();

    /**
     * Creates a new Head object
     * @param deadPlayer the player who died
     */
    public Head(Player deadPlayer) {
        this.deadPlayer = deadPlayer;
        this.clickedOn = false;

        heads.add(this);
    }

    /**
     * Finds the ground block where to place the head
     * @param deadPlayer the player who died
     * @return location to place head
     */
    private static Location findGround(Player deadPlayer) {
        Location playerLocation = deadPlayer.getLocation();
        Location tempLocation = new Location(playerLocation.getWorld(),
                playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());

        // Go through Y levels until the ground is found
        while(tempLocation.getBlock().getType().equals(Material.AIR)) {
            tempLocation.setY(tempLocation.getY()+1);
        }

        // Set the level up 1 for the air block above the ground
        tempLocation.setY(tempLocation.getY()-1);

        return tempLocation;
    }
}
