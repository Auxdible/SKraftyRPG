package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.regions.RegionFlags;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionSetup {
    private Player setupPlayer;
    private SKRPG skrpg;
    private int stage;
    private double x;
    private double z;
    private double x2;
    private double z2;
    private Location bannerLocation;
    private Location spawnLocation;
    private Location raidLocation;
    private String name;
    public RegionSetup(Player setupPlayer, SKRPG skrpg) {
        this.setupPlayer = setupPlayer;
        this.stage = 0;
        this.skrpg = skrpg;
        nextStage(null);
    }

    public int getStage() { return stage; }
    public Player getSetupPlayer() { return setupPlayer; }

    public void nextStage(String nameTyped) {
        if (stage == 0) {
            Text.applyText(setupPlayer, "Stand at Location Bound 1 for your region and say \"okay\" to proceed.");
        } else if (stage == 1) {
            x = setupPlayer.getLocation().getX();
            z = setupPlayer.getLocation().getZ();
            Text.applyText(setupPlayer, "Stand at Location Bound 2 for your region and say \"okay\" to proceed.");
        } else if (stage == 2) {
            x2 = setupPlayer.getLocation().getX();
            z2 = setupPlayer.getLocation().getZ();
            Text.applyText(setupPlayer, "Stand at the banner location for your region and say \"okay\" to proceed.");
        } else if (stage == 3) {
            bannerLocation = setupPlayer.getLocation();
            Text.applyText(setupPlayer, "Stand at the raid location for your region and say \"okay\" to proceed.");
        } else if (stage == 4) {
            raidLocation = setupPlayer.getLocation();
            Text.applyText(setupPlayer, "Stand at the spawn location for your region and say \"okay\" to proceed.");
        } else if (stage == 5) {
            spawnLocation = setupPlayer.getLocation();
            Text.applyText(setupPlayer, "Type in the name of your region.");
        } else if (stage == 6) {
            if (nameTyped.equals(null) || nameTyped.equals("")) { Text.applyText(setupPlayer, "Type in the name of your region."); return; }
            Text.applyText(setupPlayer, "Type in the region flags seperated with a comma, or type \"none\" (DECORATIVE_REGION,RESOURCE_REGION...etc)");
            this.name = nameTyped;
        } else if (stage == 7) {
            if (nameTyped.equals(null) || nameTyped.equals("")) { Text.applyText(setupPlayer, "Type in the region flags."); return; }
            List<String> splitRegions = Arrays.asList(nameTyped.split(","));
            List<RegionFlags> regionFlags = new ArrayList<>();
            for (String string : splitRegions) {
                try {
                    regionFlags.add(RegionFlags.valueOf(string));
                } catch (IllegalArgumentException x) {

                }
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    skrpg.getRegionManager().addRegion(skrpg.getRegionManager().getRegions().size(), nameTyped, x, z, x2, z2, spawnLocation, raidLocation,
                            bannerLocation.getX(), bannerLocation.getY(), bannerLocation.getZ(), regionFlags);
                }
            }.runTaskLater(skrpg, 5);

            Text.applyText(setupPlayer, "Region created!");
            skrpg.getPlayersInRegionSetup().remove(this);
            return;
        }
        stage++;
    }
}
