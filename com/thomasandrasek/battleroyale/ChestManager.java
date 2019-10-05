package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Logger;

public class ChestManager {
    public static ArrayList<Material> tier1 = new ArrayList<Material>();
    public static ArrayList<Material> tier2 = new ArrayList<Material>();
    public static ArrayList<Material> tier3 = new ArrayList<Material>();
    public static ArrayList<Material> tier4 = new ArrayList<Material>();
    public static ArrayList<Material> tier5 = new ArrayList<Material>();

    public static ArrayList<ChestLoot> tier1New = new ArrayList<>();
    public static ArrayList<ChestLoot> tier2New = new ArrayList<>();
    public static ArrayList<ChestLoot> tier3New = new ArrayList<>();
    public static ArrayList<ChestLoot> tier4New = new ArrayList<>();
    public static ArrayList<ChestLoot> tier5New = new ArrayList<>();


    public static void markChests(String arenaName) {
        Location loc1 = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".world-name")),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.x"),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.y"),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".min-location.z")
        );

        Location loc2 = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".world-name")),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.x"),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.y"),
                Main.configManager.getArena().getDouble("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".max-location.z")
        );

        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".name", arenaName);
        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".chest-count", 0);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            double x = loc1.getX();
            double y = loc1.getY();
            double z = loc1.getZ();
            int chestsFound = 0;

            @Override
            public void run() {
                int loop = 0;

                while(loop < 1000000)
                {
                    if(x == loc2.getX())
                    {
                        Main.configManager.saveArenaChest();
                        Bukkit.getScheduler().cancelTasks(Main.plugin);
                        break;
                    }

                    if(y == loc2.getY())
                    {
                        y = loc1.getY();
                        x++;
                    }

                    if(z == loc2.getZ())
                    {
                        z = loc1.getZ();
                        y++;
                    }

                    Location temp = new Location(loc1.getWorld(), x, y, z);

                    if(temp.getBlock().getType().equals(Material.CHEST)) {
                        chestsFound++;
                        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".chest-count", chestsFound);

                        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".chests.chest-" + (new Integer(chestsFound)).toString() + ".x", temp.getX());
                        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".chests.chest-" + (new Integer(chestsFound)).toString() + ".y", temp.getY());
                        Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt("arena-count") + ".chests.chest-" + (new Integer(chestsFound)).toString() + ".z", temp.getZ());

                        Main.configManager.saveArenaChest();
                    }

                    z++;

                    loop++;
                }
            }
        }, 0, 20);

        Main.configManager.saveArenaChest();
    }

    public static void clearChests(Vote winner) {
        int chestCount = Main.configManager.getArenaChest().getInt("arena.arena-" + winner.getArenaNumber() + ".chest-count");

        for(int i = 1; i <= chestCount; i++) {
            Location location = new Location(
                    Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".x"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".y"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".z")
            );

            Chest chest = (Chest) location.getBlock().getState();

            chest.getInventory().clear();
        }
    }

    public static void fillTiers() {

        for(int i = 1; i <= 5; i++) {
            for(String itemInfo : Main.plugin.getConfig().getStringList("chest-loot.tier-" + i)) {
                String array[] = itemInfo.split(", ");
                Material itemToAdd = Material.getMaterial(array[0].toUpperCase());
                if(itemToAdd != null)
                {
                    if(i == 1) {
                        tier1New.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
                    } else if (i == 2) {
                        tier2New.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
                    } else if (i == 3) {
                        tier3New.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
                    } else if (i == 4) {
                        tier4New.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
                    } else if (i == 5) {
                        tier5New.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
                    }
                }
            }
        }
    }

    public static double getMapRadius(Location min, Location max, Location center) {
        double minX = min.getX();
        double minY = min.getY();
        double minZ = min.getZ();
        double minDeltaX, minDeltaY, minDeltaZ;

        double maxX = max.getX();
        double maxY = max.getY();
        double maxZ = max.getZ();
        double maxDeltaX, maxDeltaY, maxDeltaZ;

        double centerX = max.getX();
        double centerY = max.getY();
        double centerZ = max.getZ();

        minDeltaX = Math.abs(minX - centerX);
        minDeltaY = Math.abs(minY - centerY);
        minDeltaZ = Math.abs(minZ - centerZ);
        double minRadius = Math.sqrt(Math.pow(minDeltaX, 2) + Math.pow(minDeltaY, 2) + Math.pow(minDeltaZ, 2));

        maxDeltaX = Math.abs(maxX - centerX);
        maxDeltaY = Math.abs(maxY - centerY);
        maxDeltaZ = Math.abs(maxZ - centerZ);
        double maxRadius = Math.sqrt(Math.pow(maxDeltaX, 2) + Math.pow(maxDeltaY, 2) + Math.pow(maxDeltaZ, 2));

        return Math.max(minRadius, maxRadius);
    }

    public static double getChestRadius(Location chest, Location center) {
        double chestX = chest.getX();
        double chestY = chest.getY();
        double chestZ = chest.getZ();

        double centerX = center.getX();
        double centerY = center.getY();
        double centerZ = center.getZ();

        double deltaX = Math.abs(chestX - centerX);
        double deltaY = Math.abs(chestY - centerY);
        double deltaZ = Math.abs(chestZ - centerZ);

        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));
    }

    public static void putItem(Chest chest, int tier) {
        Random random = new Random();
        Material material = Material.AIR;
        int max = 0;
        int choice;

        switch(tier) {
            case 1:
                choice = random.nextInt(tier1New.size());
                material = tier1New.get(choice).getMaterial();
                max = tier1New.get(choice).getMax();
                break;
            case 2:
                choice = random.nextInt(tier2New.size());
                material = tier2New.get(choice).getMaterial();
                max = tier2New.get(choice).getMax();
                break;
            case 3:
                choice = random.nextInt(tier3New.size());
                material = tier3New.get(choice).getMaterial();
                max = tier3New.get(choice).getMax();
                break;
            case 4:
                choice = random.nextInt(tier4New.size());
                material = tier4New.get(choice).getMaterial();
                max = tier4New.get(choice).getMax();
                break;
            case 5:
                choice = random.nextInt(tier5New.size());
                material = tier5New.get(choice).getMaterial();
                max = tier5New.get(choice).getMax();
                break;
        }


        Inventory chestInventory = chest.getInventory();
        ItemStack item = new ItemStack(material, 1);
        boolean isDouble = false;
        if(chestInventory.getSize() == 54)
            isDouble = true;

        boolean notInChest = true;
        int spot;
        int amount = random.nextInt(max) + 1;
        while(amount > 0)
        {
            while(notInChest && chestInventory.firstEmpty() != -1) {
                spot = random.nextInt(27);

                if(isDouble)
                    spot = random.nextInt(54);

                if(chestInventory.getItem(spot) == null) {
                    chestInventory.setItem(spot, item);
                    notInChest = false;
                }
            }
            notInChest = true;
            amount--;
        }
    }

    public static void fillChests(Vote winner) {
        int chestCount = Main.configManager.getArenaChest().getInt("arena.arena-" + winner.getArenaNumber() + ".chest-count");
        Random random = new Random();
        int lootCount;
        double mapRadius;
        double chestDistance;
        Location chestPosition;
        int secondGuess;
        int firstGuess;
        int luckGuess;

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

        Location center = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber() + ".mid-location.z")
        );

        mapRadius = getMapRadius(min, max, center);

        for(int i = 1; i <= chestCount; i++) {
            Location chestLocation = new Location(
                    Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name")),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".x"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".y"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber() + ".chests.chest-" + i + ".z")
            );

            Chest chest = (Chest) chestLocation.getBlock().getState();

            chestDistance = getChestRadius(chestLocation, center);
            lootCount = random.nextInt(6) + 4;

            for(int j = 0; j < lootCount; j++) {
                firstGuess = random.nextInt(100) + 1;
                luckGuess = random.nextInt(100) + 1;

                if(((Math.abs(chestDistance - mapRadius) / mapRadius) * 100) >= luckGuess) {

                    secondGuess = random.nextInt(100) + 1;



                    firstGuess = Math.max(firstGuess, secondGuess);

                    /*if(firstGuess <= 50) {
                        secondGuess = random.nextInt(100) + 1;
                    }*/

                    firstGuess = Math.max(firstGuess, secondGuess);

                }

                if(firstGuess >= 98) {
                    putItem(chest, 5);
                } else if (firstGuess >= 90) {
                    putItem(chest, 4);
                } else if (firstGuess >= 78) {
                    putItem(chest, 3);
                } else if (firstGuess >= 60) {
                    putItem(chest, 2);
                } else {
                    putItem(chest, 1);
                }
            }
        }
    }
}

