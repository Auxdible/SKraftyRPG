package com.auxdible.skrpg.player.guilds.raid;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RaidManager {
    private SKRPG skrpg;
    private HashMap<Player, Raid> raids;
    public RaidManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        this.raids = new HashMap<>();
    }

    public HashMap<Player, Raid> getRaids() { return raids; }
    public void addRaid(Raid raid, Player player) { this.raids.put(player, raid); }
    public void removeRaid(Player player) { this.raids.remove(player); }
    public Raid getRaid(Player player) {
        return raids.get(player);
    }
    public boolean isInRaid(Player player) {
        return raids.get(player) != null;
    }
}
