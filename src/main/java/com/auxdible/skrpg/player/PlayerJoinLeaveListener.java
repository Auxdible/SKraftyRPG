package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.player.actions.PlayerAction;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestDifficulty;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestType;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PlayerJoinLeaveListener implements Listener {
    private SKRPG skrpg;
    public PlayerJoinLeaveListener(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        skrpg.getPlayerManager().loadMySQLPlayerData(e.getPlayer());
        BossBar bossBar = Bukkit.createBossBar(Text.color("&bYou are playing &e&lSKQuest &r&7[v" + skrpg.getVersion() + "]"), BarColor.BLUE, BarStyle.SOLID);

        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100.0);
        bossBar.addPlayer(e.getPlayer());
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) {
            skrpg.getPlayerManager().createPlayer(e.getPlayer().getUniqueId());
        }
        new BukkitRunnable() {

            @Override
            public void run() {

                if (playerData != null) {
                    playerData.getPlayerInventory().updateInventory(e.getPlayer());
                    buildScoreboard(e.getPlayer(), skrpg);
                    playerData.setRenderedNPCs(new ArrayList<>());
                    playerData.setRegion(null);

                    playerData.setSpeed(playerData.getBaseSpeed());
                    playerData.setPlayerAction(new PlayerAction(e.getPlayer(), playerData, skrpg));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (playerData.getLocationWhereLeft() != null) {
                                e.getPlayer().teleport(playerData.getLocationWhereLeft());
                            }
                            playerData.setHp(playerData.getMaxHP());
                            playerData.setEnergy(playerData.getMaxEnergy());
                        }
                    }.runTaskLater(skrpg, 40);
                    if (!playerData.getCompletedQuests().contains(Quests.TUTORIAL) && !playerData.hasQuest(Quests.TUTORIAL)) {
                        Quests.startQuest(Quests.TUTORIAL, e.getPlayer(), playerData, skrpg);
                    }
                    if (playerData.getCompletedQuests().contains(Quests.TUTORIAL)) {
                        LocalDate localDate = LocalDate.now();
                        Calendar calendar = Calendar.getInstance();
                        ZoneId zoneId = ZoneId.systemDefault();


                        calendar.setTime(playerData.getIntrestDate());
                        calendar.set(Calendar.MILLISECOND, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        if (calendar.getTime().before(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                            playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                            calendar.setTime(playerData.getIntrestDate());
                        }
                        if (calendar.getTime().equals(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                            Calendar updatedCalendar = Calendar.getInstance();
                            updatedCalendar.setTime(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                            updatedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                            playerData.getPlayerActionManager().playerDaily(updatedCalendar);
                        }
                    }
                }
            }
        }.runTaskLater(skrpg, 20);




        e.setJoinMessage(Text.color("&8[&a+&8] &r&7" + e.getPlayer().getDisplayName()));
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20.0);


    }
    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        e.setQuitMessage(Text.color("&8[&c-&8] &r&7" + e.getPlayer().getDisplayName()));
        if (playerData == null) {
            skrpg.getPlayerManager().createPlayer(e.getPlayer().getUniqueId());
        }
        playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        playerData.setLocationWhereLeft(e.getPlayer().getLocation());
            playerData.setTrade(null);
            for (NPC npc : skrpg.getNpcManager().getNpcs()) {
                if (npc.getEntityPlayer() != null) {
                    PlayerConnection playerConnection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
                    playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.getEntityPlayer()));
                    playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getEntityPlayer().getId()));
                    playerData.getRenderedNPCs().remove(npc);
                }
            }
            playerData.setRenderedNPCs(null);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(skrpg, () -> e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()), 20L);

    }
    public static void buildScoreboard(Player player, SKRPG skrpg) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team playerTeam = scoreboard.registerNewTeam("player");
        playerTeam.setColor(ChatColor.GRAY);

        playerTeam.setPrefix(Text.color("&8&l[&r&a" + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getGlobal().getGlobalLevel() + "☼&8&l] &r&7"));
        Team npcs = scoreboard.registerNewTeam("npcs");
        npcs.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Objective objective = scoreboard.registerNewObjective(Text.color("SKQuest"), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Text.color("&b⚔ &r&e&lSKQuest &r&b⚔"));
        Objective health = scoreboard.registerNewObjective(Text.color("health"), "dummy");

        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName(Text.color("&c♥"));
        health.getScore(player.getDisplayName()).setScore(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp());
        Score score = objective.getScore(" ");
        score.setScore(7);
        Team region = scoreboard.registerNewTeam("region");
        region.addEntry(ChatColor.DARK_GREEN.toString());
        region.setPrefix(Text.color("    &f۩ "));
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(6);
        Team regionOwner = scoreboard.registerNewTeam("regionOwner");
        regionOwner.addEntry(ChatColor.AQUA.toString());
        regionOwner.setPrefix(Text.color("     &f♛ &7"));
        objective.getScore(ChatColor.AQUA.toString()).setScore(5);
        Score score3 = objective.getScore("   ");
        score3.setScore(4);
        Team team = scoreboard.registerNewTeam("credits");
        team.addEntry(ChatColor.GOLD.toString());
        team.setPrefix(Text.color("&6Nuggets&f: &6"));
        team.setSuffix(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + " ");
        objective.getScore(ChatColor.GOLD.toString()).setScore(3);
        Team globalLevel = scoreboard.registerNewTeam("globalLevel");
        globalLevel.addEntry(ChatColor.GREEN.toString());
        globalLevel.setPrefix(Text.color("&aGlobal Level&f: &a"));
        globalLevel.setSuffix(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getGlobal().getGlobalLevel() + " ");
        objective.getScore(ChatColor.GREEN.toString()).setScore(2);
        Score score1 = objective.getScore("  ");
        score1.setScore(1);
        Score score2 = objective.getScore(Text.color("&eminecraft.skrafty.com"));
        score2.setScore(0);
        player.setScoreboard(scoreboard);

        playerTeam.addEntry(player.getDisplayName());
        playerTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

    }
}
