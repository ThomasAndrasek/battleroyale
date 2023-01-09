package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
    General manager for the chests in the arena, not including the supply drop
    Marks all the chests in the arena and stores information about them
    Determines what loot can spawn in chests
    General geometry determining distance from chests to the center of the map
    Determines if chests are "lucky"
 */

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


    /**
     * Marks all chests in the specified arena
     * Counts the total number of chests
     * Stores all information about the chests in the chest config
     * Limits how many blocks can be checked at once to prevent server from crashing after sixty seconds
     * @param arenaName name of the arena
     */
    public static void markChests(String arenaName) {
        Location loc1 = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".world-name")),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".min-location.x"),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".min-location.y"),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".min-location.z")
        );

        Location loc2 = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".world-name")),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".max-location.x"),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".max-location.y"),
                Main.configManager.getArena().getDouble("arena." + "arena-"
                        + Main.configManager.getArena().getInt("arena-count") + ".max-location.z")
        );

        Main.configManager.getArenaChest().set("arena." + "arena-"
                + Main.configManager.getArena().getInt("arena-count") + ".name", arenaName);
        Main.configManager.getArenaChest().set("arena." + "arena-"
                + Main.configManager.getArena().getInt("arena-count") + ".chest-count", 0);

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
                        Main.configManager.getArenaChest().set("arena." + "arena-"
                                + Main.configManager.getArena().getInt("arena-count")
                                + ".chest-count", chestsFound);

                        Main.configManager.getArenaChest().set("arena." + "arena-"
                                + Main.configManager.getArena().getInt("arena-count")
                                + ".chests.chest-" + (Integer.valueOf(chestsFound)).toString() + ".x", temp.getX());
                        Main.configManager.getArenaChest().set("arena." + "arena-"
                                + Main.configManager.getArena().getInt("arena-count") + ".chests.chest-"
                                + (Integer.valueOf(chestsFound)).toString() + ".y", temp.getY());
                        Main.configManager.getArenaChest().set("arena." + "arena-"
                                + Main.configManager.getArena().getInt("arena-count") + ".chests.chest-"
                                + (Integer.valueOf(chestsFound)).toString() + ".z", temp.getZ());

                        Main.configManager.saveArenaChest();
                    }

                    z++;

                    loop++;
                }
            }
        }, 0, 20);

        Main.configManager.saveArenaChest();
    }

    /**
     * Clears the inventory of all the chests in the arena
     * @param winner winning vote arena
     */
    public static void clearChests(Vote winner) {
        int chestCount = Main.configManager.getArenaChest().getInt("arena.arena-" + winner.getArenaNumber()
                + ".chest-count");

        for(int i = 1; i <= chestCount; i++) {
            Location location = new Location(
                    Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-"
                            + winner.getArenaNumber() + ".world-name")),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".x"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".y"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".z")
            );

            Chest chest = (Chest) location.getBlock().getState();

            chest.getInventory().clear();
        }
    }

    /**
     * There are five tiers of loot, the higher the tier, harder it is to spawn
     * Takes all the items in the config.yml file under chest-loot and stores the information using the ChestLoot object
     * Stores information of the items based on type and how many can spawn at maximum
     */
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

    /**
     * Calculates the radius of the map using the maximum point
     * This works in a three dimensional space, considering the map as a sphere
     * Takes the maximum point and the center of the map
     * @param min the minimum location on the map
     * @param max the maximum location on the map
     * @param center the center location on the map
     * @return the maximum radius of the map
     */
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

    /**
     * Calculates the distance from the chest to the center of the map
     * Considers the map in a three dimensional space as a sphere
     * @param chest location of the chest
     * @param center location of the center of the map
     * @return the distance between the center of the map and the chest
     */
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

    /**
     * Puts an item in the chest, chooses a random item from the tier that is given
     * @param chest the chest to put the item in
     * @param tier the tier of the item to put in
     */
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

        // Determines whether the chest is a double chest or not, that way we can utilize full space
        boolean isDouble = false;
        if(chestInventory.getSize() == 54)
            isDouble = true;

        boolean notInChest = true;
        int spot;
        int amount = random.nextInt(max) + 1;
        // Put items in the chest until the amount of items is satisfied
        while(amount > 0)
        {
            // Go through chest until an empty spot is found
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

    /**
     * Fill all chests in the arena
     * @param winner the arena vote winner
     */
    public static void fillChests(Vote winner) {
        int chestCount = Main.configManager.getArenaChest().getInt("arena.arena-" + winner.getArenaNumber()
                + ".chest-count");
        Random random = new Random();
        int lootCount;
        double mapRadius;
        double chestDistance;
        int secondGuess;
        int firstGuess;
        int luckGuess;

        Location min = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber()
                        + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".min-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".min-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".min-location.z")
        );

        Location max = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber()
                        + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".max-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".max-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".max-location.z")
        );

        Location center = new Location(
                Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber()
                        + ".world-name")),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".mid-location.x"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".mid-location.y"),
                Main.configManager.getArena().getDouble("arena.arena-" + winner.getArenaNumber()
                        + ".mid-location.z")
        );

        // Calculate map radius
        mapRadius = getMapRadius(min, max, center);

        // Go through every chest in the arena that has been marked
        for(int i = 1; i <= chestCount; i++) {
            Location chestLocation = new Location(
                    Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-"
                            + winner.getArenaNumber() + ".world-name")),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".x"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".y"),
                    Main.configManager.getArenaChest().getDouble("arena.arena-" + winner.getArenaNumber()
                            + ".chests.chest-" + i + ".z")
            );

            Chest chest = (Chest) chestLocation.getBlock().getState();

            chestDistance = getChestRadius(chestLocation, center);

            // pick randomly how much loot will go in the chest
            lootCount = random.nextInt(6) + 4;

            for(int j = 0; j < lootCount; j++) {
                firstGuess = random.nextInt(100) + 1;
                luckGuess = random.nextInt(100) + 1;

                /*
                Chests can be considerd "lucky"
                The closer the chest is to the center of the map, the more likely it iwll be considered lucky
                If the chest is lucky, there will be another roll on the type of loot that can get in
                The higher choice will be chosen so better loot can be put into the chest
                 */
                if(((Math.abs(chestDistance - mapRadius) / mapRadius) * 100) >= luckGuess) {

                    secondGuess = random.nextInt(100) + 1;

                    firstGuess = Math.max(firstGuess, secondGuess);

                    firstGuess = Math.max(firstGuess, secondGuess);

                }

                // Percent table for each tier and the choice for the item to be put into the chest
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

