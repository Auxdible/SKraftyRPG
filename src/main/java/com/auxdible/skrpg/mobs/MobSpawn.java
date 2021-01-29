package com.auxdible.skrpg.mobs;


import org.bukkit.Location;

import java.util.ArrayList;

public class MobSpawn {
    private Location location;
    private MobType mob;
    private int id;
    private ArrayList<Mob> currentlySpawnedMobs;
    public MobSpawn(MobType mob, Location location, int id) {
        this.mob = mob;
        this.location = location;
        this.currentlySpawnedMobs = new ArrayList<>();
        this.id = id;
    }
    public Location getLocation() { return location; }
    public MobType getMob() { return mob; }
    public int getId() { return id; }
    public ArrayList<Mob> getCurrentlySpawnedMobs() { return currentlySpawnedMobs; }
}
