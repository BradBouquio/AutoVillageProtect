package com.gmail.bradbouquio.AutoVillageProtect;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VillageProtector {

    String worldName;
    private ValidLocationGenerator locGenerator;
    VillageFinder vilFinder;
    private boolean isFinished = false;
    public Set<Location> protectedVillages = new HashSet<>();
    public List<String> foundVillages = new ArrayList<>();

    VillageProtector(String worldName){
        this.worldName = worldName;
        locGenerator = new ValidLocationGenerator(worldName);
        vilFinder = new VillageFinder();
    }

    public void protectNext(){

        Location validLocation = locGenerator.findNext();
        if(validLocation != null) {
            vilFinder.setSearchLocation(validLocation);
            Location nearestVil = vilFinder.find();
            if(nearestVil != null){
                if(!foundVillages.contains(nearestVil.toString())){
                    foundVillages.add(nearestVil.toString());
                    protectedVillages.add(nearestVil);
                }
            }
        } else {
            isFinished = true;
        }

    }

    public boolean isFinished() {
        return isFinished;
    }

    public double getCompletionPercentage(){
        return locGenerator.completionPercentage();
    }

}
