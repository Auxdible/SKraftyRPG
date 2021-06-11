package com.auxdible.skrpg.items.forage;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForageManager {
    private SKRPG skrpg;
    private ArrayList<ForageLocation> locations;
    private List<Forage> aliveForage;
    public ForageManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        this.locations = new ArrayList<>();
        this.aliveForage = new ArrayList<>();
    }
    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("forageLocations") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("forageLocations").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("forageLocations").getKeys(false)) {

            locations.add(new ForageLocation(skrpg, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    skrpg.getConfig().getDouble("forageLocations." + s + ".x"),
                    skrpg.getConfig().getDouble("forageLocations." + s + ".y"),
                    skrpg.getConfig().getDouble("forageLocations." + s + ".z")), ForageType.getForage(skrpg.getConfig().getString("forageLocations." + s + ".forageType"))));
        }
    }
    public void disable() {

        for (ForageLocation location : locations) {
            if (location.getForageType() != null) {
                skrpg.getConfig().set("forageLocations." + location.getId() + ".x", location.getLocation().getX());
                skrpg.getConfig().set("forageLocations." + location.getId() + ".y", location.getLocation().getY());
                skrpg.getConfig().set("forageLocations." + location.getId() + ".z", location.getLocation().getZ());
                skrpg.getConfig().set("forageLocations." + location.getId() + ".forageType", location.getForageType().toString());
            }
        }
        skrpg.saveConfig();
    }

    public List<Forage> getAliveForage() { return aliveForage; }

    public ArrayList<ForageLocation> getLocations() { return locations; }
    public ForageLocation getForageLocation(Entity forage) {
        for (ForageLocation forageLocation : locations) {
            if (forageLocation.getSpawnedForage() != null) {
                if (forageLocation.getSpawnedForage().clickEntity() == forage || forageLocation.getSpawnedForage().clickStand() == forage) {
                    return forageLocation;
                }
            }
        }
        return null;
    }
}
