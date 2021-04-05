package com.auxdible.skrpg.regions;

import com.auxdible.skrpg.player.guilds.Guild;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.List;


public class Region {
    private double x;
    private double z;
    private double x2;
    private double z2;
    private String name;
    private int id;
    private Location spawnLocation;
    private Location bannerLocation;
    private Location raidLocation;
    private Guild controllingGuild;
    private boolean loaded;
    private List<RegionFlags> regionFlagsList;
    private ArmorStand stand;
    public Region(double x, double z, double x2, double z2, String name, int id, Location spawnLocation,
                  Location bannerLocation, Guild controllingGuild, Location raidLocation, boolean loaded,
                  List<RegionFlags> regionFlagsList) {
        this.x = x;
        this.z = z;
        this.x2 = x2;
        this.z2 = z2;
        this.name = name;
        this.id = id;
        this.spawnLocation = spawnLocation;
        this.bannerLocation = bannerLocation;
        this.controllingGuild = controllingGuild;
        this.loaded = loaded;
        this.raidLocation = raidLocation;
        this.regionFlagsList = regionFlagsList;
    }

    public List<RegionFlags> getRegionFlagsList() { return regionFlagsList; }
    public ArmorStand getStand() { return stand; }
    public void setStand(ArmorStand stand) { this.stand = stand; }
    public Location getRaidLocation() { return raidLocation; }
    public void setControllingGuild(Guild controllingGuild) { this.controllingGuild = controllingGuild; }
    public boolean isLoaded() { return loaded; }
    public Location getBannerLocation() { return bannerLocation; }
    public Guild getControllingGuild() { return controllingGuild; }
    public Location getSpawnLocation() { return spawnLocation; }
    public double getX() { return x; }
    public double getZ() { return z; }
    public double getX2() { return x2; }
    public double getZ2() { return z2; }
    public String getName() { return Text.color(name); }
    public int getID() { return id; }
}
