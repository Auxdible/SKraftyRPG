package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.effects.ActiveEffect;
import com.auxdible.skrpg.player.guilds.Guild;
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
        Inventory inv = Bukkit.createInventory(null, 54, "SKQuest Menu");
        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        for (int i = 9; i <= 17; i++) {
            inv.setItem(i, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        if (skrpg.getGuildManager().getPlayerGuild(playerData) != null) {
            Guild guild = skrpg.getGuildManager().getPlayerGuild(playerData);
            inv.setItem(5, new ItemBuilder(ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNjOWY4YTc0Y2EwMWJhOGM1NGRlMWVkYzgyZTFmYzA3YTgzOTIzZTY2NTc0YjZmZmU2MDY5MTkyNDBjNiJ9fX0=",
                    "3230268c-085c-4d08-92bb-8d509a191bcf")).setName("&2Your Guild").setLore(Arrays.asList(" ", "&7Guild Ranking: " + guild.getPlayersInGuild().get(playerData).getNameColored(),
                    "&7Power Level: &4" + guild.getPowerLevel(), "&7Regions Owned: &a" + guild.getOwnedRegions().size())).asItem());
        }
        inv.setItem(4, new ItemBuilder(ItemTweaker.createPlayerHead(player.getName()))
                .setName(Text.color("&aYour Statistics")).setLore(Arrays.asList(
                        Text.color("&8&m>               <"),
                        Text.color("&fMax HP: &c" + playerData.getMaxHP() + " ♥"),
                        Text.color("&fDefense: &a" + playerData.getDefence() + " ✿"),
                        Text.color("&fStrength: &4" + playerData.getStrength() + " ☄"),
                        Text.color("&fMax Energy: &e" + playerData.getMaxEnergy() + " ☢"),
                        Text.color("&fSpeed: &f" + playerData.getSpeed() + " ≈"),
                        Text.color("&fNuggets: &6" + playerData.getCredits()),
                        Text.color("&fRunic Points: &5" + playerData.getRunicPoints() + " ஐ"),
                        Text.color("&8&m>               <")
                )).asItem());
        if (!playerData.getActiveEffects().isEmpty()) {
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            for (ActiveEffect effect : playerData.getActiveEffects()) {
                int seconds = effect.getSeconds() % 60;
                int minutes = (effect.getSeconds() / 60) % 60;
                lore.add(Text.color(effect.getEffectType().getName() + " &8| &7Level &a" + effect.getLevel() + " &8| &a" + minutes + "&7:&a" + seconds));
            }
            lore.add(" ");
            inv.setItem(3, new ItemBuilder(Material.BREWING_STAND, 0).setName("&aActive Effects").setLore(lore).asItem());
        }
        inv.setItem(22, new ItemBuilder(Material.STONE_SWORD, 0).setName("&aSkills")
                .setLore(Arrays.asList(" ", Text.color("&7&oSkills are the heart of SKQuest, providing"),
                        Text.color("&7&obuffs and awards to those who do the most work."), " ")).asItem());
        if (SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) >= 25) {
            inv.setItem(30, new ItemBuilder(Material.ENCHANTING_TABLE, 0).setName("&5Runic Table").asItem());
        }
        if (SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) >= 40) {
            inv.setItem(31, new ItemBuilder(Material.SMOKER, 0).setName("&7Stovetop").asItem());
        }
        inv.setItem(20, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&aCrafting Table").asItem());
        double totalBalanceInBank = 0;
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
            inv.setItem(32, bankItem);
        }
        inv.setItem(24, new ItemBuilder(Material.SHIELD, 0).setName("&aQuests").setLore(Arrays.asList(" ", "&7&oView, and manage your current quests.", " ")).asItem());
        inv.setItem(23, new ItemBuilder(Material.CHEST, 0).setName("&aAccumulations").setLore(Arrays.asList(" ",
                Text.color("&7&oDiscover new recourses to unlock collections,"), Text.color("&7&olevel up existing collections to get more crafting recipes,"), Text.color("&7&oand MORE!"), " ")).asItem());
        inv.setItem(21, new ItemBuilder(Material.PAPER, 0).setName("&aSettings").setLore(
                Arrays.asList(" ", Text.color("&7&oChange things about your Profile."), " ")
        ).asItem());
        player.openInventory(inv);
        return false;
    }
}
