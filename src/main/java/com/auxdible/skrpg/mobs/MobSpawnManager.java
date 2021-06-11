package com.auxdible.skrpg.mobs;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class MobSpawnManager {
    private ArrayList<MobSpawn> mobSpawns;
    private SKRPG skrpg;
    public MobSpawnManager(SKRPG skrpg) {
        mobSpawns = new ArrayList<>();
        this.skrpg = skrpg;
    }

    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("mobSpawns") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("mobSpawns").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("mobSpawns").getKeys(false)) {
            double x = skrpg.getConfig().getDouble("mobSpawns." + s + ".x");
            double z = skrpg.getConfig().getDouble("mobSpawns." + s + ".z");
            double y = skrpg.getConfig().getDouble("mobSpawns." + s + ".y");
            String mobType = skrpg.getConfig().getString("mobSpawns." + s + ".mob");
            addMobSpawn(MobType.valueOf(mobType), x, y, z);
        }
    }
    public void disable() {
        for (MobSpawn mobSpawn : mobSpawns) {
            skrpg.getConfig().set("mobSpawns." + mobSpawn.getId() + ".x", mobSpawn.getLocation().getX());
            skrpg.getConfig().set("mobSpawns." + mobSpawn.getId() + ".z", mobSpawn.getLocation().getZ());
            skrpg.getConfig().set("mobSpawns." + mobSpawn.getId() + ".y", mobSpawn.getLocation().getY());
            skrpg.getConfig().set("mobSpawns." + mobSpawn.getId() + ".mob", mobSpawn.getMob().toString());
            skrpg.saveConfig();
        }

    }

    public ArrayList<MobSpawn> getMobSpawns() { return mobSpawns; }
    public MobSpawn getMobSpawn(int id) {
        for (MobSpawn mobSpawn : mobSpawns) {
            if (mobSpawn.getId() == id) {
                return mobSpawn;
            }
        }
        return null;
    }
    public void addMobSpawn(MobType mobType, double x, double y, double z) {
        MobSpawn mobSpawn = new MobSpawn
                (mobType, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                x, y, z), skrpg);
        mobSpawns.add(mobSpawn);
    }
}
