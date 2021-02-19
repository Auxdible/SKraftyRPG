package com.auxdible.skrpg.commands.inventory;

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
        Level levelCombat;
        String xpTillCombat;
        if (playerData.getCombat().getLevel() != Level._50) {
            levelCombat = Level.valueOf("_" +
                    (Integer.parseInt(playerData.getCombat().getLevel().toString()
                            .replace("_", "")) + 1));
            xpTillCombat = "" + levelCombat.getXpRequired();
        } else {
            levelCombat = Level._50;
            xpTillCombat = "&6&lCOMBAT SKILL COMPLETE!";
        }
        Level levelMining;
        String xpTillMining;
        if (playerData.getMining().getLevel() != Level._50) {
            levelMining = Level.valueOf("_" +
                    (Integer.parseInt(playerData.getMining().getLevel().toString()
                            .replace("_", "")) + 1));
            xpTillMining = "" + levelMining.getXpRequired();
        } else {
            levelMining = Level._50;
            xpTillMining = "&6&lMINING SKILL COMPLETE!";
        }
        Level levelHerbalism;
        String xpTillHerbalism;
        if (playerData.getHerbalism().getLevel() != Level._50) {
            levelHerbalism = Level.valueOf("_" +
                    (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                            .replace("_", "")) + 1));
            xpTillHerbalism = "" + levelHerbalism.getXpRequired();
        } else {
            levelHerbalism = Level._50;
            xpTillHerbalism = "&6&lHERBALISM SKILL COMPLETE!";
        }
        Level levelCrafting;
        String xpTillCrafting;
        if (playerData.getCrafting().getLevel() != Level._50) {
            levelCrafting = Level.valueOf("_" +
                    (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                            .replace("_", "")) + 1));
            xpTillCrafting = "" + levelCrafting.getXpRequired();
        } else {
            levelCrafting = Level._50;
            xpTillCrafting = "&6&lCRAFTING SKILL COMPLETE!";
        }
        inv.setItem(13, new ItemBuilder(Material.DIAMOND_SWORD, 0).setName("&7Combat &6" +
                levelCombat.toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getCombat().getXpTillNext() + "&7/&6" +
                        xpTillCombat),
                        Text.color("&7Total Combat XP: &6" + playerData.getCombat().getTotalXP()))).asItem());
        inv.setItem(12, new ItemBuilder(Material.DIAMOND_PICKAXE, 0).setName("&7Mining &6" +
                levelMining.toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getMining().getXpTillNext() + "&7/&6" +
                        xpTillMining),
                        Text.color("&7Total Mining XP: &6" + playerData.getMining().getTotalXP()))).asItem());
        inv.setItem(14, new ItemBuilder(Material.WHEAT, 0).setName("&7Herbalism &6" +
                levelHerbalism.toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getHerbalism().getXpTillNext() + "&7/&6" +
                                xpTillHerbalism),
                        Text.color("&7Total Herbalism XP: &6" + playerData.getHerbalism().getTotalXP()))).asItem());
        inv.setItem(22, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&7Crafting &6" +
                levelCrafting.toString().replace("_", "")).setLore(
                Arrays.asList(Text.color("&6" + playerData.getCrafting().getXpTillNext() + "&7/&6" +
                                xpTillCrafting),
                        Text.color("&7Total Crafting XP: &6" + playerData.getCrafting().getTotalXP()))).asItem());
        inv.setItem(27, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
        p.openInventory(inv);
        return false;
    }
}
