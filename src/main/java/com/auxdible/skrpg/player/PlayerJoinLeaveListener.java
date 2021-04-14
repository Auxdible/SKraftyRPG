package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.actions.PlayerAction;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestDifficulty;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuestType;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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

        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) {
            skrpg.getPlayerManager().createPlayer(e.getPlayer().getUniqueId());
        }
        new BukkitRunnable() {

            @Override
            public void run() {

                if (playerData != null) {
                    buildScoreboard(e.getPlayer(), skrpg);
                    playerData.setRenderedNPCs(new ArrayList<>());
                    playerData.setRegion(null);
                    playerData.setSpeed(playerData.getBaseSpeed());
                    playerData.setPlayerAction(new PlayerAction(e.getPlayer(), playerData, skrpg));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playerData.setHp(playerData.getMaxHP());
                        }
                    }.runTaskLater(skrpg, 40);
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
                            playerData.setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                            calendar.setTime(playerData.getIntrestDate());
                        }
                        if (calendar.getTime().equals(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                            Calendar updatedCalendar = Calendar.getInstance();
                            updatedCalendar.setTime(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                            updatedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                            playerData.setIntrestDate(updatedCalendar.getTime());
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 0.25f);
                            for (Bank bank : playerData.getBanks()) {
                                playerData.setCredits(playerData.getCredits() + (double) Math.round(((bank.getCredits() * 0.005) * 100) / 100));
                                Text.applyText(e.getPlayer(), "&aYou earned &6" + (double) Math.round(((bank.getCredits() * 0.005) * 100) / 100) + " Nuggets &afrom bank intrest!");
                            }
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.4f);
                                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.0f, 0.2f);
                                    Text.applyText(e.getPlayer(), "&aYour &6♛ Royalty Quests &aare refreshed!");
                                    Text.applyText(e.getPlayer(), " ");
                                    List<RoyaltyQuestType> royaltyQuestTypes = new ArrayList<>(EnumSet.allOf(RoyaltyQuestType.class));
                                    for (int i = 0; i <= playerData.getRoyaltyQuestSlots(); i++) {
                                        int randomQuest = new Random().nextInt(royaltyQuestTypes.size());
                                        List<RoyaltyQuestDifficulty> validDifficulties = new ArrayList<>();

                                        for (RoyaltyQuestDifficulty royaltyQuestDifficulty : EnumSet.allOf(RoyaltyQuestDifficulty.class)) {
                                            if (SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) >= SKRPG.levelToInt(royaltyQuestDifficulty.getMinLevel().toString())) {
                                                validDifficulties.add(royaltyQuestDifficulty);
                                            }
                                        }
                                        int randomDifficulty = new Random().nextInt(validDifficulties.size());
                                        RoyaltyQuest royaltyQuest = new RoyaltyQuest(0,
                                                royaltyQuestTypes.get(randomQuest).getAmountNeeded() * (validDifficulties.get(randomDifficulty).getPriority() + 1), royaltyQuestTypes.get(randomQuest), validDifficulties.get(randomDifficulty));
                                        Text.applyText(e.getPlayer(), "&8&l>  &r&6" + royaltyQuest.getRoyaltyQuestType().getName() + " &8&l| &r" + royaltyQuest.getDifficulty().getColoredName());
                                        Text.applyText(e.getPlayer(), "&7" + royaltyQuest.getRoyaltyQuestType().getObjective());
                                        Text.applyText(e.getPlayer(), " ");
                                        Text.applyText(e.getPlayer(), "&6" + royaltyQuest.getProgressInteger() + "&7/&6" + royaltyQuest.getAmountNeeded());

                                        playerData.getRoyaltyQuests().add(royaltyQuest);

                                    }

                                }
                            }.runTaskLater(skrpg, 40);
                        }
                    }
                }
            }
        }.runTaskLater(skrpg, 20);



        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        e.setJoinMessage(Text.color("&8[&a+&8] &r&7" + e.getPlayer().getDisplayName()));
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20.0);

        for (org.bukkit.inventory.ItemStack itemStack : Arrays.asList(e.getPlayer().getInventory().getContents())) {
            if (itemStack != null) {
                ItemInfo stackInfo = ItemInfo.parseItemInfo(itemStack);
                if (stackInfo == null) { e.getPlayer().getInventory().remove(itemStack); }
            }
        }
    }
    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
        e.setQuitMessage(Text.color("&8[&c-&8] &r&7" + e.getPlayer().getDisplayName()));
        if (playerData == null) {
            skrpg.getPlayerManager().createPlayer(e.getPlayer().getUniqueId());
        }
        playerData = skrpg.getPlayerManager().getPlayerData(e.getPlayer().getUniqueId());
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
            playerData.setRenderedNPCs(null);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(skrpg, () -> e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()), 20L);
    }
    public static void buildScoreboard(Player player, SKRPG skrpg) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team playerTeam = scoreboard.registerNewTeam("player");
        playerTeam.setColor(ChatColor.GRAY);
        playerTeam.setPrefix(Text.color("&8&l[&r&e" + SKRPG.levelToInt(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCombat().getLevel().toString()) + "⚔&8&l] &r&7"));
        Team npcs = scoreboard.registerNewTeam("npcs");
        npcs.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        Objective objective = scoreboard.registerNewObjective(Text.color("SKRPG"), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Text.color("&b⚔ &r&e&lSKRPG &r&b⚔"));
        Objective health = scoreboard.registerNewObjective(Text.color("health"), "dummy");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName(Text.color("&c♥"));
        health.getScore(player.getDisplayName()).setScore(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp());
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

        playerTeam.addEntry(player.getDisplayName());
        playerTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    }
}
