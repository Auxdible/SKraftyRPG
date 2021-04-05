package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.TradeItem;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
            List<TradeItem> removedItems = new ArrayList<>();
            for (TradeItem tradeItem : playerData.getStash()) {
                if (player.getInventory().firstEmpty() != -1) {
                    removedItems.add(tradeItem);
                    playerData.getPlayerActionManager().addExistingItem(tradeItem);
                }
            }
            for (TradeItem tradeItem : removedItems) {
                playerData.getStash().remove(tradeItem);
            }
        }

        Text.applyText(player, "&aPicked up your stash!");
        return false;
    }
}
