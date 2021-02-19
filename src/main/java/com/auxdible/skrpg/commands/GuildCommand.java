package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.Trade;
import com.auxdible.skrpg.player.guilds.Guild;
import com.auxdible.skrpg.player.guilds.GuildPermissions;
import com.auxdible.skrpg.player.guilds.GuildRank;
import com.auxdible.skrpg.player.guilds.raid.Raid;
import com.auxdible.skrpg.player.guilds.raid.RaidMob;
import com.auxdible.skrpg.player.guilds.raid.RaidMobs;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import com.google.common.base.Enums;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.*;

public class GuildCommand implements CommandExecutor {
    private SKRPG skrpg;
    public GuildCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You need to be a player in order to use this!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        Guild guild = skrpg.getGuildManager().getPlayerGuild(playerData);
        if (SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) < 10) {
            Text.applyText(p, "&cYou need to be Combat 10 to unlock guilds!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            return false;
        }
        if (args.length == 0) {
            p.performCommand("guild help");
        } else if (args[0].equalsIgnoreCase("help")) {
            Text.applyText(p, "&8&m>        &r&7☫ &l&2GUILDS HELP &r&7☫&8&m        <");
            Text.applyTextWithHover(p, "&2• &7/guild", "&7Access the guild help menu");
            Text.applyTextWithHover(p, "&2• &7/guild create (name)", "&7Create a guild.");
            Text.applyTextWithHover(p, "&2• &7/guild kick (player)", "&7Kick a player from the guild.");
            Text.applyTextWithHover(p, "&2• &7/guild invite (player)", "&7Invite a player to the guild.");
            Text.applyTextWithHover(p, "&2• &7/guild accept/deny", "&7Accept/Deny a guild invite.");
            Text.applyTextWithHover(p, "&2• &7/guild promote (player)", "&7Promote a player up one rank.");
            Text.applyTextWithHover(p, "&2• &7/guild demote (player)", "&7Demote a player down one rank.");
            Text.applyTextWithHover(p, "&2• &7/guild leave", "&7Leave your guild.");
            Text.applyTextWithHover(p, "&2• &7/guild disband", "&7Disband your guild. &c[This will delete your guild FOREVER!]");
            Text.applyTextWithHover(p, "&2• &7/guild chat (message)", "&7Type a message out to everyone in your guild.");
            Text.applyTextWithHover(p, "&2• &7/guild sc (message)", "&7Type a message to all the staff in your guild.");
            Text.applyTextWithHover(p, "&2• &7/guild raid (buy/leave)", "&7Raid a region or manage your current raid.");
            Text.applyText(p, "&8&m>                                  <");
        } else if (args[0].equalsIgnoreCase("menu")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            Inventory inv = Bukkit.createInventory(null, 54, Text.color("&7☫ &2Guild Menu &7☫ &8| &r" + guild.getName()));
            for (int i = 0; i <= 53; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName("").asItem());
            }
            ArrayList<PlayerData> guildPlayers = new ArrayList<>(guild.getPlayersInGuild().keySet());
            for (int i = 0; i <= 8; i++) {
                if (i + 1 > guildPlayers.size()) {
                    inv.setItem(i + 9, null);
                } else if (guildPlayers.get(i) != null) {
                    ItemStack playerHead = ItemTweaker.createPlayerHead(Bukkit.getPlayer(guildPlayers.get(i).getUuid())
                            .getDisplayName());
                    ItemMeta iM = playerHead.getItemMeta();
                    iM.setDisplayName(Text.color(guild.getPlayersInGuild().get(guildPlayers.get(i)).getColor() +
                            Bukkit.getOfflinePlayer(guildPlayers.get(i).getUuid()).getName()));
                    List<String> lore = Arrays.asList(" ", Text.color("&7Guild Rank: " + guild.getPlayersInGuild().get(guildPlayers.get(i)).getNameColored()),
                            Text.color("&7Combat Level: &6" + SKRPG.levelToInt(guildPlayers.get(i).getCombat().getLevel().toString())), " ");
                    iM.setLore(lore);
                    playerHead.setItemMeta(iM);
                    inv.setItem(i + 9, playerHead);
                }

            }
            inv.setItem(31, new ItemBuilder(Material.IRON_SWORD, 0).setName("&7Power Level: &c" + guild.getPowerLevel()).asItem());
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.KICK_PLAYER)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission KICK_PLAYER to use this!");
                return false;
            }
            if (args[1] != null) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    if (guild.getPlayersInGuild().keySet().contains(skrpg.getPlayerManager()
                            .getPlayerData(Bukkit.getPlayer(args[1]).getUniqueId()))) {
                        guild.removePlayer(skrpg.getPlayerManager()
                                .getPlayerData(Bukkit.getPlayer(args[1]).getUniqueId()));
                        if (Bukkit.getPlayer(args[1]).isOnline()) {
                            Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[1]).getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                            Text.applyText(Bukkit.getPlayer(args[1]), "&cYou were kicked from " + guild.getName() + "!");
                        }
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                        Text.applyText(p, "&cKicked player " + args[1] + " !");
                    }
                }
            }

        } else if (args[0].equalsIgnoreCase("invite")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.INVITE_PLAYER)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission INVITE_PLAYER to use this!");
                return false;
            }
            if (guild.getPlayersInGuild().size() == 9) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cThere are too many players in your guild!");
                return false;
            }
            if (args[1] == null) {
                Text.applyText(p, "&cPlease enter a player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                Text.applyText(p, "&cPlease enter a valid player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (skrpg.getGuildManager().getPlayerGuild(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1]).getUniqueId())) != null) {
                Text.applyText(p, "&cThis player is already in a guild!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            skrpg.getGuildInviteManager().addInvite(Bukkit.getPlayer(args[1]), guild);
            Text.applyText(p, "&aSent guild invite request to " + Bukkit.getPlayer(args[1]).getDisplayName() + ".");
            Text.applyText(Bukkit.getPlayer(args[1]), "&aYou recieved a guild invite from " + p.getName() + " to join " + guild.getName() + "&a!");
            Text.applyTextAdvanced(Bukkit.getPlayer(args[1]), "&a&l[ACCEPT]", "&aClick to accept!", "/guild accept");
            Text.applyTextAdvanced(Bukkit.getPlayer(args[1]), "&c&l[DENY]", "&cClick to deny!", "/guild deny");
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    if (!p.isOnline()) {
                        skrpg.getGuildInviteManager().removeInvite(Bukkit.getPlayer(args[1]), guild);
                        Text.applyText(Bukkit.getPlayer(args[1]), "&c" + p.getDisplayName() + " left! Guild invite cancelled!");
                        cancel();
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        skrpg.getGuildInviteManager().removeInvite(Bukkit.getPlayer(args[1]), guild);
                        Text.applyText(p, "&cYour guild invite wasn't accepted in time or was denied!");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }
                    if (seconds == 30) {
                        skrpg.getGuildInviteManager().removeInvite(Bukkit.getPlayer(args[1]), guild);
                        Text.applyText(p, "&cYour guild invite wasn't accepted in time or was denied!");
                        Text.applyText(Bukkit.getPlayer(args[1]), "&cYour guild invite wasn't accepted in time or was denied!");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }

                    if (skrpg.getGuildInviteManager().getInvite(Bukkit.getPlayer(args[1])) == null) {
                        skrpg.getGuildInviteManager().removeInvite(Bukkit.getPlayer(args[1]), guild);
                        Text.applyText(p, "&cYour guild invite wasn't accepted in time or was denied!");
                        Text.applyText(Bukkit.getPlayer(args[1]), "&cYour guild invite wasn't accepted in time or was denied!");
                        
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }

                    seconds++;
                }
            }.runTaskTimer(skrpg, 0, 20);
        } else if (args[0].equalsIgnoreCase("deny")) {
            if (skrpg.getGuildInviteManager().getInvite(p) != null) {
                skrpg.getGuildInviteManager().removeInvite(p, skrpg.getGuildInviteManager().getInvite(p));
                Text.applyText(p, "&cGuild invite denied!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            } else {
                Text.applyText(p, "&cThere is no active guild invite!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (skrpg.getGuildInviteManager().getInvite(p) != null) {
                skrpg.getGuildInviteManager().getInvite(p).addPlayer(playerData);
                skrpg.getGuildInviteManager().removeInvite(p, skrpg.getGuildInviteManager().getInvite(p));
                Text.applyText(p, "&aGuild invite accepted! Do /g menu to view info about your guild!");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            } else {
                Text.applyText(p, "&cThere is no active guild invite!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args[1] == null) {
                Text.applyText(p, "&cYou need to enter a name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (skrpg.getGuildManager().getPlayerGuild(skrpg.getPlayerManager().getPlayerData(p.getUniqueId())) != null) {
                Text.applyText(p, "&cYou are already in a guild!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            skrpg.getGuildManager().createGuild(playerData, Text.color(args[1]).replace("_", " "));
            Text.applyText(p, "&aYou have created the guild " + Text.color(args[1]).replace("_", " ") + " !");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
            return false;
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.CHANGE_PERMISSIONS)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission CHANGE_PERMISSIONS to use this!");
                return false;
            }
            if (args[1] == null) {
                Text.applyText(p, "&cPlease enter a player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                Text.applyText(p, "&cPlease enter a valid player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getPlayersInGuild().keySet()
                    .contains(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1]).getUniqueId()))) {
                Text.applyText(p, "&cThis player is not in your guild!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (guild.getPlayersInGuild()
                    .get(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId())).getPriority() >= guild.getPlayersInGuild().get(playerData).getPriority()) {
                Text.applyText(p, "&cYou cannot promote a player of this rank!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            for (GuildRank guildRank : EnumSet.allOf(GuildRank.class)) {
                if (guild.getPlayersInGuild()
                        .get(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                                .getUniqueId())).getPriority() + 1 == guildRank.getPriority()) {
                    if (guildRank == guild.getPlayersInGuild().get(playerData)) {
                        Text.applyText(p, "&cYou cannot promote a player to your rank!");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        return false;
                    }
                    guild.getPlayersInGuild().remove(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId()));
                    guild.getPlayersInGuild().put(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId()), guildRank);
                    Text.applyText(p, "&aPromoted this player to " + guildRank.getNameColored() + "&a!");
                    return false;
                }
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.CHANGE_PERMISSIONS)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission CHANGE_PERMISSIONS to use this!");
                return false;
            }
            if (args[1] == null) {
                Text.applyText(p, "&cPlease enter a player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                Text.applyText(p, "&cPlease enter a valid player name!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (!guild.getPlayersInGuild().keySet()
                    .contains(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1]).getUniqueId()))) {
                Text.applyText(p, "&cThis player is not in your guild!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (guild.getPlayersInGuild()
                    .get(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId())).getPriority() >= guild.getPlayersInGuild().get(playerData).getPriority()) {
                Text.applyText(p, "&cYou cannot demote a player of this rank!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (guild.getPlayersInGuild().get(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                    .getUniqueId())).equals(GuildRank.MEMBER)) {
                Text.applyText(p, "&cYou cannot demote a member!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            for (GuildRank guildRank : EnumSet.allOf(GuildRank.class)) {
                if (guild.getPlayersInGuild()
                        .get(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                                .getUniqueId())).getPriority() - 1 == guildRank.getPriority()) {
                    guild.getPlayersInGuild().remove(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId()));
                    guild.getPlayersInGuild().put(skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[1])
                            .getUniqueId()), guildRank);
                    Text.applyText(p, "&aDemoted this player to " + guildRank.getNameColored() + "&a!");
                    return false;
                }
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (guild.getPlayersInGuild().get(playerData) == GuildRank.OWNER) {
                Text.applyText(p, "&cYou are the guild owner! Do /guild disband instead.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            Text.applyText(p, "&aYou left your guild.");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
            for (PlayerData playerData1 : guild.getPlayersInGuild().keySet()) {
                if (Bukkit.getPlayer(playerData1.getUuid()) != null) {
                    Text.applyText(Bukkit.getPlayer(playerData1.getUuid()), "&c" + p.getDisplayName() + " left your guild.");
                    Bukkit.getPlayer(playerData1.getUuid()).playSound(Bukkit.getPlayer(playerData1.getUuid()).getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                }
            }
            guild.removePlayer(playerData);
        } else if (args[0].equalsIgnoreCase("disband")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (guild.getPlayersInGuild().get(playerData) != GuildRank.OWNER) {
                Text.applyText(p, "&cYou need to be a guild owner to do this!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            for (PlayerData playerData1 : guild.getPlayersInGuild().keySet()) {
                if (Bukkit.getPlayer(playerData1.getUuid()) != null) {
                    Text.applyText(Bukkit.getPlayer(playerData1.getUuid()), "&c" + p.getDisplayName() + " disbanded your guild.");
                    Bukkit.getPlayer(playerData1.getUuid()).playSound(Bukkit.getPlayer(playerData1.getUuid()).getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                }
            }
            skrpg.getGuildManager().deleteGuild(guild.getId());
            Text.applyText(p, "&cYou disbanded your guild.");
            return false;
        } else if (args[0].equalsIgnoreCase("raid")) {
            if (guild == null) {
                Text.applyText(p, "&cYou need a guild in order to use this command! &cDo /g create (name) or ask for someone to invite you.");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            if (skrpg.getRaidManager().getRaid(p) != null) {
                if (args[1] != null) {
                    if (args[1].equalsIgnoreCase("buy")) {
                        Inventory inv = Bukkit.createInventory(null, 54, "Raid Mob Shop");
                        List<RaidMobs> raidMobsList = Arrays.asList(RaidMobs.values());
                            for (RaidMobs raidMobs : raidMobsList) {
                                inv.setItem(raidMobsList.indexOf(raidMobs), new ItemBuilder(Material.ROTTEN_FLESH, 0)
                                        .setName(raidMobs.getName()).setLore(Arrays.asList(" ", Text.color("&7Cost: &b" + raidMobs.getCreditsCost()),
                                                Text.color("&7Skill Level Required: &c" + raidMobs.getSkillLevelRequired()), " ")).asItem());
                            }
                            p.openInventory(inv);
                        } else if (args[1].equalsIgnoreCase("leave")) {
                        skrpg.getRaidManager().getRaid(p).fail(skrpg);
                        return false;
                    } else {
                        Text.applyText(p, "&cValid arguments: buy, leave");
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    }
                } else {
                    Text.applyText(p, "&cYou are currently in a raid! Valid arguments: buy, leave.");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                }
                return false;
            }
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.RAID)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission RAID to use this!");
                return false;
            }
            if (playerData.getRegion() == null) {
                Text.applyText(p, "&cYou are not in a region!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
            Raid raid = new Raid(p, skrpg.getGuildManager().getPlayerGuild(playerData), playerData.getRegion());
            raid.start(skrpg);
            skrpg.getRaidManager().addRaid(raid, p);
        } else if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("sc")) {
            if (!guild.getGuildPermissions().get(guild.getPlayersInGuild().get(playerData))
                    .contains(GuildPermissions.CHAT)) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cYou need permission CHAT to use this!");
                return false;
            }
            if (args.length == 1) {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
                Text.applyText(p, "&cPlease enter a message!");
                return false;
            }
            StringBuilder message = new StringBuilder();
            for (int i = 1; i != args.length; i++) {
                message.append(args[i]).append(" ");
            }
            if (args[0].equalsIgnoreCase("sc")) {
                guild.staffMessage(p, message.toString());
            } else { guild.guildMessage(p, message.toString()); }
        }
        return false;
    }
}
