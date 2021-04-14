package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;

public enum Quests {
    TUTORIAL(new TutorialQuest()),
    ABANDONED_MINES(new AbandonedMinesQuest()),
    FARMING_QUEST(new FarmingQuest()),
    LORD_BRYAN(new LordBryanQuest());
    private Quest quest;
    Quests(Quest quest) {
        this.quest = quest;
    }
    public Quest getQuest() { return quest; }
    public static void startQuest(Quests quest, Player player, PlayerData playerData, SKRPG skrpg) {
        if (playerData.getActiveQuest() != null) {
            Text.applyText(player,  "&cYou are already in a quest! Finish your current quest!");
        }
        playerData.setActiveQuest(quest);
        quest.getQuest().executePhase(1, player, skrpg);
    }
    public static void completeQuest(Quests quest, Player player, PlayerData playerData, SKRPG skrpg) {
        playerData.setQuestPhase(0);
        playerData.setActiveQuest(null);
        playerData.getCompletedQuests().add(quest);
        Text.applyText(player, "&8&m>                                          ");
        Text.applyText(player, "&a&l           QUEST COMPLETE!");
        Text.applyText(player, " ");
        Text.applyText(player, "&7You completed the quest: &e" + quest.getQuest().name());
        Text.applyText(player, "&7Rewards: ");
        if (quest.getQuest().getItemRewards() != null) {
            for (CraftingIngrediant craftingIngrediant : quest.getQuest().getItemRewards()) {
                Text.applyText(player, craftingIngrediant.getItems().getRarity().getNameColored() + craftingIngrediant.getItems().getName() + " &8| " + craftingIngrediant.getItems().getCraftingAmount());
                ItemStack builtItemStack = Items.buildItem(craftingIngrediant.getItems());
                if (craftingIngrediant.getAmount() != 0) {
                    builtItemStack.setAmount(craftingIngrediant.getAmount());
                }
                player.getInventory().addItem(builtItemStack);
            }
        }
        if (quest.getQuest().getCreditsReward() != 0) {
            Text.applyText(player, "&f+ &6" + quest.getQuest().getCreditsReward() + " Nuggets");
            playerData.setCredits(playerData.getCredits() + quest.getQuest().getCreditsReward());
        }
        if (quest.getQuest().xpRewards() != null) {
            if (quest.getQuest().xpRewards().get(0) != 0) {
                Text.applyText(player, "&f+ &e" + quest.getQuest().xpRewards().get(0) + " &eCombat XP");
                playerData.getCombat().setXpTillNext(playerData.getCombat().getXpTillNext() + quest.getQuest().xpRewards().get(0));
                playerData.getCombat().setTotalXP(playerData.getCombat().getTotalXP() + quest.getQuest().xpRewards().get(0));
                playerData.getCombat().levelUpSkill(player, playerData, skrpg);
            } else if (quest.getQuest().xpRewards().get(1) != 0) {
                Text.applyText(player, "&f+ &e" + quest.getQuest().xpRewards().get(1) + " &eMining XP");
                playerData.getMining().setXpTillNext(playerData.getMining().getXpTillNext() + quest.getQuest().xpRewards().get(1));
                playerData.getMining().setTotalXP(playerData.getMining().getTotalXP() + quest.getQuest().xpRewards().get(1));
                playerData.getMining().levelUpSkill(player, playerData, skrpg);
            } else if (quest.getQuest().xpRewards().get(2) != 0) {
                Text.applyText(player, "&f+ &e" + quest.getQuest().xpRewards().get(2) + " &eHerbalism XP");
                playerData.getHerbalism().setXpTillNext(playerData.getHerbalism().getXpTillNext() + quest.getQuest().xpRewards().get(2));
                playerData.getHerbalism().setTotalXP(playerData.getHerbalism().getTotalXP() + quest.getQuest().xpRewards().get(2));
                playerData.getHerbalism().levelUpSkill(player, playerData, skrpg);
            } else if (quest.getQuest().xpRewards().get(3) != 0) {
                Text.applyText(player, "&f+ &e" + quest.getQuest().xpRewards().get(3) + " &eCrafting XP");
                playerData.getCrafting().setXpTillNext(playerData.getCrafting().getXpTillNext() + quest.getQuest().xpRewards().get(3));
                playerData.getCrafting().setTotalXP(playerData.getCrafting().getTotalXP() + quest.getQuest().xpRewards().get(3));
                playerData.getCrafting().levelUpSkill(player, playerData, skrpg);
            }
        }

        Text.applyText(player, "&8&m>                                          ");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.4f);
    }
}
