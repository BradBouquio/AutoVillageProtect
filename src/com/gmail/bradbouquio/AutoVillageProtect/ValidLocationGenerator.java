package com.gmail.bradbouquio.AutoVillageProtect;

import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ValidLocationGenerator {


    private static int xCoord;
    private static int zCoord;
    private final int minXCoord;
    private final int minZCoord;
    private final int maxXCoord;
    private final int maxZCoord;
    private boolean finishedAllCoords = false;

    String worldName;
    World world;
    double percentageComplete = 0;
    double blocksToCheck = 0;
    double blocksChecked = 0;

    ValidLocationGenerator(String worldName){
        this.worldName = worldName;
        world = Bukkit.getWorld(worldName);
        this.minXCoord = AutoVillageProtect.plugin.getConfig().getInt("SearchBoundaries.MinX");
        this.minZCoord = AutoVillageProtect.plugin.getConfig().getInt("SearchBoundaries.MinZ");
        this.maxXCoord = AutoVillageProtect.plugin.getConfig().getInt("SearchBoundaries.MaxX");
        this.maxZCoord = AutoVillageProtect.plugin.getConfig().getInt("SearchBoundaries.MaxZ");
        xCoord = minXCoord;
        zCoord = minZCoord;
        blocksToCheck = ((maxXCoord - minXCoord)/16d) * ((maxZCoord - minZCoord)/16d);
    }

    @Nullable
    public Location findNext(){
        Location loc = null;
        while(loc == null) {
            if (finishedAllCoords) return null;
            loc = new Location(Bukkit.getWorld(worldName), xCoord, 0, zCoord);
            finishedAllCoords = setNextCoords();
            if (world != null && loc != null) return loc;
        }
        return null;
    }

    private boolean setNextCoords(){
        if(xCoord + 16 < maxXCoord) xCoord = xCoord + 16;
        else{
            if(zCoord + 16 < maxZCoord) {
                blocksChecked++;
                xCoord = minXCoord;
                zCoord = zCoord + 16;
                return false;
            }
            else {
                xCoord = minXCoord;
                zCoord = minZCoord;
                return true;
            }
        }
        blocksChecked++;
        return false;
    }

    public double completionPercentage(){
        return (blocksChecked/blocksToCheck)*100;
    }
}
