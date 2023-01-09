package com.thomasandrasek.battleroyale;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * The vote for the arena that the player wants to win
 */

public class PlayerVote {

    public static ArrayList<PlayerVote> playerVotes = new ArrayList<>();

    private Player player;
    private int voteID;

    /**
     * Constructs the PlayerVote object
     * @param player the player that has voted
     * @param voteID the id of the vote
     */
    public PlayerVote(Player player, int voteID)
    {
        this.player = player;
        this.voteID = voteID;

        playerVotes.add(this);
    }

    /**
     * Gets the id of the vote that the user has voted for
     * @return the id of the vote
     */
    public int getVoteID()
    {
        return this.voteID;
    }

    /**
     * Gets the player who voted
     * @return player who voted
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Sets the id of the vote that the user voted for
     * @param voteID the id of which the voter has voted for
     */
    public void setVoteID(int voteID)
    {
        this.voteID = voteID;
    }

    /**
     * Gets the player vote from the list of player votes
     * @param player the player who voted
     * @return the vote of the player
     */
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
