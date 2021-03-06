package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SettingsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SettingsCommand(SKRPG skrpg) { this.skrpg = skrpg; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this!");
            return false;
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 45, "Settings");
        for (int i = 0; i <= 44; i++) {
            inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        if (playerData.getSellAboveRarity() == Rarity.COMMON) {
            inv.setItem(10, new ItemBuilder(Material.BARRIER, 0).setName("&fDo not Sell Beyond This Rarity")
                    .setLore(Arrays.asList(" ", Text.color("&7Rarity: &cNONE")))
                    .asItem());

        } else if (playerData.getSellAboveRarity() == Rarity.UNCOMMON) {
            inv.setItem(10, new ItemBuilder(Material.LIME_DYE, 0).setName("&aDo not Sell Beyond This Rarity")
                    .setLore(Arrays.asList(" ", Text.color("&7Rarity: " + playerData.getSellAboveRarity().getNameColored())))
                    .asItem());
        } else if (playerData.getSellAboveRarity() == Rarity.RARE) {
            inv.setItem(10, new ItemBuilder(Material.BLUE_DYE, 0).setName("&1Do not Sell Beyond This Rarity")
                    .setLore(Arrays.asList(" ", Text.color("&7Rarity: " + playerData.getSellAboveRarity().getNameColored())))
                    .asItem());
        } else if (playerData.getSellAboveRarity() == Rarity.EPIC) {
            inv.setItem(10, new ItemBuilder(Material.PURPLE_DYE, 0).setName("&5Do not Sell Beyond This Rarity")
                    .setLore(Arrays.asList(" ", Text.color("&7Rarity: " + playerData.getSellAboveRarity().getNameColored())))
                    .asItem());
        } else if (playerData.getSellAboveRarity() == Rarity.LEGENDARY) {
            inv.setItem(10, new ItemBuilder(Material.YELLOW_DYE, 0).setName("&eDo not Sell Beyond This Rarity")
                    .setLore(Arrays.asList(" ", Text.color("&7Rarity: &aALL")))
                    .asItem());
        }
        inv.setItem(31, new ItemBuilder(Material.REDSTONE_BLOCK, 0).setName("&c&lReset My Stats").setLore(Arrays.asList(" ",
                Text.color("&c&lWARNING! &r&cDo not click this "),
                Text.color("&cunless you are absolutely sure!"), " ")).asItem());
        if (playerData.canTrade()) {
            inv.setItem(13, new ItemBuilder(Material.GOLD_NUGGET, 0)
                    .setName("&aToggle Trading")
                    .setLore(Arrays.asList(" ", Text.color("&aYou have trading enabled!"), " ")).asItem());
        } else {
            inv.setItem(13, new ItemBuilder(Material.NETHERITE_INGOT, 0)
                    .setName("&cToggle Trading")
                    .setLore(Arrays.asList(" ", Text.color("&cYou have trading disabled!"), " ")).asItem());
        }
        if (playerData.canDrop()) {
            inv.setItem(16, new ItemBuilder(Material.FEATHER, 0)
                    .setName("&aToggle Dropping Items")
                    .setLore(Arrays.asList(" ", Text.color("&aYou have dropping items enabled!"), " ")).asItem());
        } else {
            inv.setItem(16, new ItemBuilder(Material.ANVIL, 0)
                    .setName("&cToggle Dropping Items")
                    .setLore(Arrays.asList(" ", Text.color("&cYou have dropping items disabled!"), " ")).asItem());
        }
        p.openInventory(inv);
        return false;
    }
}
