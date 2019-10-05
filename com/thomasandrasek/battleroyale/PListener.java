package com.thomasandrasek.battleroyale;

import com.thomasandrasek.teampoints.Team;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PListener implements Listener {
    Main plugin;

    protected PListener(Main plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        SpawnPoint.replacePlayer(event.getPlayer());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        if(event.getEntity().getType().equals(EntityType.SNOWBALL))
        {
            if(event.getHitEntity() != null)
            {
                if(event.getHitEntity().getType().equals(EntityType.PLAYER))
                {
                    Player player = (Player) event.getHitEntity();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                }
            }
        }

        if(event.getEntity().getType().equals(EntityType.EGG))
        {
            if(event.getHitEntity() != null)
            {
                Player player = (Player) event.getHitEntity();
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 20));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 10));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if(SpawnPoint.getSpawnPoint((event.getEntity())) != null)
        {
            for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
            {
                if(SpawnPoint.spawnPoints.get(i).getPlayer().equals(event.getEntity()))
                {
                    SpawnPoint.spawnPoints.remove(i);
                    if(event.getEntity().getKiller() != null)
                        event.getEntity().getKiller().setLevel(event.getEntity().getKiller().getLevel() + 2);
                    break;
                }
            }
        }

        if(SpawnPoint.getSpawnPoint((event.getEntity())) != null)
        {
            for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
            {
                if(SpawnPoint.spawnPoints.get(i).getPlayer().equals(event.getEntity()))
                {
                    SpawnPoint.spawnPoints.remove(i);
                    break;
                }
            }
        }

        event.getEntity().setLevel(0);

        if(event.getDeathMessage().contains("went up in flames") || event.getDeathMessage().contains("burned to death"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " has met their fiery demise! &6" + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("tried to swim in lava"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " that's not water! &6" + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("suffocated in a wall"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " got stuck between a rock and a hard place! &6" + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("drowned"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " forgot how to swim! &6" + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("fell from a high place"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " thought they had wings! &6" + + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("was slain by"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " tried to challenge " + event.getEntity().getKiller().getDisplayName() + "! ❤&e"+ (Math.round(event.getEntity().getKiller().getHealth()*100.0)/100.0) + " &6" + + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("was shot by"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " tried to outrun " + event.getEntity().getKiller().getDisplayName() + "! ❤&e"+ (Math.round(event.getEntity().getKiller().getHealth()*100.0)/100.0) + " &6" + + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("was impaled by"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " was impaled by " + event.getEntity().getKiller().getDisplayName() + "! ❤&e"+ (Math.round(event.getEntity().getKiller().getHealth()*100.0)/100.0) + " &6" + + SpawnPoint.spawnPoints.size() + " remaining!"));

        if(event.getDeathMessage().contains("was struck by lightning whilst fighting"))
            event.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + event.getEntity().getDisplayName() + " has been struct down with the power of THOR by " + event.getEntity().getKiller().getDisplayName() + "! ❤&e"+ (Math.round(event.getEntity().getKiller().getHealth()*100.0)/100.0) + " &6" + + SpawnPoint.spawnPoints.size() + " remaining!"));

        event.getEntity().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(SpawnPoint.getSpawnPoint(event.getPlayer()) != null)
        {
            BrokenBlock.breakBlock(event.getBlock().getType(), event.getBlock().getLocation(), event);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(SpawnPoint.getSpawnPoint(event.getPlayer()) != null)
        {
            PlacedBlock.placeBlock(event.getBlock().getType(), event.getBlock().getLocation(), event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getCurrentItem() != null)
        {
            try
            {
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&9&lSharpness I Cost: &65xp")))
                {
                    EnchantManager.giveSharpnessBook(event);
                }

                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&9&lProtection I Cost: &62xp")))
                {
                    EnchantManager.giveProtectionBook(event);
                }

                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&l&9Power I Cost: &65xp")))
                {
                    EnchantManager.givePowerBook(event);
                }
            }
            catch(NullPointerException exception)
            {
                // do nothing, fuck you
            }
        }


        if(event.getCurrentItem() != null && event.getCursor() != null)
        {
            if(event.getCursor().getType().equals(Material.ENCHANTED_BOOK))
            {
                Set set = event.getCursor().getItemMeta().getEnchants().entrySet();
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                Iterator itr = set.iterator();
                Map.Entry entry = (Map.Entry)  itr.next();
                meta.addEnchant((Enchantment) entry.getKey(), 1, true);
                event.getCurrentItem().setItemMeta(meta);

                event.getCursor().setAmount(0);

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.EXPERIENCE_BOTTLE))
        {
            event.getPlayer().setLevel(event.getPlayer().getLevel() + 5);
            event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            event.setCancelled(true);
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.MUSHROOM_STEW))
        {
            if(event.getPlayer().getHealth() + 4 > 20)
            {
                event.getPlayer().setHealth(20);
            }
            else
            {
                event.getPlayer().setHealth(event.getPlayer().getHealth() + 4);
            }
            event.getPlayer().getInventory().getItemInMainHand().setType(Material.BOWL);
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS))
        {
            ArrayList<Location> locations = new ArrayList<>();
            for(int i = 0; i < SpawnPoint.spawnPoints.size(); i++)
            {
                if(!(SpawnPoint.spawnPoints.get(i).getPlayer().equals(event.getPlayer())))
                {
                    Team team = Team.getTeamFromPlayer(event.getPlayer());
                    if(!(team.getTeamMate(event.getPlayer()).getPlayer().equals(SpawnPoint.spawnPoints.get(i).getPlayer())))
                    {
                        locations.add(SpawnPoint.spawnPoints.get(i).getPlayer().getLocation());
                    }
                }
            }

            Location closest = locations.get(0);
            double closestDistance = closest.distance(event.getPlayer().getLocation());

            for(int i = 1; i < locations.size(); i++)
            {
                if(locations.get(i).distance(event.getPlayer().getLocation()) < closestDistance)
                {
                    closestDistance = locations.get(i).distance(event.getPlayer().getLocation());
                    closest = locations.get(i);
                }
            }

            event.getPlayer().setCompassTarget(closest);
            locations.clear();

            if(Compass.getCompass(event.getPlayer()) != null)
            {
                if(Compass.getCompass(event.getPlayer()).getUses() -1 >= 0)
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPointing towards nearest player. &6" + (Compass.getCompass(event.getPlayer()).getUses()-1) + " &cuses remaining."));

                if(Compass.getCompass(event.getPlayer()).getUses() == 0)
                {
                    event.getPlayer().getInventory().getItemInMainHand().setAmount(0);
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Compass broken."));
                }
                Compass.getCompass(event.getPlayer()).decrementUses();
            }
            else
            {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPointing towards nearest player. &64 &cuses remaining."));
                new Compass(event.getPlayer());
            }
        }

        if(!(event.getClickedBlock() == null))
        {
            if(event.getClickedBlock().getType().equals(Material.CHEST))
            {
                if(ArenaManager.supplyDropLocation != null)
                {
                    if(event.getClickedBlock().getLocation().equals(ArenaManager.supplyDropLocation))
                    {
                        if(SupplyDropManager.tridentChest)
                        {
                            ArenaManager.supplyDropLocation.getWorld().setStorm(true);
                            ArenaManager.supplyDropLocation.getWorld().setThundering(true);
                        }
                    }
                }

            }


            if(event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE))
            {
                EnchantManager.openEnchantMenu(event);
            }

            if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_HOE) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("BR WAND"))
            {
                if(event.getAction().equals(Action.LEFT_CLICK_BLOCK))
                {
                    Player player = event.getPlayer();
                    player.sendMessage("Position 1 set " + event.getClickedBlock().getLocation().toString());
                    ArenaWand wand = ArenaWand.getArenaWand(player);
                    if(wand != null)
                    {
                        wand.setFirstLocation(event.getClickedBlock().getLocation());
                    }
                    else
                    {
                        wand = new ArenaWand(player);
                        wand.setFirstLocation(event.getClickedBlock().getLocation());
                    }

                    event.setCancelled(true);
                }

                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND))
                {
                    Player player = event.getPlayer();
                    player.sendMessage("Position 2 set " + event.getClickedBlock().getLocation().toString());
                    ArenaWand wand = ArenaWand.getArenaWand(player);
                    if(wand != null)
                    {
                        wand.setSecondLocation(event.getClickedBlock().getLocation());
                    }
                    else
                    {
                        wand = new ArenaWand(player);
                        wand.setSecondLocation(event.getClickedBlock().getLocation());
                    }

                    event.setCancelled(true);
                }
            }
        }
    }
}
