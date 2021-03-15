package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlayerJoinLeaveListener implements Listener {
    private SKRPG skrpg;
    public PlayerJoinLeaveListener(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        skrpg.getPlayerManager().loadMySQLPlayerData(e.getPlayer());

        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        buildScoreboard(e.getPlayer());
        playerData.setRegion(null);
        playerData.setSpeed(playerData.getBaseSpeed());
        new BukkitRunnable() {
            @Override
            public void run() {
                playerData.setHp(playerData.getMaxHP());
            }
        }.runTaskLater(skrpg, 20);

        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
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
                if (npc.getItemInHand() != null) {
                    List<Pair<EnumItemSlot, ItemStack>> itemList = new ArrayList<>();
                    itemList.add(Pair.of(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemBuilder(npc.getItemInHand(), 1).asItem())));
                    playerConnection.sendPacket(new PacketPlayOutEntityEquipment(npc.getEntityPlayer().getId(), itemList));
                }
                e.getPlayer().getScoreboard().getTeam("npcs").addEntry(npc.getEntityPlayer().getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.getEntityPlayer()));
                    }
                }.runTaskLater(skrpg ,50);
            }
        }
        if (!playerData.getCompletedQuests().contains(Quests.TUTORIAL)) {
            Quests.startQuest(Quests.TUTORIAL, e.getPlayer(), playerData, skrpg);
            e.getPlayer().getInventory().clear();
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
                skrpg.getLogger().info("Before");
                skrpg.getLogger().info(calendar.getTime() + "");
                skrpg.getLogger().info(Date.from(localDate.atStartOfDay(zoneId).toInstant()) + "");
                playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                calendar.setTime(playerData.getIntrestDate());
                skrpg.getLogger().info(calendar.getTime() + "");
            }
            if (calendar.getTime().equals(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                skrpg.getLogger().info(calendar.get(Calendar.DAY_OF_MONTH) + " ");
                Calendar updatedCalendar = Calendar.getInstance();
                updatedCalendar.setTime(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                updatedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                playerData.setIntrestDate(updatedCalendar.getTime());
                skrpg.getLogger().info(updatedCalendar.getTime() + " updated calendar");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
                for (Bank bank : playerData.getBanks()) {
                    playerData.setCredits(playerData.getCredits() + (int) (bank.getCredits() * 0.005));
                    Text.applyText(e.getPlayer(), "&aYou earned &6" + Math.round(bank.getCredits() * 0.005) + " Nuggets &afrom bank intrest!");
                }
            }
        }
    }
    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        e.setQuitMessage(Text.color("&8[&c-&8] &r&7" + e.getPlayer().getDisplayName()));
        playerData.setActiveQuest(null);
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
    }
    public void buildScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team npcs = scoreboard.registerNewTeam("npcs");
        npcs.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Objective objective = scoreboard.registerNewObjective(Text.color("SKRPG"), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Text.color("&b⚔ &r&e&lSKRPG &r&b⚔"));
        Score score = objective.getScore(" ");
        score.setScore(6);
        Team region = scoreboard.registerNewTeam("region");
        region.addEntry(ChatColor.GREEN.toString());
        region.setPrefix(Text.color("&aRegion&f: ۩ "));
        objective.getScore(ChatColor.GREEN.toString()).setScore(5);
        Team regionOwner = scoreboard.registerNewTeam("regionOwner");
        regionOwner.addEntry(ChatColor.AQUA.toString());
        regionOwner.setPrefix(Text.color("&7Owned By: "));
        objective.getScore(ChatColor.AQUA.toString()).setScore(4);
        Score score3 = objective.getScore("   ");
        score3.setScore(3);
        Team team = scoreboard.registerNewTeam("credits");
        team.addEntry(ChatColor.GOLD.toString());
        team.setPrefix(Text.color("&6Nuggets&f: &6"));
        team.setSuffix(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + " ");
        objective.getScore(ChatColor.GOLD.toString()).setScore(2);
        Score score1 = objective.getScore("  ");
        score1.setScore(1);
        Score score2 = objective.getScore(Text.color("&eminecraft.skrafty.com"));
        score2.setScore(0);
        player.setScoreboard(scoreboard);
    }
}
