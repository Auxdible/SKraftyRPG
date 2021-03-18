package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuCommand implements CommandExecutor {
    private SKRPG skrpg;
    public MenuCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this command!");
        }
        Player player = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 54, "SKRPG Menu");
        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(11, new ItemBuilder(ItemTweaker.createPlayerHead(player.getName()))
                .setName(Text.color("&aYour Statistics")).setLore(Arrays.asList(
                        Text.color("&8&m>               <"),
                        Text.color("&fMax HP: &c" + playerData.getMaxHP() + " ♥"),
                        Text.color("&fDefence: &a" + playerData.getDefence() + " ✿"),
                        Text.color("&fStrength: &4" + playerData.getStrength() + " ☄"),
                        Text.color("&fMax Energy: &e" + playerData.getMaxEnergy() + " ☢"),
                        Text.color("&fSpeed: &f" + playerData.getSpeed() + " ≈"),
                        Text.color("&fNuggets: &6" + playerData.getCredits()),
                        Text.color("&fRunic Points: &5" + playerData.getRunicPoints() + " ஐ"),
                        Text.color("&8&m>               <")
                )).asItem());
        ItemStack itemHelmet = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO HELMET").asItem();
        if (player.getInventory().getHelmet().getType() != Material.AIR) {
            itemHelmet = player.getInventory().getHelmet();
        }
        ItemStack itemChestplate = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO CHESTPLATE").asItem();
        if (player.getInventory().getChestplate().getType() != Material.AIR) {
            itemChestplate = player.getInventory().getChestplate();
        }
        ItemStack itemLeggings = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO LEGGINGS").asItem();
        if (player.getInventory().getLeggings().getType() != Material.AIR) {
            itemLeggings = player.getInventory().getLeggings();
        }
        ItemStack itemBoots = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO BOOTS").asItem();
        if (player.getInventory().getBoots().getType() != Material.AIR) {
            itemBoots = player.getInventory().getBoots();
        }
        ItemStack item = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO ITEM").asItem();
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            item = player.getInventory().getItemInMainHand();
        }
        inv.setItem(10, itemHelmet);
        inv.setItem(20, item);
        inv.setItem(19, itemChestplate);
        inv.setItem(28, itemLeggings);
        inv.setItem(37, itemBoots);
        inv.setItem(29, new ItemBuilder(Material.STONE_SWORD, 0).setName("&aSkills")
                .setLore(Arrays.asList(" ", Text.color("&7&oSkills are the heart of SKRPG, providing"),
                        Text.color("&7&obuffs and awards to those who do the most work."), " ")).asItem());
        if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) >= 25) {
            inv.setItem(22, new ItemBuilder(Material.ENCHANTING_TABLE, 0).setName("&5Runic Table").asItem());
        }
        inv.setItem(13, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&aCrafting Table").asItem());
        int totalBalanceInBank = 0;
        boolean hasBankLevel = false;
        List<String> lore = new ArrayList<>();

        lore.add(" ");
        for (int i = 0; i < playerData.getBanks().size(); i++) {
            Bank bank = playerData.getBanks().get(i);
            totalBalanceInBank = totalBalanceInBank + bank.getCredits();
            lore.add(Text.color("&7Bank &a" + (playerData.getBanks().indexOf(bank) + 1)));
            lore.add(" ");
            lore.add(Text.color("&7Balance: &6" + bank.getCredits()));
            lore.add(Text.color("&7Level: " + bank.getLevel().getNameColored()));
            lore.add(" ");
            if (bank.getLevel().getLevel() >= BankLevel.DELUXE.getLevel()) {
                hasBankLevel = true;
            }
        }
        if (hasBankLevel) {
            ItemStack bankItem = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3NWQxYjc4NWQxOGQ0N2IzZWE4ZjBhN2UwZmQ0YTFmYWU5ZTdkMzIzY2YzYjEzOGM4Yzc4Y2ZlMjRlZTU5In19fQ==", "1d2bf3fe-1b67-495f-995d-435693e90fa0");
            ItemMeta iM = bankItem.getItemMeta();
            iM.setDisplayName(Text.color("&aManage Banks"));
            lore.add(" ");
            lore.add(Text.color("&7Total Balance: &6" + totalBalanceInBank + " Nuggets"));
            iM.setLore(lore);
            bankItem.setItemMeta(iM);
            inv.setItem(31, bankItem);
        }
        inv.setItem(38, new ItemBuilder(Material.CHEST, 0).setName("&aCollections").setLore(Arrays.asList(" ",
                Text.color("&7&oDiscover new recourses to unlock collections,"), Text.color("&7&olevel up existing collections to get more crafting recipes,"), Text.color("&7&oand MORE!"), " ")).asItem());
        inv.setItem(40, new ItemBuilder(Material.PAPER, 0).setName("&aSettings").setLore(
                Arrays.asList(" ", Text.color("&7&oChange things about your Profile."), " ")
        ).asItem());
        player.openInventory(inv);
        return false;
    }
}
