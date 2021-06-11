package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class StashCommand implements CommandExecutor {
    private SKRPG skrpg;
    public StashCommand(SKRPG skrpg) { this.skrpg = skrpg; }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You cannot use this command as the console!");
            return false;
        }
        Player player = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        if (!playerData.getStash().isEmpty()) {
            List<SKRPGItemStack> removedItems = new ArrayList<>();
            for (SKRPGItemStack SKRPGItemStack : playerData.getStash()) {
                if (player.getInventory().firstEmpty() != -1) {
                    removedItems.add(SKRPGItemStack);
                    playerData.getPlayerActionManager().addExistingItem(SKRPGItemStack);
                }
            }
            for (SKRPGItemStack SKRPGItemStack : removedItems) {
                playerData.getStash().remove(SKRPGItemStack);
            }
        }

        Text.applyText(player, "&aPicked up your stash!");
        return false;
    }
}
