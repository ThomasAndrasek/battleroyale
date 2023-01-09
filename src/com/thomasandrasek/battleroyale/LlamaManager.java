package com.thomasandrasek.battleroyale;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TraderLlama;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

import java.util.ArrayList;
import java.util.Random;

/*
    General manager for the llamas o' plenty
 */

public class LlamaManager {

    public static ArrayList<TraderLlama> arenaLlamas = new ArrayList<>();

    public static ArrayList<ChestLoot> llamaItems = new ArrayList<>();

    private static Random random = new Random();

    /**
     * Fills the llama items list with the custom items from the config file
     */
    public static void fillLlamaItems() {
        for (String itemInfo : Main.plugin.getConfig().getStringList("llama-content")) {
            String array[] = itemInfo.split(", ");
            Material itemToAdd = Material.getMaterial(array[0].toUpperCase());
            if (itemToAdd != null) {
                llamaItems.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
            }
        }
    }

    /**
     * Sapwns the llamas around the map
     * @param winner the vote winner arena
     */
    public static void spawnLlamas(Vote winner)
    {
        int i = 0;

        while(i < 3)
        {
            World world = Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name"));
            WorldBorder border = world.getWorldBorder();
            double maxX, maxZ, minX, minZ;
            maxX = border.getCenter().getX() + border.getSize()/2;
            maxZ = border.getCenter().getZ() + border.getSize()/2;
            minX = border.getCenter().getX() - border.getSize()/2;
            minZ = border.getCenter().getZ() - border.getSize()/2;

            double x = (Math.random() * ((maxX - 50) - (minX + 50))) + minX;
            double z = (Math.random() * ((maxZ - 50) - (minZ + 50))) + minZ;
            x = (double) ((int) x);
            z = (double) ((int) z);

            Location temp = new Location(world, x, 254, z);

            // Goes until the ground/solid-structure is found
            while(temp.getBlock().getType().equals(Material.AIR) || temp.getBlock().getType().equals(Material.GLASS))
            {
                temp.setY(temp.getY()-1);
            }

            temp.setY(temp.getY()+1);

            TraderLlama llama = (TraderLlama) world.spawnEntity(temp, EntityType.TRADER_LLAMA);
            llama.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6&lLlamas O' Plenty"));
            llama.setCustomNameVisible(true);
            llama.setAdult();
            llama.setStrength(5);
            llama.setCarryingChest(true);

            int j = 0;

            LlamaInventory inventory = llama.getInventory();

            while(j < 15) {
                int choice = random.nextInt(llamaItems.size());
                Material material = llamaItems.get(choice).getMaterial();
                int max = llamaItems.get(choice).getMax();

                int amount = random.nextInt(max) + 1;

                ItemStack item = new ItemStack(material, amount);

                boolean notInChest = true;
                int spot;

                while (notInChest && inventory.firstEmpty() != -1) {
                    spot = random.nextInt(15);

                    if (inventory.getItem(spot) == null) {
                        inventory.setItem(spot, item);
                        notInChest = false;
                    }
                }
                notInChest = true;

                j++;
            }

            arenaLlamas.add(llama);

            i++;
        }
    }

    /**
     * Kills all the llamas in the arena
     * Sets them to babies so that their loot doesn't drop on the ground
     */
    public static void killLlamas()
    {
        for(int i = 0; i < arenaLlamas.size(); i++)
        {
            arenaLlamas.get(i).setBaby();
            arenaLlamas.get(i).setHealth(0);
        }

        arenaLlamas.clear();
    }
}
