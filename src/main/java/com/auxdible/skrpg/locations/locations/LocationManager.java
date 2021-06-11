package com.auxdible.skrpg.locations.locations;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;

public class LocationManager {
    private SKRPG skrpg;
    private HashMap<String, Location> locations;
    public LocationManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        this.locations = new HashMap<>();
    }
    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("locations") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("locations").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("locations").getKeys(false)) {
            locations.put(s, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    skrpg.getConfig().getDouble("locations." + s + ".x"),
                    skrpg.getConfig().getDouble("locations." + s + ".y"),
                    skrpg.getConfig().getDouble("locations." + s + ".z"),
                    (float) skrpg.getConfig().getDouble("locations." + s + ".yaw"),
                    (float) skrpg.getConfig().getDouble("locations." + s + ".pitch")));
        }
    }
    public void disable() {

        for (String location : locations.keySet()) {
            skrpg.getConfig().set("locations." + location + ".x", locations.get(location).getX());
            skrpg.getConfig().set("locations." + location + ".y", locations.get(location).getY());
            skrpg.getConfig().set("locations." + location + ".z", locations.get(location).getZ());
            skrpg.getConfig().set("locations." + location + ".yaw", locations.get(location).getYaw());
            skrpg.getConfig().set("locations." + location + ".pitch", locations.get(location).getPitch());
        }
        skrpg.saveConfig();
    }

    public HashMap<String, Location> getLocations() { return locations; }

    public Location getAbandonedMinesLocation() {
        if (locations.get("abandonedMines") != null) {
            return locations.get("abandonedMines");
        }
        return null;
    }
    public Location getAbandonedMinesSurfaceLocation() {
        if (locations.get("abandonedMinesSurface") != null) {
            return locations.get("abandonedMinesSurface");
        }
        return null;
    }
    public Location getCoalMineLocation() {
        if (locations.get("coalmine") != null) {
            return locations.get("coalmine");
        }
        return null;
    }
    public Location getIronMineLocation() {
        if (locations.get("ironmine") != null) {
            return locations.get("ironmine");
        }
        return null;
    }
    public Location getGoldMineLocation() {
        if (locations.get("goldmine") != null) {
            return locations.get("goldmine");
        }
        return null;
    }
    public Location getLapisMineLocation() {
        if (locations.get("lapismine") != null) {
            return locations.get("lapismine");
        }
        return null;
    }
    public Location getRedstoneMineLocation() {
        if (locations.get("redstonemine") != null) {
            return locations.get("redstonemine");
        }
        return null;
    }
    public Location getCrystalliteMineLocation() {
        if (locations.get("crystallitemine") != null) {
            return locations.get("crystallitemine");
        }
        return null;
    }
    public Location getDiamondMineLocation() {
        if (locations.get("diamondmine") != null) {
            return locations.get("diamondmine");
        }
        return null;
    }
    public Location getObsidianMineLocation() {
        if (locations.get("obsidianmine") != null) {
            return locations.get("obsidianmine");
        }
        return null;
    }
    public Location getMonolithSpawnLocation() {
        if (locations.get("monolithspawn") != null) {
            return locations.get("monolithspawn");
        }
        return null;
    }
}
