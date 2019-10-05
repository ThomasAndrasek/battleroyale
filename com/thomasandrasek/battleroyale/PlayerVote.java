package com.thomasandrasek.battleroyale;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerVote {

    public static ArrayList<PlayerVote> playerVotes = new ArrayList<>();

    private Player player;
    private int voteID;

    public PlayerVote(Player player, int voteID)
    {
        this.player = player;
        this.voteID = voteID;

        playerVotes.add(this);
    }

    public int getVoteID()
    {
        return this.voteID;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public void setVoteID(int voteID)
    {
        this.voteID = voteID;
    }

    public static PlayerVote getPlayerVote(Player player)
    {
        for(int i = 0; i < playerVotes.size(); i++)
        {
            if(playerVotes.get(i).getPlayer().equals(player))
            {
                return playerVotes.get(i);
            }
        }

        return null;
    }
}
