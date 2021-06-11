package com.auxdible.skrpg.items.forage;


import com.auxdible.skrpg.SKRPG;
import org.bukkit.Location;

public class ForageLocation {
    private Location location;
    private ForageType forageType;
    private Forage spawnedForage;
    private SKRPG skrpg;
    public ForageLocation(SKRPG skrpg, Location location, ForageType forageType) {
        this.location = location;
        this.forageType = forageType;
        this.skrpg = skrpg;
    }

    public ForageType getForageType() { return forageType; }
    public Location getLocation() { return location; }
    public void spawn() {
        if (spawnedForage != null) {
            spawnedForage.onRemove(skrpg, location);
        }


        spawnedForage = forageType.getForage();
        spawnedForage.onSpawn(skrpg, location);
        skrpg.getForageManager().getAliveForage().add(spawnedForage);
    }
    public void remove() {

        spawnedForage.onRemove(skrpg, location);
        spawnedForage = null;
        skrpg.getForageManager().getAliveForage().remove(spawnedForage);
    }
    public Forage getSpawnedForage() { return spawnedForage; }

    public int getId() {
        return skrpg.getForageManager().getLocations().indexOf(this);
    }
}
