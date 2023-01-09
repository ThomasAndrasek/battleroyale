package com.thomasandrasek.battleroyale;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
//import net.minecraft.server.v1_14_R1.Particles;

import java.util.ArrayList;
import java.util.Random;

public class SupplyDropManager {

    public static boolean tridentChest = false;

    public static ArrayList<ChestLoot> supplyDropItems = new ArrayList<>();

    private static Random random = new Random();

    public static void fillSupplyDropItems() {
        for (String itemInfo : Main.plugin.getConfig().getStringList("supply-drop-content")) {
            String array[] = itemInfo.split(", ");
            Material itemToAdd = Material.getMaterial(array[0].toUpperCase());
            if (itemToAdd != null) {
                supplyDropItems.add(new ChestLoot(itemToAdd, Integer.parseInt(array[1])));
            }
        }
    }

    public static Location supplyDropLocation(Vote winner)
    {
        World world = Bukkit.getWorld(Main.configManager.getArena().getString("arena.arena-" + winner.getArenaNumber() + ".world-name"));
        WorldBorder border = world.getWorldBorder();
        double maxX, maxZ, minX, minZ;
        maxX = border.getCenter().getX() + border.getSize()/2;
        maxZ = border.getCenter().getZ() + border.getSize()/2;
        minX = border.getCenter().getX() - border.getSize()/2;
        minZ = border.getCenter().getZ() - border.getSize()/2;

        double x = (Math.random() * (maxX - minX)) + minX;
        double z = (Math.random() * (maxZ - minZ)) + minZ;
        x = (double) ((int) x);
        z = (double) ((int) z);

        Location temp = new Location(world, x, 254, z);

        while(temp.getBlock().getType().equals(Material.AIR) || temp.getBlock().getType().equals(Material.GLASS))
        {
            temp.setY(temp.getY()-1);
        }

        temp.setY(temp.getY()+1);

        return temp;
    }

    public static double fall(Location dropLocation, double y, double yGround) {
        double yGroundLevel = yGround;
        dropLocation.setY(y);

        if (dropLocation.getY() != yGroundLevel) {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                //dropLocation.getWorld().spawnParticle(Particle.CLOUD, dropLocation.getX(), dropLocation.getY(), dropLocation.getZ(), 500);

//                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(Particles.CLOUD, true, (float) dropLocation.getX(), (float) dropLocation.getY(), (float) dropLocation.getZ(), 0.5f, 0, 0.5f, 0, 100);
//                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
//
//                packet = new PacketPlayOutWorldParticles(Particles.FIREWORK, true, (float) dropLocation.getX(), (float) dropLocation.getY(), (float) dropLocation.getZ(), 0.5f, 0, 0.5f, 4, 100);
//                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                player.playSound(new Location(dropLocation.getWorld(), dropLocation.getX(), yGroundLevel, dropLocation.getZ()), Sound.ENTITY_FIREWORK_ROCKET_BLAST, (float) (0.3 / (dropLocation.getY() / yGroundLevel)), 1);
                player.playSound(dropLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
            }
            dropLocation.setY(dropLocation.getY() - 1);
        }
        else
        {
            spawnSupplyDrop(dropLocation);
            for (Player player:Bukkit.getOnlinePlayers())
            {
                player.playSound(dropLocation, Sound.ENTITY_IRON_GOLEM_HURT, 1, 1);
            }
            return -1;
        }

        return dropLocation.getY();
    }

    public static void spawnSupplyDrop(Location dropLocation)
    {
        int chestType = random.nextInt(10) + 1;

        dropLocation.getBlock().setType(Material.CHEST);
        Chest supplyDrop = (Chest) dropLocation.getBlock().getState();
        Inventory inventory = supplyDrop.getInventory();

        if(chestType == 1)
        {
            ItemStack trident = new ItemStack(Material.TRIDENT, 1);
            ItemMeta meta = trident.getItemMeta();
            meta.addEnchant(Enchantment.LOYALTY, 3, true);
            meta.addEnchant(Enchantment.CHANNELING, 1, true);
            trident.setItemMeta(meta);
            inventory.setItem(13, trident);
            tridentChest = true;
        }
        else
        {
            int i = 0;

            while(i < 10) {
                int choice = random.nextInt(supplyDropItems.size());
                Material material = supplyDropItems.get(choice).getMaterial();
                int max = supplyDropItems.get(choice).getMax();


                ItemStack item = new ItemStack(material, 1);

                boolean notInChest = true;
                int spot;
                int amount = random.nextInt(max) + 1;
                while (amount > 0) {
                    while (notInChest && inventory.firstEmpty() != -1) {
                        spot = random.nextInt(27);

                        if (inventory.getItem(spot) == null) {
                            inventory.setItem(spot, item);
                            notInChest = false;
                        }
                    }
                    notInChest = true;
                    amount--;
                }

                i++;
            }
        }
    }

}
