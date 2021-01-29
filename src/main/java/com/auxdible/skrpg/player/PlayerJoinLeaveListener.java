package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public class PlayerJoinLeaveListener implements Listener {
    private SKRPG skrpg;
    public PlayerJoinLeaveListener(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        if (skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId()) == null) {
            skrpg.getPlayerManager().createPlayer(e.getPlayer().getUniqueId());
        }
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        buildScoreboard(e.getPlayer());
        playerData.setRegion(null);
        playerData.setSpeed(playerData.getBaseSpeed());
        playerData.setHp(playerData.getMaxHP());
        e.setJoinMessage(Text.color("&8[&a+&8] &r&7" + e.getPlayer().getDisplayName()));
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20.0);
        for (NPC npc : skrpg.getNpcManager().getNpcs()) {
            if (npc.getEntityPlayer() != null) {
                PlayerConnection playerConnection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
                playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo
                        .EnumPlayerInfoAction.ADD_PLAYER, npc.getEntityPlayer()));
                playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc.getEntityPlayer()));
                DataWatcher watcher = npc.getEntityPlayer().getDataWatcher();
                Integer byteInt = 127;
                watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), byteInt.byteValue());

                playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getEntityPlayer().getId(),
                        npc.getEntityPlayer().getDataWatcher(), true));
                playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc.getEntityPlayer(), (byte) (
                        npc.getLocation().getYaw() * 256 / 360)));
                e.getPlayer().getScoreboard().getTeam("npcs").addEntry(npc.getEntityPlayer().getName());
            }
        }
        LocalDate localDate = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        ZoneId zoneId = ZoneId.systemDefault();

        if (localDate.getYear() > calendar.get(Calendar.YEAR)) {
            playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
        }
        if (localDate.getMonthValue() > calendar.get(Calendar.MONTH) + 1) {
            playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
        }
        if (localDate.getDayOfMonth() > calendar.get(Calendar.DAY_OF_MONTH) && localDate.getMonthValue() == calendar.get(Calendar.MONTH) + 1) {

            playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
        }

        calendar.setTime(playerData.getIntrestDate());
        skrpg.getLogger().info("Date: " + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.MONTH) + " " + localDate.getMonthValue() + " " + localDate.getDayOfMonth());
        if (localDate.getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH)) {
            skrpg.getLogger().info(calendar.get(Calendar.DAY_OF_MONTH) + " ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            playerData.setIntrestDate(calendar.getTime());
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
            for (Bank bank : playerData.getBanks()) {
                playerData.setCredits(playerData.getCredits() + (int) (bank.getCredits() * 0.005));
                Text.applyText(e.getPlayer(), "&aYou earned &b" + (bank.getCredits() * 0.005) + " C$ &afrom bank intrest!");
            }
        }
    }
    public void buildScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(Text.color("SKRPG"), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Text.color("&e&lSKRPG"));
        Team npcs = scoreboard.registerNewTeam("npcs");
        npcs.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Score score = objective.getScore(" ");
        score.setScore(5);
        Team region = scoreboard.registerNewTeam("region");
        region.addEntry(ChatColor.GREEN.toString());
        region.setPrefix(Text.color("&aRegion&f: ۩ "));
        objective.getScore(ChatColor.GREEN.toString()).setScore(4);
        Score score3 = objective.getScore("   ");
        score3.setScore(3);
        Team team = scoreboard.registerNewTeam("credits");
        team.addEntry(ChatColor.AQUA.toString());
        team.setPrefix(Text.color("&bCredits&f: &b"));
        team.setSuffix(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + " ");
        objective.getScore(ChatColor.AQUA.toString()).setScore(2);
        Score score1 = objective.getScore("  ");
        score1.setScore(1);
        Score score2 = objective.getScore(Text.color("&eEnjoy the test! :)"));
        score2.setScore(0);
        player.setScoreboard(scoreboard);
    }
}
