package com.thomasandrasek.battleroyale;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpawnPoint
{
    private Location spawn;
    private Player player;

    public static ArrayList<SpawnPoint> spawnPoints = new ArrayList<>();

    public SpawnPoint(Location location, Player player)
    {
        this.spawn = location;
        this.player = player;

        spawnPoints.add(this);
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public static Location getSpawnPoint(Player player)
    {
        for(int i = 0; i < spawnPoints.size(); i++)
        {
            if(spawnPoints.get(i).getPlayer().equals(player))
            {
                return spawnPoints.get(i).getSpawn();
            }
        }

        return null;
    }

    public static void replacePlayer(Player player)
    {
        for(int i = 0; i < spawnPoints.size(); i++)
        {
            if(spawnPoints.get(i).getPlayer().getDisplayName().equals(player.getDisplayName()))
            {
                spawnPoints.get(i).setPlayer(player);
            }
        }
    }

    public static SpawnPoint getSpawnPointFromName(Player player)
    {
        for(int i = 0; i < spawnPoints.size(); i++)
        {
            if(spawnPoints.get(i).getPlayer().getDisplayName().equals(player.getDisplayName()))
            {
                return spawnPoints.get(i);
            }
        }

        return null;
    }
}
