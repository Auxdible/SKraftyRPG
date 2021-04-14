package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FarmingQuest implements Quest {
    @Override
    public List<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 1000;
    }

    @Override
    public int questPhases() {
        return 3;
    }

    @Override
    public Level levelRequired() {
        return null;
    }

    @Override
    public List<Double> xpRewards() {
        return Arrays.asList(0.0, 0.0, 200.0, 0.0);
    }
    public void giveItems(Player player, PlayerData playerData, Items itemType, ItemInfo itemInfo, SKRPG skrpg) {
        if (playerData.getActiveQuest() != Quests.FARMING_QUEST) { return; }
        int amount = 0;
        for (ItemStack itemStack : Arrays.asList(player.getInventory().getContents())) {
            ItemInfo itemInfo2 = ItemInfo.parseItemInfo(itemStack);
            if (itemInfo2 != null) {
                if (itemInfo2.getItem() == itemType) {
                    amount = amount + itemStack.getAmount();
                }
            }
        }
        skrpg.getLogger().info("3");
        if (itemType.equals(Items.SWEET_BERRIES) && playerData.getQuestPhase() == 3) {
            if (itemInfo.isProcessed()) {
                executePhase(3, player, skrpg);
            }
        } else if (itemType.equals(Items.SWEET_BERRIES) && amount >= 10 && playerData.getQuestPhase() == 2) {
            executePhase(2, player, skrpg);
        }
    }
    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            playerData.setQuestPhase(2);

            new BukkitRunnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lFarmer Joe &r&8| &7Alrighty, ma' little pumpkin. You want to start by farmin' some crops, &7thankfully there are some in ma' yard. &7Go gather 10 &fSweet Berries &7for me, would ya?");
                }
            }.runTaskLater(skrpg, 40);
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lFarmer Joe &r&8| &7Don't forget! Some crops are &astronger and better &7than others! These are marked with stars and will sell for more!");
                    Text.applyText(player, "&aFarmer Joe's Quality Chart:");
                    Text.applyText(player, "&8&m                             ");
                    Text.applyText(player, "&8[&c☼&8] - &7Top Quality. Price Multiplier: &a5x");
                    Text.applyText(player, "&8[&5✯&8] - &7Deluxe. Price Multiplier: &a4x");
                    Text.applyText(player, "&8[&6★★★&8] - &73 Stars. Price Multiplier: &a3x");
                    Text.applyText(player, "&8[&6★★&7☆&8] - &72 Stars. Price Multiplier: &a2x");
                    Text.applyText(player, "&8[&6★&7☆☆&8] - &71 Star. Price Multiplier: &a1x");
                    Text.applyText(player, "&8&m                             ");
                }
            }.runTaskLater(skrpg, 100);
        } else if (phase == 2) {
            playerData.setQuestPhase(3);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Text.applyText(player, "&e&lFarmer Joe &r&8| &7That's it! Now, we need to process the sweet berries. &7Click the Smoker behind me, and process the Sweet Berries. &7This will cost iron, so make sure you have some. &7Once you're done, bring the processed sweet berries to me.");

        } else if (phase == 3) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Quests.completeQuest(Quests.FARMING_QUEST, player, playerData, skrpg);
            player.getInventory().addItem(Items.buildItem(Items.LOW_QUALITY_FRUIT));
            Text.applyText(player, "&e&lFarmer Joe &r&8| &7There you go! Now, you can apply this Low Quality Fruit, which you obtain from leaves on trees, to a compressed sweet berry to create a &aBerryfruit&7! &7You will need &aHerbalism Level 2 &7to cook it.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lFarmer Joe &r&8| &7Go to the Smoker, and go to &aCook Food, &7then select the food base and item you want to apply to it, and click apply to cook a &aBerryfruit&7! &7You will get &cRegeneration 1 &7for &a30 seconds &7when you eat it!");

                }
            }.runTaskLater(skrpg, 40);
        }
    }

    @Override
    public String name() {
        return "Living Off The Land";
    }
}
