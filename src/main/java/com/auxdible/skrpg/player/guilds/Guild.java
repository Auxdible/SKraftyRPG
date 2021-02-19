package com.auxdible.skrpg.player.guilds;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
    private int id;
    private int powerLevel;
    private boolean disbanded;
    public Guild(int id, String name, HashMap<PlayerData, GuildRank> playersInGuild, ArrayList<Region> ownedRegions, HashMap<GuildRank, List<GuildPermissions>> guildPermissions, boolean disbanded) {
        this.name = name;
        this.playersInGuild = playersInGuild;
        this.ownedRegions = ownedRegions;
        this.guildPermissions = guildPermissions;
        this.id = id;
        this.disbanded = disbanded;
        for (PlayerData playerData : playersInGuild.keySet()) {
            powerLevel = powerLevel + SKRPG.levelToInt(playerData.getCombat().getLevel().toString());
        }
    }
    public boolean isDisbanded() { return disbanded; }
    public void setDisbanded(boolean disbanded) { this.disbanded = disbanded; }

    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }

    public int getId() { return id; }
    public HashMap<GuildRank, List<GuildPermissions>> getGuildPermissions() { return guildPermissions; }
    public String getName() { return name; }
    public ArrayList<Region> getOwnedRegions() { return ownedRegions; }
    public HashMap<PlayerData, GuildRank> getPlayersInGuild() { return playersInGuild; }
    public void takeOverRegion(Region region, Player player) {
        // TODO REGION TAKEOVER SYSTEM
        for (PlayerData playerData : playersInGuild.keySet()) {
            if (Bukkit.getPlayer(playerData.getUuid()) != null) {
                Bukkit.getPlayer(playerData.getUuid())
                        .playSound(Bukkit.getPlayer(playerData.getUuid()).getLocation(),
                                Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
            }
        }
        guildAnnouncement("&6&l" + player.getDisplayName() + " &7took over the region " + region.getName());
        if (this.ownedRegions == null) {
            this.ownedRegions = new ArrayList<>();
            this.ownedRegions.add(region);
        } else {
            this.ownedRegions.add(region);
        }

    }
    public void loseRegion(Region region, Player player) {
        // TODO REGION TAKEOVER SYSTEM
        for (PlayerData playerData : playersInGuild.keySet()) {
            if (Bukkit.getPlayer(playerData.getUuid()) != null) {
                Bukkit.getPlayer(playerData.getUuid())
                        .playSound(Bukkit.getPlayer(playerData.getUuid()).getLocation(),
                                Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.2f);
            }
        }
        guildAnnouncement("&c&l" + player.getDisplayName() + " &7took over your region " + region.getName());
        this.ownedRegions.remove(region);
    }
    public void guildAnnouncement(String string) {
        for (PlayerData playerData : playersInGuild.keySet()) {
            if (Bukkit.getPlayer(playerData.getUuid()) != null) {
                Text.applyText(Bukkit.getPlayer(playerData.getUuid()), "&2&lGuild &r&8| &7" + string);
            }
        }
    }
    public void guildMessage(Player player, String string) {
        for (PlayerData playerData : playersInGuild.keySet()) {
            if (Bukkit.getPlayer(playerData.getUuid()) != null) {
                if (getGuildPermissions().get(getPlayersInGuild().get(playerData)).contains(GuildPermissions.CHAT)) {
                    Text.applyText(Bukkit.getPlayer(playerData.getUuid()), "&2&lGuild &r&8| " +
                            getPlayersInGuild().get(playerData).getNameColored() + " " + player.getName() + " &7> &r&f" + string);
                }

            }
        }
    }
    public void staffMessage(Player player, String string) {
        for (PlayerData playerData : playersInGuild.keySet()) {
            if (Bukkit.getPlayer(playerData.getUuid()) != null) {
                if (getGuildPermissions().get(getPlayersInGuild().get(playerData)).contains(GuildPermissions.STAFF_CHAT)) {
                    Text.applyText(Bukkit.getPlayer(playerData.getUuid()), "&3&lStaff &r&8| " +
                            getPlayersInGuild().get(playerData).getNameColored() + " " + player.getName() + " &6> &r&7" + string);
                }

            }
        }
    }
    public void removePlayer(PlayerData playerData) {
        this.playersInGuild.remove(playerData);
    }
    public void addPlayer(PlayerData playerData) {
        this.playersInGuild.put(playerData, GuildRank.MEMBER);
    }
}
