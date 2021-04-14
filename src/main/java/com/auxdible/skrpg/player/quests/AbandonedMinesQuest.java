package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbandonedMinesQuest implements Quest {
    @Override
    public ArrayList<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 2500;
    }

    @Override
    public int questPhases() {
        return 1;
    }

    @Override
    public Level levelRequired() {
        return null;
    }

    @Override
    public List<Double> xpRewards() {
        return Arrays.asList(0.0, 200.0, 0.0, 0.0);
    }

    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {
        if (phase == 1) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.2f);
            Text.applyText(player, "&e&lOld Miner &r&8| &7The long abandoned mines are waiting for you. I will take you down the &elift. &7Meet me at the end of the mines.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.teleport(skrpg.getLocationManager().getAbandonedMinesLocation());
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.2f);
                }
            }.runTaskLater(skrpg, 60);
        } else if (phase == 2) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.2f);
            Text.applyText(player, "&e&lOld Miner &r&8| &7You made it! The mines are now cleared out, thanks to you. Take my old pickaxe, and some experience as a gift. &7See you soon.");
            Quests.completeQuest(Quests.ABANDONED_MINES, player, skrpg.getPlayerManager().getPlayerData(player.getUniqueId()), skrpg);
            ItemStack itemStack = Items.buildItem(Items.STONE_PICKAXE);
            ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
            itemInfo.addEnchantment(new Enchantment(Enchantments.EFFICIENCY, 2));
            Items.updateItem(itemStack, itemInfo);
            player.getInventory().addItem(itemStack);
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.teleport(skrpg.getLocationManager().getAbandonedMinesSurfaceLocation());
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.2f);
                }
            }.runTaskLater(skrpg, 60);
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(player, "&e&lOld Miner &r&8| &7The mines contain many types of &eores. &7Click me to access &eThe Lift&7.");
                }
            }.runTaskLater(skrpg, 60);
        }
    }

    @Override
    public String name() {
        return "The Abandoned Mines";
    }
}
