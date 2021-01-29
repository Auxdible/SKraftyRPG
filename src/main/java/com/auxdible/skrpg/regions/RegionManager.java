package com.auxdible.skrpg.regions;

import com.auxdible.skrpg.SKRPG;


import java.util.ArrayList;
import java.util.UUID;

public class RegionManager {
    private ArrayList<Region> regions;
    private SKRPG skrpg;
    public RegionManager(SKRPG skrpg) {
        regions = new ArrayList<>();
        this.skrpg = skrpg;
    }

    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("regions") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("regions").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("regions").getKeys(false)) {
            double x = skrpg.getConfig().getDouble("regions." + s + ".x");
            double z = skrpg.getConfig().getDouble("regions." + s + ".z");
            double x2 = skrpg.getConfig().getDouble("regions." + s + ".x2");
            double z2 = skrpg.getConfig().getDouble("regions." + s + ".z2");
            String name = skrpg.getConfig().getString("regions." + s + ".name");
            regions.add(new Region(x, z, x2, z2, name, Integer.parseInt(s)));
        }
    }
    public void disable() {
        for (Region region : regions) {
            skrpg.getConfig().set("regions." + region.getID() + ".x", region.getX());
            skrpg.getConfig().set("regions." + region.getID() + ".z", region.getZ());
            skrpg.getConfig().set("regions." + region.getID() + ".x2", region.getX2());
            skrpg.getConfig().set("regions." + region.getID() + ".z2", region.getZ2());
            skrpg.getConfig().set("regions." + region.getID() + ".name", region.getName());
        }
        skrpg.saveConfig();
    }

    public ArrayList<Region> getRegions() { return regions; }
    public Region getRegion(int id) {
        for (Region region : regions) {
            if (region.getID() == id) {
                return region;
            }
        }
        return null;
    }
    public void addRegion(int id, String name, double x, double z, double x2, double z2) {
        regions.add(new Region(x, z, x2, z2, name, id));
    }
}
