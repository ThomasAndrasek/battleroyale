package com.thomasandrasek.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
    Executes all the commands from the players.
 */

public class CommandExecution
{
    /**
     * Command execution method
     * @param sender person who sent the command
     * @param cmd the command to be executed
     * @param label the label of the command
     * @param args the arguments of the command
     * @return whether or not the command is executed
     */
    public static boolean executeCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("start"))
        {
            ArenaManager.startArena(args);
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("br"))
        {
            if(args[0].equalsIgnoreCase("wand"))
            {
                ArenaWand.givePlayerWand((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("createarena"))
            {
                ArenaManager.createArena((Player) sender, args[1]);
                return true;
            }

            if(args[0].equalsIgnoreCase("lobby"))
            {
                if(args[1].equalsIgnoreCase("setspawn"))
                {
                    LobbyManager.setLobbySpawn((Player) sender);
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("mapchests"))
            {
                Main.configManager.getArenaChest().set("arena.arena-" + args[1] + ".chests", null);
                Main.configManager.saveArenaChest();

                ((Player) sender).sendMessage("Mapping chests");

                Location loc1 = new Location(
                        Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-" + args[1]
                                + ".world-name")),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".min-location.x"),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".min-location.y"),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".min-location.z")
                );

                Location loc2 = new Location(
                        Bukkit.getWorld(Main.configManager.getArena().getString("arena." + "arena-" + args[1]
                                + ".world-name")),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".max-location.x"),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".max-location.y"),
                        Main.configManager.getArena().getDouble("arena." + "arena-" + args[1] + ".max-location.z")
                );

                Main.configManager.getArenaChest().set("arena." + "arena-" + Main.configManager.getArena().getInt(
                        "arena-count") + ".chest-count", 0);

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
                                Main.configManager.getArenaChest().set("arena." + "arena-" + args[1] + ".chest-count",
                                        chestsFound);

                                Main.configManager.getArenaChest().set("arena." + "arena-" + args[1] + ".chests.chest-"
                                        + (Integer.valueOf(chestsFound)).toString() + ".x", temp.getX());
                                Main.configManager.getArenaChest().set("arena." + "arena-" + args[1] + ".chests.chest-"
                                        + (Integer.valueOf(chestsFound)).toString() + ".y", temp.getY());
                                Main.configManager.getArenaChest().set("arena." + "arena-" + args[1] + ".chests.chest-"
                                        + (Integer.valueOf(chestsFound)).toString() + ".z", temp.getZ());

                                Main.configManager.saveArenaChest();
                            }

                            z++;

                            loop++;
                        }
                    }
                }, 0, 20);

                Main.configManager.saveArenaChest();

                return true;
            }

            if(args[0].equalsIgnoreCase("setmid"))
            {
                ArenaManager.setMapCenter((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("setnextspawn"))
            {
                ArenaManager.setNextSpawn((Player) sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("test"))
            {
                ChestManager.fillTiers();
                Main.plugin.getConfig().set("test", ((Player) sender).getInventory().getItemInMainHand());
                Main.plugin.saveConfig();
                return true;
            }
        }

        if(cmd.getName().equalsIgnoreCase("vote"))
        {
            Vote.vote((Player) sender, Integer.parseInt(args[0]));
            return true;
        }

        return false;
    }
}
