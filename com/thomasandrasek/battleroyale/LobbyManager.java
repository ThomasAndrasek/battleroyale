package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class LobbyManager {

    public static void manageLobby()
    {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            int previousAmountOfPlayers = 0;
            boolean firstLoop = true;
            boolean winnerAnnounce = false;

            int minPlayerTimer = 30;
            int teleportTimer = 3;

            Vote winner;

            @Override
            public void run() {

                if(firstLoop)
                {
                    Vote.setup();
                    firstLoop = false;
                }

                if(Bukkit.getOnlinePlayers().size() >= Main.plugin.getConfig().getInt("min-players"))
                {
                    if((winnerAnnounce == false) && (minPlayerTimer == 0))
                    {
                        Bukkit.broadcastMessage("Starting soon");
                        winner = Vote.pickWinner();
                        Bukkit.broadcastMessage("Winning map is " + winner.getVoteID());
                        ArenaManager.loadArena(winner);
                        winnerAnnounce = true;
                        minPlayerTimer--;
                    }
                    else
                    {
                        if(winnerAnnounce == false)
                        {
                            Bukkit.broadcastMessage("Picking map in " + minPlayerTimer + "seconds!");
                            minPlayerTimer--;
                        }
                    }


                }
                else
                {
                    if(previousAmountOfPlayers != Bukkit.getOnlinePlayers().size())
                    {
                        Bukkit.broadcastMessage("Waiting for " + (Main.plugin.getConfig().getInt("min-players") - Bukkit.getOnlinePlayers().size()) + " players.");
                    }

                    previousAmountOfPlayers = Bukkit.getOnlinePlayers().size();

                    Vote.displayVotes();

                }

                if(winnerAnnounce)
                {
                    Bukkit.broadcastMessage("Teleporting players in " + teleportTimer + "seconds");
                    teleportTimer--;
                    if(teleportTimer == 0)
                    {
                        ArenaManager.teleportPlayersToArena(winner);
                        WorldBorder border = ArenaManager.setInitialBorder(winner);
                        Bukkit.getScheduler().cancelTasks(Main.plugin);
                        ArenaManager.runArena(winner, border);
                    }
                }
            }
        }, 0, 20);
    }


    public static void setLobbySpawn(Player player)
    {
        Main.configManager.getLobby().set("lobby.world", player.getWorld().getName());
        Main.configManager.getLobby().set("lobby.spawn.x", player.getLocation().getX());
        Main.configManager.getLobby().set("lobby.spawn.y", player.getLocation().getY());
        Main.configManager.getLobby().set("lobby.spawn.z", player.getLocation().getZ());
        Main.configManager.saveLobby();

        player.sendMessage("Lobby spawn has been set.");

        Vote.setup();
        Vote.displayVotes();
    }
}
