package com.auxdible.skrpg.mobs;


import com.auxdible.skrpg.SKRPG;
import org.bukkit.Location;

import java.util.ArrayList;

public class MobSpawn {
    private Location location;
    private MobType mob;
    private SKRPG skrpg;
    private ArrayList<Mob> currentlySpawnedMobs;
    public MobSpawn(MobType mob, Location location, SKRPG skrpg) {
        this.mob = mob;
        this.location = location;
        this.currentlySpawnedMobs = new ArrayList<>();
        this.skrpg = skrpg;
    }
    public Location getLocation() { return location; }
    public MobType getMob() { return mob; }
    public int getId() { return skrpg.getMobSpawnManager().getMobSpawns().indexOf(this); }
    public ArrayList<Mob> getCurrentlySpawnedMobs() { return currentlySpawnedMobs; }
}
