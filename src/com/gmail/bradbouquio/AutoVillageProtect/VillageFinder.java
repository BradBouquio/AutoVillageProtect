package com.gmail.bradbouquio.AutoVillageProtect;

import com.sun.istack.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.StructureType;

public class VillageFinder {

    Location loc;

    VillageFinder(){
    }

    VillageFinder(Location loc){
        this.loc = loc;
    }



    @Nullable
    public Location find() {
        Location nearestLoc;

        nearestLoc = loc.getWorld().locateNearestStructure(loc, StructureType.VILLAGE, 1, false);

        if(!WERegionMaker.regionAtLocation(nearestLoc)) return nearestLoc;
        else return null;
    }

    public void setSearchLocation(Location validLocation) {
        loc = validLocation;
    }
}
