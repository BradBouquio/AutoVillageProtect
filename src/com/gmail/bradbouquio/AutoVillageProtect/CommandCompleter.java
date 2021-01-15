package com.gmail.bradbouquio.AutoVillageProtect;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCompleter implements TabCompleter {

    private static final List<String> COMMANDS = new ArrayList<>();
    private static final List<String> WORLDNAME = new ArrayList<>();
    private static final List<String> BLANKLIST = new ArrayList<>();

    public CommandCompleter() {
        COMMANDS.add("protect");
        COMMANDS.add("reload");
        COMMANDS.add("removeall");
        Bukkit.getWorlds().forEach(world -> {
            WORLDNAME.add(world.getName());
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("avp.admin") || !sender.isOp()) return BLANKLIST;
        if(args.length == 1) {
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
            Collections.sort(completions);
            return completions;
        }
        if(args[0].equalsIgnoreCase("protect") && args.length == 2) {
            if(args[1].length()==0) return WORLDNAME;
            else return BLANKLIST;
        }

        if(args[0].equalsIgnoreCase("removeall") && args.length == 2) {
            if(args[1].length()==0) return WORLDNAME;
            else return BLANKLIST;
        }
        return BLANKLIST;
    }
}
