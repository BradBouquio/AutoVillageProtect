package com.gmail.bradbouquio.AutoVillageProtect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private int serviceTaskID = 0;
    int ticks = 0;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission("avp.admin") && !commandSender.isOp()){
            commandSender.sendMessage(ChatColor.RED + "You don't have permission!");
            return true;
        }
        if(args.length > 0 && args[0].equalsIgnoreCase("protect")){
            if(args.length > 1){
                String worldName = args[1];
                if(!Bukkit.getWorlds().contains(Bukkit.getWorld(args[1]))){
                    commandSender.sendMessage("That is not a valid world");
                    return true;
                }
                VillageProtector vilProt = new VillageProtector(worldName);

                commandSender.sendMessage("Starting village protection..");
                WERegionMaker regionMaker = new WERegionMaker(Bukkit.getWorld(worldName));
                serviceTaskID = Bukkit.getScheduler().runTaskTimer(AutoVillageProtect.plugin, () -> {
                    ticks++;
                    if(vilProt.isFinished()) {
                        Bukkit.getScheduler().cancelTask(serviceTaskID);
                        commandSender.sendMessage("Protected " + vilProt.foundVillages.size() + " new village(s) in " + ticks/20 + " seconds.");
                        ticks = 0;
                    } else{
                        for(int i = 0; i < 400; i++){
                            vilProt.protectNext();
                        }

                        if(ticks%80 == 0){
                            commandSender.sendMessage(String.format("%.2f",vilProt.getCompletionPercentage()) + "% complete with "
                                    + vilProt.foundVillages.size() + " new villages protected.");
                        }

                        vilProt.protectedVillages.forEach(location -> {
                            regionMaker.create(location, AutoVillageProtect.plugin.getConfig().getInt("ProtectionRadius"));
                        });
                        vilProt.protectedVillages.clear();

                    }

                }, 1L, 1L).getTaskId();

                return true;
            }

        }

        if(args.length > 0 && args[0].equalsIgnoreCase("reload")){
            AutoVillageProtect.plugin.reloadConfig();
            commandSender.sendMessage("AutoVillageProtect config reloaded.");
            return true;
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("removeall")){
            if(args.length > 1){
                String worldName = args[1];
                if(!Bukkit.getWorlds().contains(Bukkit.getWorld(args[1]))){
                    commandSender.sendMessage("That is not a valid world");
                    return true;
                }
                WERegionMaker remover = new WERegionMaker(Bukkit.getWorld(worldName));
                remover.removeAll();
                commandSender.sendMessage("Regions have been removed from " + worldName + ".");
            }
        }
        return false;
    }

}
