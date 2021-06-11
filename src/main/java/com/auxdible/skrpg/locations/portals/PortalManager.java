package com.auxdible.skrpg.locations.portals;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.locations.regions.Region;
import com.auxdible.skrpg.locations.regions.RegionFlags;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class PortalManager {
    private ArrayList<Portal> portals;
    private SKRPG skrpg;
    public PortalManager(SKRPG skrpg) {
        portals = new ArrayList<>();
        this.skrpg = skrpg;
    }

    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("portals") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("portals").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("portals").getKeys(false)) {
            Location bound1 = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    skrpg.getConfig().getDouble("portals." + s + ".x"),
                    skrpg.getConfig().getDouble("portals." + s + ".y"),
                    skrpg.getConfig().getDouble("portals." + s + ".z"));
            Location bound2 = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    skrpg.getConfig().getDouble("portals." + s + ".x2"),
                    skrpg.getConfig().getDouble("portals." + s + ".y2"),
                    skrpg.getConfig().getDouble("portals." + s + ".z2"));
            Location teleport = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    skrpg.getConfig().getDouble("portals." + s + ".xTeleport"),
                    skrpg.getConfig().getDouble("portals." + s + ".yTeleport"),
                    skrpg.getConfig().getDouble("portals." + s + ".zTeleport"));
            portals.add(new Portal(skrpg, bound1, bound2, teleport));

        }
    }
    public void disable() {
        for (Portal portal : portals) {
            skrpg.getConfig().set("portals." + portal.getId() + ".x", portal.getBound1().getX());
            skrpg.getConfig().set("portals." + portal.getId() + ".y", portal.getBound1().getY());
            skrpg.getConfig().set("portals." + portal.getId() + ".z", portal.getBound1().getZ());
            skrpg.getConfig().set("portals." + portal.getId() + ".x2", portal.getBound2().getX());
            skrpg.getConfig().set("portals." + portal.getId() + ".y2", portal.getBound2().getY());
            skrpg.getConfig().set("portals." + portal.getId() + ".z2", portal.getBound2().getZ());
            skrpg.getConfig().set("portals." + portal.getId() + ".xTeleport", portal.getTeleportLocation().getX());
            skrpg.getConfig().set("portals." + portal.getId() + ".yTeleport", portal.getTeleportLocation().getY());
            skrpg.getConfig().set("portals." + portal.getId() + ".zTeleport", portal.getTeleportLocation().getZ());


        }
        skrpg.saveConfig();
    }

    public ArrayList<Portal> getPortals() { return portals; }
    public Portal getPortal(int id) {
        for (Portal portal : portals) {
            if (portal.getId() == id) {
                return portal;
            }
        }
        return null;
    }
}
