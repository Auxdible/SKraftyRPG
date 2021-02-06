package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class SkillsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SkillsCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this command!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 36, "Your Skills");
        for (int i = 0; i <= 35; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(13, new ItemBuilder(Material.DIAMOND_SWORD, 0).setName("&7Combat &6" +
                playerData.getCombat().getLevel().toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getCombat().getXpTillNext() + "&7/&6" +
                        Level.valueOf("_" +
                                (Integer.parseInt(playerData.getCombat().getLevel().toString()
                                        .replace("_", "")) + 1)).getXpRequired()),
                        Text.color("&7Total Combat XP: &6" + playerData.getCombat().getTotalXP()))).asItem());
        inv.setItem(12, new ItemBuilder(Material.DIAMOND_PICKAXE, 0).setName("&7Mining &6" +
                playerData.getMining().getLevel().toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getMining().getXpTillNext() + "&7/&6" +
                                Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getMining().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired()),
                        Text.color("&7Total Mining XP: &6" + playerData.getMining().getTotalXP()))).asItem());
        inv.setItem(14, new ItemBuilder(Material.WHEAT, 0).setName("&7Herbalism &6" +
                playerData.getHerbalism().getLevel().toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getHerbalism().getXpTillNext() + "&7/&6" +
                                Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired()),
                        Text.color("&7Total Herbalism XP: &6" + playerData.getHerbalism().getTotalXP()))).asItem());
        inv.setItem(22, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&7Crafting &6" +
                playerData.getCrafting().getLevel().toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getCrafting().getXpTillNext() + "&7/&6" +
                                Level.valueOf("_" +
                                        (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                                .replace("_", "")) + 1)).getXpRequired()),
                        Text.color("&7Total Crafting XP: &6" + playerData.getCrafting().getTotalXP()))).asItem());
        inv.setItem(27, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
        p.openInventory(inv);
        return false;
    }
}
