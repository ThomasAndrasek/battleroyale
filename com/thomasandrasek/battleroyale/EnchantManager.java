package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantManager {


    public static void openEnchantMenu(PlayerInteractEvent event)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&d&lEpic Enchantments Sold Here OwO"));

        ItemStack ironSword = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = ironSword.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lSharpness I Cost: &65xp"));
        ironSword.setItemMeta(meta);

        ItemStack ironChestPlate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        meta = ironChestPlate.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lProtection I Cost: &62xp"));
        ironChestPlate.setItemMeta(meta);

        ItemStack bow = new ItemStack(Material.BOW, 1);
        meta = bow.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&9Power I Cost: &65xp"));
        bow.setItemMeta(meta);

        inventory.setItem(11, ironSword);
        inventory.setItem(13, ironChestPlate);
        inventory.setItem(15, bow);

        event.getPlayer().openInventory(inventory);
        event.setCancelled(true);
    }

    public static void giveSharpnessBook(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(player.getLevel() >= 5)
        {
            if(player.getInventory().firstEmpty() != -1)
            {
                player.setLevel(player.getLevel() - 5);

                ItemStack sharpnessBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta meta = sharpnessBook.getItemMeta();
                meta.setDisplayName("Sharpness I");
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                sharpnessBook.setItemMeta(meta);

                player.getInventory().setItem(player.getInventory().firstEmpty(), sharpnessBook);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPurchased: Sharpness I"));
            }
            else
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInventory full"));
            }
        }
        else
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot enough experience"));
        }

        event.setCancelled(true);
    }

    public static void givePowerBook(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(player.getLevel() >= 5)
        {
            if(player.getInventory().firstEmpty() != -1)
            {
                player.setLevel(player.getLevel() - 5);

                ItemStack powerBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta meta = powerBook.getItemMeta();
                meta.setDisplayName("Power I");
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                powerBook.setItemMeta(meta);

                player.getInventory().setItem(player.getInventory().firstEmpty(), powerBook);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPurchased: Power I"));
            }
            else
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInventory full"));
            }
        }
        else
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot enough experience"));
        }

        event.setCancelled(true);
    }

    public static void giveProtectionBook(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(player.getLevel() >= 2)
        {
            if(player.getInventory().firstEmpty() != -1)
            {
                player.setLevel(player.getLevel() - 2);

                ItemStack protectionBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta meta = protectionBook.getItemMeta();
                meta.setDisplayName("Protection I");
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                protectionBook.setItemMeta(meta);

                player.getInventory().setItem(player.getInventory().firstEmpty(), protectionBook);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPurchased: Protection I"));
            }
            else
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInventory full"));
            }
        }
        else
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot enough experience"));
        }

        event.setCancelled(true);
    }
}
