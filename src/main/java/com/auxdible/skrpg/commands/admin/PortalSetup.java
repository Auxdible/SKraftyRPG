package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.locations.portals.Portal;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PortalSetup {
    private Player setupPlayer;
    private SKRPG skrpg;
    private int stage;
    private Location bound1;
    private Location bound2;
    private Location teleportLocation;
    public PortalSetup(Player setupPlayer, SKRPG skrpg) {
        this.setupPlayer = setupPlayer;
        this.stage = 0;
        this.skrpg = skrpg;
        nextStage();
    }

    public int getStage() { return stage; }
    public Player getSetupPlayer() { return setupPlayer; }

    public void nextStage() {
        if (stage == 0) {
            Text.applyText(setupPlayer, "Stand at Location Bound 1 for your portal and say \"okay\" to proceed.");
        } else if (stage == 1) {
            bound1 = setupPlayer.getLocation();
            Text.applyText(setupPlayer, "Stand at Location Bound 2 for your portal and say \"okay\" to proceed.");
        } else if (stage == 2) {
            bound2 = setupPlayer.getLocation();

            Text.applyText(setupPlayer, "Stand at the teleport location for your portal and say \"okay\" to proceed.");
        } else if (stage == 3) {
            teleportLocation = setupPlayer.getLocation();


            skrpg.getPortalManager().getPortals().add(new Portal(skrpg, bound1, bound2, teleportLocation));
            Text.applyText(setupPlayer, "Portal created!");
            skrpg.getPlayersInPortalSetup().remove(this);
            return;
        }
        stage++;
    }
}
