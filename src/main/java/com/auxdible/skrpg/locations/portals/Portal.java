package com.auxdible.skrpg.locations.portals;


import com.auxdible.skrpg.SKRPG;
import org.bukkit.Location;

public class Portal {
    private Location bound1;
    private Location bound2;
    private Location teleportLocation;
    private SKRPG skrpg;
    public Portal(SKRPG skrpg, Location bound1, Location bound2, Location teleportLocation) {
        this.bound1 = bound1;
        this.bound2 = bound2;
        this.teleportLocation = teleportLocation;
        this.skrpg = skrpg;
    }

    public Location getBound1() { return bound1; }

    public Location getBound2() { return bound2; }

    public Location getTeleportLocation() { return teleportLocation; }

    public int getId() { return skrpg.getPortalManager().getPortals().indexOf(this); }
}
