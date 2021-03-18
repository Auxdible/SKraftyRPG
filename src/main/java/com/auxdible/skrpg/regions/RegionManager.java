package com.auxdible.skrpg.regions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;


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
            getRegion(Integer.parseInt(s));
        }
    }
    public void disable() {
        for (Region region : regions) {
            skrpg.getConfig().set("regions." + region.getID() + ".x", region.getX());
            skrpg.getConfig().set("regions." + region.getID() + ".z", region.getZ());
            skrpg.getConfig().set("regions." + region.getID() + ".x2", region.getX2());
            skrpg.getConfig().set("regions." + region.getID() + ".z2", region.getZ2());
            skrpg.getConfig().set("regions." + region.getID() + ".spawn.x", region.getSpawnLocation().getX());
            skrpg.getConfig().set("regions." + region.getID() + ".spawn.y", region.getSpawnLocation().getY());
            skrpg.getConfig().set("regions." + region.getID() + ".spawn.z", region.getSpawnLocation().getZ());
            skrpg.getConfig().set("regions." + region.getID() + ".raid.x", region.getRaidLocation().getX());
            skrpg.getConfig().set("regions." + region.getID() + ".raid.y", region.getRaidLocation().getY());
            skrpg.getConfig().set("regions." + region.getID() + ".raid.z", region.getRaidLocation().getZ());
            skrpg.getConfig().set("regions." + region.getID() + ".spawn.yaw", region.getSpawnLocation().getYaw());
            skrpg.getConfig().set("regions." + region.getID() + ".spawn.pitch", region.getSpawnLocation().getPitch());
            skrpg.getConfig().set("regions." + region.getID() + ".regionBanner.x", Math.floor(region.getBannerLocation().getX()));
            skrpg.getConfig().set("regions." + region.getID() + ".regionBanner.y", Math.floor(region.getBannerLocation().getY()));
            skrpg.getConfig().set("regions." + region.getID() + ".regionBanner.z", Math.floor(region.getBannerLocation().getZ()));
            if (region.getControllingGuild() != null) {
                skrpg.getConfig().set("regions." + region.getID() + ".guildId", region.getControllingGuild().getId());
            } else {
                skrpg.getConfig().set("regions." + region.getID() + ".guildId", -1);
            }

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
        double x = skrpg.getConfig().getDouble("regions." + id + ".x");
        double z = skrpg.getConfig().getDouble("regions." + id + ".z");
        double x2 = skrpg.getConfig().getDouble("regions." + id + ".x2");
        double z2 = skrpg.getConfig().getDouble("regions." + id + ".z2");
        double xSpawn = skrpg.getConfig().getDouble("regions." + id + ".spawn.x");
        double YSpawn = skrpg.getConfig().getDouble("regions." + id + ".spawn.y");
        double ZSpawn = skrpg.getConfig().getDouble("regions." + id + ".spawn.z");
        double yawSpawn = skrpg.getConfig().getDouble("regions." + id + ".spawn.yaw");
        double pitchSpawn = skrpg.getConfig().getDouble("regions." + id + ".spawn.pitch");
        double xRegion = skrpg.getConfig().getDouble("regions." + id + ".regionBanner.x");
        double yRegion = skrpg.getConfig().getDouble("regions." + id + ".regionBanner.y");
        double zRegion = skrpg.getConfig().getDouble("regions." + id + ".regionBanner.z");
        double xRaid = skrpg.getConfig().getDouble("regions." + id + ".raid.x");
        double yRaid = skrpg.getConfig().getDouble("regions." + id + ".raid.y");
        double zRaid = skrpg.getConfig().getDouble("regions." + id + ".raid.z");
        String name = skrpg.getConfig().getString("regions." + id + ".name");
        int guildId = skrpg.getConfig().getInt("regions." + id + ".guildId");
        Region region = new Region(x, z, x2, z2, name, id,
                new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                        xSpawn, YSpawn, ZSpawn, (float) yawSpawn, (float) pitchSpawn),
                new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                        xRegion, yRegion, zRegion, 0.0f, 0.0f),
                skrpg.getGuildManager().getGuild(guildId), new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                xRaid, yRaid, zRaid, 0.0f, 0.0f), false);
        if (!regions.contains(region)) {
            regions.add(region);
            buildRegion(region);
        }

        return region;
    }
    public void buildRegion(Region region) {
        region.getBannerLocation().getBlock().setType(Material.GREEN_BANNER);
        Rotatable blockData = (Rotatable) region.getBannerLocation().getBlock().getBlockData();
        blockData.setRotation(BlockFace.WEST);
        region.getBannerLocation().getBlock().setBlockData(blockData);
        if (region.getStand() != null) {
            region.getStand().remove();
            region.setStand(null);
        }
        ArmorStand armorStand = (ArmorStand) region.getBannerLocation().getWorld().spawnEntity(region.getBannerLocation(), EntityType.ARMOR_STAND);
        region.setStand(armorStand);
        String name;
        if (region.getControllingGuild() != null) {
            name = Text.color(region.getControllingGuild().getName());
        } else {
            name = "NONE";
        }
        armorStand.setCustomName(Text.color(region.getName() + " &8| &7Owned By: " + name));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.teleport(region.getBannerLocation().add(0.5, 0.0, 0.5));
    }
    public void addRegion(int id, String name, double x, double z, double x2, double z2, Location spawnLocation, Location raidLocation, double bannerX, double bannerY, double bannerZ) {
        Region region = new Region(x, z, x2, z2, name, id, spawnLocation, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                bannerX, bannerY, bannerZ, 0.0f, 0.0f), null, raidLocation,false);
        regions.add(region);
        buildRegion(region);
    }
}
