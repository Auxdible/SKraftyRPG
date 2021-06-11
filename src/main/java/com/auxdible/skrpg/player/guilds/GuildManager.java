package com.auxdible.skrpg.player.guilds;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.locations.regions.Region;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GuildManager {
    private SKRPG skrpg;
    private ArrayList<Guild> guilds;
    public GuildManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        this.guilds = new ArrayList<>();
    }
    public void enableGuilds() {
        try {
            ResultSet guilds = SKRPG.prepareStatement("SELECT * FROM guilds_table").executeQuery();
            while (guilds.next()) {
                skrpg.getLogger().info("next!");
                String name = guilds.getString("NAME");
                List<String> members = Arrays.asList(guilds.getString("members").split(","));
                List<String> ranks = Arrays.asList(guilds.getString("ranks").split(","));
                List<String> permissions = Arrays.asList(guilds.getString("permissions").split(","));
                List<String> regions = Arrays.asList(guilds.getString("regions").split(","));
                HashMap<PlayerData, GuildRank> playerDataList = new HashMap<>();
                HashMap<GuildRank, List<GuildPermissions>> guildRankListHashMap = new HashMap<>();
                List<GuildRank> guildRanks = Arrays.asList(GuildRank.values());
                for (int b = 0; b < members.size(); b++) {
                    skrpg.getLogger().info(members.get(b));
                    playerDataList.put(skrpg.getPlayerManager().loadOfflineMySQLData(Bukkit.getOfflinePlayer(UUID.fromString(members.get(b)))), GuildRank.valueOf(ranks.get(b)));
                }
                for (String permission : permissions) {
                    List<String> rankPermissions = Arrays.asList(permission.split("#"));
                    List<GuildPermissions> guildPermissions = new ArrayList<>();
                    for (String rankPerm : rankPermissions) {
                        guildPermissions.add(GuildPermissions.valueOf(rankPerm));
                    }
                    guildRankListHashMap.put(guildRanks.get(permissions.indexOf(permission)), guildPermissions);
                }
                ArrayList<Region> regionsList = new ArrayList<>();
                for (String region : regions) {
                    for (Region allRegions : skrpg.getRegionManager().getRegions()) {
                        if (ChatColor.stripColor(Text.color(allRegions.getName())).toUpperCase().equals(region)) {
                            regionsList.add(allRegions);
                        }
                    }
                }
                Guild guild = new Guild(guilds.getInt("ID"), name, playerDataList, regionsList, guildRankListHashMap, false);
                this.guilds.add(guild);
            }
        } catch (SQLException x) {
            x.printStackTrace();
        }


    }
    public void disableGuilds() {
        for (Guild guild : guilds) {

                try {
                    if (!guild.isDisbanded()) {
                    ResultSet rs = SKRPG.prepareStatement("SELECT COUNT(ID) FROM guilds_table WHERE ID = " +
                            "'" + guild.getId() + "';").executeQuery();
                    rs.next();
                    StringJoiner membersJoiner = new StringJoiner(",");
                    StringJoiner membersRanksJoiner = new StringJoiner(",");
                    StringJoiner guildPermissionsJoiner = new StringJoiner(",");
                    StringJoiner guildRegionsJoiner = new StringJoiner(",");
                    for (PlayerData playerData : guild.getPlayersInGuild().keySet()) {
                        membersJoiner.add(playerData.getUuid().toString());
                        membersRanksJoiner.add(guild.getPlayersInGuild().get(playerData).toString());
                    }

                    for (List<GuildPermissions> guildPermissions : guild.getGuildPermissions().values()) {
                        StringJoiner subGuildPermissionsJoiner = new StringJoiner("#");
                        for (GuildPermissions guildPermissions1 : guildPermissions) {
                            subGuildPermissionsJoiner.add(guildPermissions1.toString());
                        }
                        guildPermissionsJoiner.add(subGuildPermissionsJoiner.toString());
                    }
                    if (guild.getOwnedRegions() == null) {
                        guildRegionsJoiner.add("NONE");
                    } else {
                        for (Region region : guild.getOwnedRegions()) {
                            guildRegionsJoiner.add(ChatColor.stripColor(Text.color(region.getName().toUpperCase())));
                        }
                    }
                    if (rs.getInt(1) == 0) {
                        SKRPG.prepareStatement("INSERT INTO guilds_table(ID, NAME, MEMBERS, RANKS, PERMISSIONS, REGIONS)" +
                                " VALUES ('" + guild.getId() + "','" + guild.getName() + "','" +
                                membersJoiner.toString() + "','" + membersRanksJoiner.toString() + "','" +
                                guildPermissionsJoiner.toString() + "','" + guildRegionsJoiner.toString() + "');").executeUpdate();
                    } else {
                        SKRPG.prepareStatement("UPDATE guilds_table SET NAME = '" + guild.getName() + "'," +
                                " MEMBERS = '" + membersJoiner.toString() + "'," +
                                " RANKS = '" + membersRanksJoiner.toString() + "'," +
                                " PERMISSIONS = '" + guildPermissionsJoiner.toString() + "'," +
                                " REGIONS = '" + guildRegionsJoiner.toString() + "' WHERE ID = '" + guild.getId() + "';").executeUpdate();
                    }
                    } else {
                        SKRPG.prepareStatement("DELETE FROM guilds_table WHERE ID = '" + guild.getId() + "';").executeUpdate();
                    }
                } catch (SQLException x) {
                    x.printStackTrace();
                }

        }
    }
    public ArrayList<Guild> getGuilds() { return guilds; }
    public void createGuild(PlayerData owner, String name) {
        HashMap<PlayerData, GuildRank> guildMember = new HashMap<>();
        HashMap<GuildRank, List<GuildPermissions>> guildPermsDefault = new HashMap<>();
        guildPermsDefault.put(GuildRank.MEMBER, Arrays.asList(GuildPermissions.CHAT));
        guildPermsDefault.put(GuildRank.ELDER, Arrays.asList(GuildPermissions.CHAT, GuildPermissions.INVITE_PLAYER));
        guildPermsDefault.put(GuildRank.MODERATOR, Arrays.asList(GuildPermissions.CHAT, GuildPermissions.KICK_PLAYER,
                GuildPermissions.STAFF_CHAT, GuildPermissions.INVITE_PLAYER, GuildPermissions.RAID));
        guildPermsDefault.put(GuildRank.ADMIN, Arrays.asList(GuildPermissions.CHAT, GuildPermissions.KICK_PLAYER,
                GuildPermissions.STAFF_CHAT, GuildPermissions.INVITE_PLAYER, GuildPermissions.BAN_PLAYER, GuildPermissions.CHANGE_PERMISSIONS, GuildPermissions.RAID));
        guildPermsDefault.put(GuildRank.OWNER, Arrays.asList(GuildPermissions.CHAT, GuildPermissions.KICK_PLAYER,
                GuildPermissions.STAFF_CHAT, GuildPermissions.INVITE_PLAYER, GuildPermissions.BAN_PLAYER, GuildPermissions.CHANGE_PERMISSIONS,
                GuildPermissions.CHANGE_NAME, GuildPermissions.RAID));
        guildMember.put(owner, GuildRank.OWNER);
        Guild guild = new Guild(guilds.size() + 1, name, guildMember, null, guildPermsDefault, false);
        StringJoiner membersJoiner = new StringJoiner(",");
        StringJoiner membersRanksJoiner = new StringJoiner(",");
        StringJoiner guildPermissionsJoiner = new StringJoiner(",");
        StringJoiner guildRegionsJoiner = new StringJoiner(",");

        for (PlayerData playerData : guild.getPlayersInGuild().keySet()) {
            membersJoiner.add(playerData.getUuid().toString());
            membersRanksJoiner.add(guild.getPlayersInGuild().get(playerData).toString());
        }
        for (List<GuildPermissions> guildPermissions : guild.getGuildPermissions().values()) {
            StringJoiner subGuildPermissionsJoiner = new StringJoiner("#");
            for (GuildPermissions guildPermissions1 : guildPermissions) {
                subGuildPermissionsJoiner.add(guildPermissions1.toString());
            }
            guildPermissionsJoiner.add(subGuildPermissionsJoiner.toString());
        }
        if (guild.getOwnedRegions() == null) {
            guildRegionsJoiner.add("NONE");
        } else {
            for (Region region : guild.getOwnedRegions()) {
                guildRegionsJoiner.add(ChatColor.stripColor(Text.color(region.getName().toUpperCase())));
            }
        }
        try {
            SKRPG.prepareStatement("INSERT INTO guilds_table(ID, NAME, MEMBERS, RANKS, PERMISSIONS, REGIONS)" +
                    " VALUES ('" + guild.getId() + "','" + guild.getName() + "','" +
                    membersJoiner.toString() + "','" + membersRanksJoiner.toString() + "','" +
                    guildPermissionsJoiner.toString() + "','" + guildRegionsJoiner.toString() + "');").executeUpdate();
        } catch (SQLException x) {
            x.printStackTrace();
        }


        this.guilds.add(guild);
    }
    public void deleteGuild(int id) {
        if (getGuild(id).getOwnedRegions() != null) {
            for (Region regionsOwned : getGuild(id).getOwnedRegions()) {
                regionsOwned.setControllingGuild(null);
                skrpg.getRegionManager().buildRegion(regionsOwned);
            }
        }
        getGuild(id).setDisbanded(true);
        this.guilds.remove(getGuild(id));
        try {
            SKRPG.prepareStatement("DELETE FROM guilds_table WHERE ID = '" + id + "';").executeUpdate();
        } catch (SQLException x) {
            x.printStackTrace();
        }
        for (Guild guild : getGuilds()) {
            skrpg.getLogger().info(guild.getId() + " " + guild.getName());
        }
    }
    public boolean isInGuild(PlayerData playerData) {
        for (Guild guild : guilds) {
            if (guild.getPlayersInGuild().containsKey(playerData)) {
                return true;
            }
        }
        return false;
    }
    public Guild getPlayerGuild(PlayerData playerData) {
        for (Guild guild : guilds) {
            if (guild.getPlayersInGuild().containsKey(playerData)) {
                return guild;
            }
        }
        return null;
    }
    public Guild getGuild(int id) {
        for (Guild guild : guilds) {
            if (guild.getId() == id) {
                return guild;
            }
        }
        return null;
    }
}
