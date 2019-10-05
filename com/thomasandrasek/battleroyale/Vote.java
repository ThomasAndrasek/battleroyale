package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Vote {
    public static ArrayList<Vote> votes = new ArrayList<>();

    private String arenaName;
    private int voteID;
    private int voteTotal;
    private int arenaNumber;

    public static Random random = new Random();

    public Vote(String arenaName, int arenaNumber)
    {
        this.arenaName = arenaName;
        this.voteID = votes.size() + 1;
        this.voteTotal = 0;
        this.arenaNumber = arenaNumber;

        votes.add(this);
    }

    public String getArenaName()
    {
        return this.arenaName;
    }

    public int getVoteID()
    {
        return this.voteID;
    }

    public int getVoteTotal()
    {
        return this.voteTotal;
    }

    public int getArenaNumber() { return this.arenaNumber; }

    public void addVote()
    {
        this.voteTotal++;
    }

    public void removeVote()
    {
        this.voteTotal--;
    }

    public static void displayVotes()
    {
        for(int i = 0; i < votes.size(); i++)
        {
            Bukkit.broadcastMessage(votes.get(i).getVoteID() + ": " + votes.get(i).getArenaName() + ", Votes: " + votes.get(i).getVoteTotal());
        }
    }

    public static void setup()
    {
        int arenaTotal = Main.configManager.getArena().getInt("arena-count");
        int loop = 0;
        while((loop < 4) && (loop < arenaTotal))
        {
            int arenaNum = random.nextInt(arenaTotal) + 1;
            if(!(alreadyBeingVotedFor(Main.configManager.getArena().getString("arena.arena-" + arenaNum + ".name"))))
            {
                new Vote(Main.configManager.getArena().getString("arena.arena-" + arenaNum + ".name"), arenaNum);
                loop++;
            }
        }
    }

    public static boolean alreadyBeingVotedFor(String arenaName)
    {
        for(int i = 0; i < votes.size(); i++)
        {
            if(arenaName.equalsIgnoreCase(votes.get(i).getArenaName()))
            {
                return true;
            }
        }

        return false;
    }

    public static void test()
    {
        Bukkit.broadcastMessage("test");
    }

    public static Vote getVote(int voteID)
    {
        for(int i = 0; i < votes.size(); i++)
        {
            if(votes.get(i).getVoteID() == voteID)
            {
                return votes.get(i);
            }
        }

        return null;
    }

    public static void vote(Player player, int voteID)
    {
        PlayerVote playerVote = PlayerVote.getPlayerVote(player);

        if(playerVote == null)
        {
            if(getVote(voteID) != null)
            {
                new PlayerVote(player, voteID);
                Vote vote = getVote(voteID);
                vote.addVote();
            }
        }
        else
        {
            if(getVote(voteID) != null)
            {
                PlayerVote previousVote = PlayerVote.getPlayerVote(player);
                getVote(voteID).addVote();
                getVote(previousVote.getVoteID()).removeVote();
                previousVote.setVoteID(voteID);
            }
        }
    }

    public static int getHigh()
    {
        int high = votes.get(0).getVoteTotal();

        for(int i = 1; i < votes.size(); i++)
        {
            if(votes.get(i).getVoteTotal() > high)
            {
                high = votes.get(i).getVoteTotal();
            }
        }

        return high;
    }

    public static Vote pickWinner()
    {
        int highestVote = getHigh();

        ArrayList<Vote> highestVotes = new ArrayList<>();

        for(int i = 0; i < votes.size(); i++)
        {
            if(votes.get(i).getVoteTotal() == highestVote)
            {
                highestVotes.add(votes.get(i));
            }
        }

        if(highestVotes.size() == 1)
        {
            return highestVotes.get(0);
        }
        else
        {
            int winnerSpot = random.nextInt(highestVotes.size());

            return highestVotes.get(winnerSpot);
        }
    }

}
