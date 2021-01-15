package com.gmail.bradbouquio.AutoVillageProtect;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WERegionMaker {

    private static RegionManager rgManager;
    private List<String> regionNames;
    private static boolean regionAtLocation = false;

    WERegionMaker(World world){
        rgManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        regionNames = new ArrayList<>();
        rgManager.getRegions().keySet().forEach(name -> {if(name.startsWith("_avp_vil")) regionNames.add(name);});
    }

    public static boolean regionAtLocation(Location location) {
        regionAtLocation = false;
        if(rgManager == null) rgManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        rgManager.getApplicableRegions(BlockVector3.at(location.getBlockX(),location.getBlockY(),location.getBlockZ())).forEach(region -> {
            if(region.getId().startsWith("_avp_vil")){
                regionAtLocation = true;
            }
        });
        return regionAtLocation;
    }

    public void create(Location loc, int radius){
        BlockVector3 minPoint = BlockVector3.at(loc.getBlockX()-radius, 0, loc.getBlockZ()-radius);
        BlockVector3 maxPoint = BlockVector3.at(loc.getBlockX()+radius, 256, loc.getBlockZ()+radius);
        String nextRgNum = regionNames.size() > 0 ? String.valueOf(regionNames.size())  : String.valueOf(0);
        String nextRgName = "_avp_vil" + nextRgNum;

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(nextRgName, minPoint, maxPoint);
        regionNames.add(nextRgName);

        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        Map<String, Object> userFlags = AutoVillageProtect.plugin.getConfig().getConfigurationSection("Flags").getValues(false);

        for(String userFlag : userFlags.keySet()){
            for(Flag flag : flagRegistry.getAll()){
                if(flag.getName().equalsIgnoreCase(userFlag)){
                    try {
                        region.setFlag(flag,flag.parseInput(FlagContext.create().setInput((String)userFlags.get(userFlag)).build()));
                    } catch (InvalidFlagFormat invalidFlagFormat) {
                        invalidFlagFormat.printStackTrace();
                    }

                }
            }
        }
        rgManager.addRegion(region);
    }

    public void removeAll(){
        rgManager.getRegions().keySet().forEach((name) -> {
            if(name.startsWith("_avp_vil")) rgManager.removeRegion(name);
        });
    }

}
