package com.auxdible.skrpg.player.guilds;

import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.regions.Region;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Guild {
    private HashMap<PlayerData, GuildRank> playersInGuild;
    private ArrayList<Region> ownedRegions;
    private HashMap<GuildRank, List<GuildPermissions>> guildPermissions;
    private String name;
    public Guild(String name, HashMap<PlayerData, GuildRank> playersInGuild, ArrayList<Region> ownedRegions, HashMap<GuildRank, List<GuildPermissions>> guildPermissions) {
        this.name = name;
        this.playersInGuild = playersInGuild;
        this.ownedRegions = ownedRegions;
        this.guildPermissions = guildPermissions;
    }
    public HashMap<GuildRank, List<GuildPermissions>> getGuildPermissions() { return guildPermissions; }
    public String getName() { return name; }
    public ArrayList<Region> getOwnedRegions() { return ownedRegions; }
    public HashMap<PlayerData, GuildRank> getPlayersInGuild() { return playersInGuild; }
    public void takeOverRegion(Region region) {
        // TODO REGION TAKEOVER SYSTEM
        this.ownedRegions.add(region);
    }
    public void loseRegion(Region region) {
        // TODO REGION TAKEOVER SYSTEM
        this.ownedRegions.remove(region);
    }
}
