package com.thomasandrasek.battleroyale;

import com.thomasandrasek.teampoints.Team;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/*

Arena Manger - General manager for running the arenas

Last updated: October 4, 2019

 */

public class ArenaManager {
    private Main plugin = Main.getPlugin(Main.class);

    public static boolean arenaRunning = false;

    public static Location supplyDropLocation = null;

    /*
    Void function to run an Arena, takes the inputs for the world border and the winning arena.
     */
    public static void runArena(Vote winner, WorldBorder border)
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            int warmupTime = Main.plugin.getConfig().getInt("warmup-time") * 4;
            int refillChestTime = Main.plugin.getConfig().getInt("refill-chest-time");
            int supplyDropTime = Main.plugin.getConfig().getInt("supply-drop-time");
            int deathmatchTime = Main.plugin.getConfig().getInt("deathmatch-time") * 4;
            int loopCount = 0;
            int borderShrinktime = 120;
            int teleportTime = 20;
            int sendToLobbyTime = 40;
            double supplyDropY;
            double groundLevel;

            Location spawnDrop;

            boolean firstWinAnnounce = true;

            @Override
            public void run() {
                if(teleportTime <= 0)
                {
                    if(teleportTime == 0)
                    {
                        ArenaManager.teleportPlayersToArena(winner);
                        teleportTime--;
                    }

                    if(warmupTime == 0)
                    {
                        if(loopCount == 0)
                        {
                            /*
                            For every player in the arena:
                                Sent a title saying the game has started
                                Play a sound
                                Set their health to full
                                Set their hunger to full
                                Remove their spawnpoint if they are offline
                             */
                            arenaRunning = true;
                            for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                            {
                                SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&cGAME HAS STARTED"),
                                        ChatColor.translateAlternateColorCodes('&', "&6GOOD LUCK!"),
                                        0,
                                        40,
                                        20);
                                SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f,  1);
                                SpawnPoint.spawnPoints.get(i).getPlayer().setHealth(20);
                                SpawnPoint.spawnPoints.get(i).getPlayer().setFoodLevel(20);

                                if(!(SpawnPoint.spawnPoints.get(i).getPlayer().isOnline()))
                                {
                                    SpawnPoint.spawnPoints.remove(i);
                                    i--;
                                }
                            }

                            LlamaManager.spawnLlamas(winner);
                        }
                        loopCount++;

                        if(loopCount % 4 == 0)
                        {
                            if(refillChestTime == 417)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().setInvulnerable(false);
                                }
                            }

                            if(refillChestTime % 60 == 0 &&  refillChestTime != 0)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                                }

                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lCHESTS WILL RESTOCK IN " + (refillChestTime / 60) + " MINUTE(S)!"));
                            }

                            if(refillChestTime == 30)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                                }
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lCHESTS WILL RESTOCK IN 30 SECONDS!"));
                            }

                            if(refillChestTime == 10)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                                }
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lCHESTS WILL RESTOCK IN 10 SECONDS!"));
                            }

                            if(refillChestTime == 0)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&6CHESTS RESTOCKED!"),
                                            "",
                                            20,
                                            40,
                                            20);
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
                                }
                                ChestManager.clearChests(winner);
                                ChestManager.fillChests(winner);
                                refillChestTime--;
                            }

                            if(refillChestTime <= 5 && refillChestTime > 0)
                            {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lCHESTS RESTOCK IN " + refillChestTime + " SECONDS!"));

                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                                }
                            }

                            if(refillChestTime != -1)
                                refillChestTime--;

                            borderShrinktime--;

                            if(borderShrinktime == 30)
                            {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&lBORDER SHRINKING IN 30 SECONDS!"));
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                }
                            }

                            if(borderShrinktime == 10)
                            {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&lBORDER SHRINKING IN 10 SECONDS!"));
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                }
                            }

                            if(borderShrinktime <= 5 && borderShrinktime > 0)
                            {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4&lBORDER SHRINKING IN " + borderShrinktime + " SECONDS!"));

                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                                }
                            }

                            if(borderShrinktime == 0)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&4BORDER IS SHRINKING!"),
                                            "",
                                            20,
                                            40,
                                            20);

                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.5f, 1);
                                }

                                border.setSize(border.getSize() * 3/4, 20);
                                borderShrinktime = 120;
                            }

                            if(supplyDropTime == 0)
                            {
                                spawnDrop = SupplyDropManager.supplyDropLocation(winner);
                                supplyDropLocation = spawnDrop;
                                groundLevel = spawnDrop.getY();
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aSupply drop is falling from the sky! &e" + spawnDrop.getX() + " " + spawnDrop.getY() + " " + spawnDrop.getZ()));
                                supplyDropY = SupplyDropManager.fall(spawnDrop, 254, spawnDrop.getY());
                            }

                            if(supplyDropTime != -1)
                                supplyDropTime--;
                        }

                        if(supplyDropTime == -1)
                        {
                            if(supplyDropLocation != null)
                            {
                                if(supplyDropY != -1)
                                    supplyDropY = SupplyDropManager.fall(spawnDrop, spawnDrop.getY(), groundLevel);
                            }
                        }

                        if(deathmatchTime != -1)
                            deathmatchTime--;

                        ArrayList<Team> tempTeamList = new ArrayList<>();
                        for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                        {
                            Team team = Team.getTeamFromPlayer(SpawnPoint.spawnPoints.get(i).getPlayer());

                            if(team != null)
                            {
                                boolean teamInList = false;

                                for(int j = 0; j < tempTeamList.size(); j++)
                                {
                                    if(tempTeamList.get(j).equals(team))
                                        teamInList = true;
                                }
                                if(!teamInList)
                                    tempTeamList.add(team);
                            }
                        }

                        if(tempTeamList.size() == 1)
                        {
                            arenaRunning = false;

                            if(firstWinAnnounce)
                            {
                                for(int i = 0; i < Team.teams.size(); i++)
                                {
                                    for(int j = 0; j < Team.teams.get(i).getTeamMembers().size(); j++)
                                    {
                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().sendTitle(
                                                ChatColor.translateAlternateColorCodes('&', "&c&lVICTORY ROYALE!"),
                                                ChatColor.translateAlternateColorCodes('&', "&6&l" + tempTeamList.get(0).getTeamMembers().get(0).getPlayer().getDisplayName() + " & " + tempTeamList.get(0).getTeamMembers().get(1).getPlayer().getDisplayName()),
                                                0,
                                                200,
                                                80
                                        );

                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().getInventory().clear();
                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().setLevel(0);
                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().setTotalExperience(0);
                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().setExp(0);

                                        Compass.compasses.clear();
                                        LlamaManager.killLlamas();

                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().playSound(Team.teams.get(i).getTeamMembers().get(j).getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.5f, 1);
                                    }
                                }

                                if(supplyDropLocation != null)
                                {
                                    supplyDropLocation.getWorld().setThundering(false);
                                    supplyDropLocation.getWorld().setStorm(false);

                                    supplyDropLocation.getBlock().setType(Material.AIR);
                                }

                                firstWinAnnounce = false;
                            }


                            //LobbyManager.manageLobby();

                            if(sendToLobbyTime == 0)
                            {
                                Location spawnLocation = new Location(Bukkit.getWorld("lobby"), 0.5, 142, 0.5);
                                for(int i = 0; i < Team.teams.size(); i++) {
                                    for (int j = 0; j < Team.teams.get(i).getTeamMembers().size(); j++) {
                                        Team.teams.get(i).getTeamMembers().get(j).getPlayer().teleport(spawnLocation);
                                    }
                                }

                                SpawnPoint.spawnPoints.clear();
                                //Vote.votes.clear();
                                BrokenBlock.replaceBrokenBlocks();
                                PlacedBlock.replacePlacedBlocks();
                                Bukkit.getScheduler().cancelTasks(Main.plugin);
                                arenaRunning = false;
                            }

                            sendToLobbyTime--;
                        }
                    }
                    else
                    {

                        keepStill();
                        if(warmupTime % 4 == 0)
                        {
                            if(warmupTime / 4 == 30)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&cStarting in &630 &cseconds!"),
                                            "",
                                            0,
                                            40,
                                            20);
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                }
                            }

                            if(warmupTime / 4 == 10)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&cStarting in &610 &cseconds!"),
                                            "",
                                            0,
                                            40,
                                            20);
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                }
                            }

                            if(warmupTime / 4 <= 5 && warmupTime / 4 != 0)
                            {
                                for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
                                {
                                    SpawnPoint.spawnPoints.get(i).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&cStarting in &6" + warmupTime/4 + " &cseconds!"),
                                            "",
                                            0,
                                            20,
                                            0);
                                    SpawnPoint.spawnPoints.get(i).getPlayer().playSound(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                                }
                            }
                        }

                        warmupTime--;
                    }
                }
                else
                {
                    if(teleportTime % 4 == 0)
                    {
                        for(int i = 0; i < Team.teams.size(); i++) {
                            for (int j = 0; j < Team.teams.get(i).getTeamMembers().size(); j++)
                            {
                                Team.teams.get(i).getTeamMembers().get(j).getPlayer().playSound(Team.teams.get(i).getTeamMembers().get(j).getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                                Team.teams.get(i).getTeamMembers().get(j).getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', "&cTeleport in &6" + (teleportTime/4) + " &cseconds!"),
                                        "",
                                        0,
                                        20,
                                        0);
                            }
                        }
                    }

                    teleportTime--;
                }
            }
        }, 0, 5);
    }

    public static void startArena(String args[])
    {
        Vote winner = new Vote("doesn't matter", Integer.parseInt(args[0]));
        ArenaManager.loadArena(winner);
        //ArenaManager.teleportPlayersToArena(winner);
        WorldBorder border = ArenaManager.setInitialBorder(winner);
        ArenaManager.runArena(winner, border);
    }

    public static WorldBorder setInitialBorder(Vote winner)
    {
        Location center = getMapCenter(winner);

        Location min = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".min-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".min-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".min-location.z")
        );

        Location max = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".max-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".max-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".max-location.z")
        );

        WorldBorder border = center.getWorld().getWorldBorder();
        border.setCenter(center);

        double size = Math.max(max.getX() - min.getX(), max.getZ() - min.getZ());

        border.setSize(size);
        border.setDamageAmount(2);

        return border;
    }

    public static Location getMapCenter(Vote winner)
    {
        Location center = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.z")
        );

        return center;
    }

    public static void keepStill()
    {
        for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
        {
            Player player = SpawnPoint.spawnPoints.get(i).getPlayer();
            Location loc = player.getLocation();
            Location spawnLoc = SpawnPoint.spawnPoints.get(i).getSpawn();

            if((loc.getX() >  spawnLoc.getX() + 1) || (loc.getX() < spawnLoc.getX() - 1) ||
                    (loc.getZ() > spawnLoc.getZ() + 1) || (loc.getZ() < spawnLoc.getZ() - 1)
                    || (loc.getY() < spawnLoc.getY() - 1)) {

                player.teleport(spawnLoc);

            }
        }
    }

    public static void createArena(Player player, String arenaName)
    {
        ArenaWand wand = ArenaWand.getArenaWand(player);
        if(wand != null)
        {
            if((wand.getFirstLocation() != null) && (wand.getSecondLocation() != null))
            {
                Location minLocation, maxLocation, firstLocation, secondLocation;
                minLocation = null;
                maxLocation = null;
                firstLocation = wand.getFirstLocation();
                secondLocation = wand.getSecondLocation();
                double minX, minY, minZ;
                double maxX, maxY, maxZ;

                if(firstLocation.getX() >= secondLocation.getX())
                {
                    maxX = firstLocation.getX();
                    minX = secondLocation.getX();
                }
                else
                {
                    maxX = secondLocation.getX();
                    minX = firstLocation.getX();
                }
                if(firstLocation.getY() >= secondLocation.getY())
                {
                    maxY = firstLocation.getY();
                    minY = secondLocation.getY();
                }
                else
                {
                    maxY = secondLocation.getY();
                    minY = firstLocation.getY();
                }
                if(firstLocation.getZ() >= secondLocation.getZ())
                {
                    maxZ = firstLocation.getZ();
                    minZ = secondLocation.getZ();
                }
                else
                {
                    maxZ = secondLocation.getZ();
                    minZ = firstLocation.getZ();
                }

                minLocation = new Location(firstLocation.getWorld(), minX, minY, minZ);
                maxLocation = new Location(firstLocation.getWorld(), maxX, maxY, maxZ);

                if((minLocation != null) && (maxLocation != null))
                {
                    Bukkit.broadcastMessage("Min location: " + minLocation);
                    Bukkit.broadcastMessage("Max location: " + maxLocation);
                }

                Main.configManager.getArena().set("arena-count", Main.configManager.getArena().getInt("arena-count") + 1);
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".name", arenaName);
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".world-name", minLocation.getWorld().getName());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.x", minLocation.getX());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.y", minLocation.getY());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.z", minLocation.getZ());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.x", maxLocation.getX());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.y", maxLocation.getY());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.z", maxLocation.getZ());
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".mid-location.x", Math.round((maxLocation.getX() + minLocation.getX())/2));
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".mid-location.y", Math.round((maxLocation.getY() + minLocation.getY())/2));
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".mid-location.z", Math.round((maxLocation.getZ() + minLocation.getZ())/2));
                Main.configManager.getArena().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".spawn.spawn-count", 0);
                Main.configManager.saveArena();

                ChestManager.markChests(arenaName);

            }
        }
    }

    public static void loadArena(Vote winner)
    {
        ChestManager.clearChests(winner);
        ChestManager.fillChests(winner);
    }

    public static void setMapCenter(Player player)
    {
        int arenaID = getArenaByWorld(player);

        if(arenaID != -1)
        {
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".mid-location.x", player.getLocation().getX());
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".mid-location.y", player.getLocation().getY());
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".mid-location.z", player.getLocation().getZ());
            Main.configManager.saveArena();
            player.sendMessage("Mid location set.");
        }
        else
        {
            player.sendMessage("You are not in an arena.");
        }
    }

    public static int getArenaByWorld(Player player)
    {
        for(int i = 1; i <= Main.configManager.getArena().getInt("arena-count"); i++)
        {
            if(Main.configManager.getArena().getString("arena.arena-" + i + ".world-name").equalsIgnoreCase(player.getWorld().getName()))
            {
                return i;
            }
        }

        return -1;
    }

    public static void setNextSpawn(Player player)
    {
        int arenaID = getArenaByWorld(player);

        if(arenaID != -1)
        {
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".spawn.spawn-count", Main.configManager.getArena().getInt("arena.arena-" + arenaID + ".spawn.spawn-count") + 1);
            int spawnNumber = Main.configManager.getArena().getInt("arena.arena-" + arenaID + ".spawn.spawn-count");
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".spawn.spawn-" + spawnNumber + ".x", player.getLocation().getX());
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".spawn.spawn-" + spawnNumber + ".y", player.getLocation().getY());
            Main.configManager.getArena().set("arena.arena-" + arenaID + ".spawn.spawn-" + spawnNumber + ".z", player.getLocation().getZ());
            Main.configManager.saveArena();
            player.sendMessage("Spawn set");
        }
        else
        {
            player.sendMessage("You are not in an arena.");
        }
    }

    public static void deathMatchTeleport()
    {
        for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
        {
            SpawnPoint.spawnPoints.get(i).getPlayer().teleport(SpawnPoint.spawnPoints.get(i).getSpawn());
        }
    }

    public static void teleportPlayersToArena(Vote winner)
    {
        int s = 1;

        Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
        Object[] temp = Bukkit.getOnlinePlayers().toArray();

        for(int i = 0; i < Bukkit.getOnlinePlayers().size(); i++)
        {
            players[i] = (Player) temp[i];
        }

        for(int i = 0; i < Team.teams.size(); i++)
        {
            for(int j = 0; j < Team.teams.get(i).getTeamMembers().size(); j++)
            {
                Team.teams.get(i).getTeamMembers().get(j).getPlayer().setInvulnerable(true);
            }
            int p = 0;
            while(p < Team.teams.get(i).getTeamMembers().size())
            {
                if(s <= Main.configManager.getArena().getInt("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-count"))
                {
                    Location spawn = new Location(
                            Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                            Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".x"),
                            Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".y"),
                            Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".z")
                    );

                    Team.teams.get(i).getTeamMembers().get(p).getPlayer().teleport(spawn);
                    new SpawnPoint(spawn, Team.teams.get(i).getTeamMembers().get(p).getPlayer());
                    if(Team.teams.get(i).getTeamMembers().get(0).getPlayer().equals(Team.teams.get(i).getTeamMembers().get(1).getPlayer()))
                    {
                        p++;
                    }
                    s++;
                    p++;
                }
                else
                {
                    s = 1;
                }
            }
        }
        /*while(p < players.length)
        {
            if(s <= Main.configManager.getArena().getInt("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-count"))
            {
                Location spawn = new Location(
                        Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                        Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".x"),
                        Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".y"),
                        Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".spawn.spawn-" + s + ".z")
                );

                players[p].teleport(spawn);
                new SpawnPoint(spawn, players[p]);
                s++;
                p++;
            }
            else
            {
                s = 1;
            }
        }*/
    }

}
