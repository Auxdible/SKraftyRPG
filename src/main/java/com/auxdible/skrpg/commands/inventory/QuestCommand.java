package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.quests.Quest;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class QuestCommand implements CommandExecutor {
    private SKRPG skrpg;
    public QuestCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this as a player!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null, 36, "Your Quests");
            for (int i = 0; i <= 35; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            List<Integer> questSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);

            for (Quest quests : playerData.getActiveQuest()) {
                List<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add(Text.color("&7Current Phase: &a" + quests.getPhase()));
                if (quests.getCreditsReward() != 0) {
                    lore.add(Text.color("&7Nuggets Reward: &6" + quests.getCreditsReward()));
                }
                if (quests.getItemRewards() != null) {
                    lore.add(Text.color("&7Item Rewards: &6" + quests.getCreditsReward()));
                    for (SKRPGItemStack SKRPGItemStack : quests.getItemRewards()) {
                        lore.add(Text.color(SKRPGItemStack.getItemInfo().getItem().getRarity().getColor() + SKRPGItemStack.getItemInfo().getItem().getName()));
                    }
                }
                boolean hasXp = false;
                if (quests.xpRewards() != null) {
                    for (double xp : quests.xpRewards()) {
                        if (xp != 0.0) {
                            if (!hasXp) {
                                hasXp = true;
                                lore.add(Text.color("&7XP Rewards:"));
                            }
                            String xpType = "Combat XP";
                            if (quests.xpRewards().indexOf(xp) == 1) {
                                xpType = "Mining XP";
                            } else if (quests.xpRewards().indexOf(xp) == 2) {
                                xpType = "Herbalism XP";
                            } else if (quests.xpRewards().indexOf(xp) == 3) {
                                xpType = "Crafting XP";
                            } else if (quests.xpRewards().indexOf(xp) == 4) {
                                xpType = "Fishing XP";
                            }
                            lore.add(Text.color("&e" + xp + " " + xpType));
                        }
                    }
                }
                lore.add(" ");
                inv.setItem(questSlots.get(playerData.getActiveQuest().indexOf(quests)), new ItemBuilder(Material.ENCHANTED_BOOK, 0).setName("&a&l" + quests.name())
                        .setLore(lore).asItem());
            }
            inv.setItem(27, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
            p.openInventory(inv);
        }
        return false;
    }
}
