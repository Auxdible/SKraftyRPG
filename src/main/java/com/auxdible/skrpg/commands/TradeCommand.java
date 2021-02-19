package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.Trade;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeCommand implements CommandExecutor {
    private SKRPG skrpg;
    public TradeCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this in game!");
        }
        Player player = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (!playerData.canTrade()) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            Text.applyText(player, "&cYou have trades disabled!");
            return false;
        }
        if (args.length == 0) {
            Text.applyText(player, "&cValid Arguments: accept, deny, online_player_name");
        } else if (args[0].equals("accept")) {
            if (skrpg.getTradeManager().getTrade(player) != null) {
                playerData.setTrade(skrpg.getTradeManager().getTrade(player));
                skrpg.getTradeManager().getTrade(player).setPlayer2(player);
                skrpg.getTradeManager().removeTrade(player, skrpg.getTradeManager().getTrade(player));
                playerData.getTrade().start(skrpg);
                Text.applyText(player, "&aTrade accepted!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            } else {
                Text.applyText(player, "&cThere is no active trade invite!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
        } else if (args[0].equals("deny")) {
            if (skrpg.getTradeManager().getTrade(player) != null) {
                playerData.setTrade(null);
                skrpg.getTradeManager().removeTrade(player, skrpg.getTradeManager().getTrade(player));
                Text.applyText(player, "&cTrade denied!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);

            } else {
                Text.applyText(player, "&cThere is no active trade invite!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return false;
            }
        } else if (Bukkit.getPlayer(args[0]) != null) {
            if (!skrpg.getPlayerManager().getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId()).canTrade()) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                Text.applyText(player, "&cThis player has trades disabled!");
                return false;
            }
            Trade trade = new Trade(player, null);
            playerData.setTrade(trade);
            skrpg.getTradeManager().addTrade(Bukkit.getPlayer(args[0]), trade);
            Text.applyText(player, "&aSent trade request to " + Bukkit.getPlayer(args[0]).getDisplayName() + ".");
            Text.applyText(Bukkit.getPlayer(args[0]), "&aYou recieved a trade request from " + player.getName() + "!");
            Text.applyTextAdvanced(Bukkit.getPlayer(args[0]), "&a&l[ACCEPT]", "&aClick to accept!", "/trade accept");
            Text.applyTextAdvanced(Bukkit.getPlayer(args[0]), "&c&l[DENY]", "&cClick to deny!", "/trade deny");
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        skrpg.getTradeManager().removeTrade(Bukkit.getPlayer(args[0]), trade);
                        Text.applyText(Bukkit.getPlayer(args[0]), "&c" + player.getDisplayName() + " left! Trade cancelled!");
                        playerData.setTrade(null);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }
                    if (Bukkit.getPlayer(args[0]) == null) {
                        skrpg.getTradeManager().removeTrade(Bukkit.getPlayer(args[0]), trade);
                        Text.applyText(player, "&cYour trade wasn't accepted in time or was denied!");
                        playerData.setTrade(null);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }
                    if (seconds == 30) {
                        skrpg.getTradeManager().removeTrade(Bukkit.getPlayer(args[0]), trade);
                        Text.applyText(player, "&cYour trade wasn't accepted in time or was denied!");
                        Text.applyText(Bukkit.getPlayer(args[0]), "&cYour trade wasn't accepted in time or was denied!");
                        playerData.setTrade(null);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    }

                    if (skrpg.getTradeManager().getTrade(Bukkit.getPlayer(args[0])) == null && playerData.getTrade() == null) {
                        skrpg.getTradeManager().removeTrade(Bukkit.getPlayer(args[0]), trade);
                        Text.applyText(player, "&cYour trade wasn't accepted in time or was denied!");
                        Text.applyText(Bukkit.getPlayer(args[0]), "&cYour trade wasn't accepted in time or was denied!");
                        playerData.setTrade(null);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        cancel();
                    } else if (skrpg.getTradeManager().getTrade(Bukkit.getPlayer(args[0])) == null) { cancel(); }

                    seconds++;
                }
            }.runTaskTimer(skrpg, 0, 20);
        } else {
            Text.applyText(player, "&cValid Arguments: accept, deny, online_player_name");
        }
        return false;
    }
}
