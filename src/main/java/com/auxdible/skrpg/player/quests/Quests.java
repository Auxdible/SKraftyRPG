package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum Quests {
    TUTORIAL(new TutorialQuest()),
    ABANDONED_MINES(new AbandonedMinesQuest()),
    FARMING_QUEST(new FarmingQuest()),
    LORD_BRYAN(new LordBryanQuest()),
    FISHERMAN_QUEST(new FishermanQuest()),
    VALISSA_ARACHNE_QUEST(new ValissaArachneQuest());
    private Quest quest;
    Quests(Quest quest) {
        this.quest = quest;
    }
    public Quest getQuest() { return quest; }
    public static void startQuest(Quests quest, Player player, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(quest)) {
            Text.applyText(player,  "&cYou are already in this quest!");
            return;
        }
        if (playerData.getActiveQuest().size() >= 14) {
            Text.applyText(player, "&cYou cannot have more than 14 quests active at once!");
            return;
        }
        Quest questStart = quest.getQuest();
        questStart.setPhase(1);
        playerData.getActiveQuest().add(questStart);
        quest.getQuest().executePhase(player, skrpg);
    }
    public static void completeQuest(Quests quest, Player player, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(quest)) {
            playerData.getActiveQuest().remove(playerData.getQuest(quest));
            playerData.getCompletedQuests().add(quest);
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&a&l           QUEST COMPLETE!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You completed the quest: &e" + quest.getQuest().name());
            Text.applyText(player, "&7Rewards: ");
            if (quest.getQuest().getItemRewards() != null) {
                for (SKRPGItemStack SKRPGItemStack : quest.getQuest().getItemRewards()) {
                    Text.applyText(player, SKRPGItemStack.getItemInfo().getItem().getRarity().getNameColored() + SKRPGItemStack.getItemInfo().getItem().getName() + " &8| " + SKRPGItemStack.getItemInfo().getItem().getCraftingAmount());
                    ItemStack builtItemStack = Items.buildItem(SKRPGItemStack.getItemInfo().getItem());
                    Items.updateItem(builtItemStack, SKRPGItemStack.getItemInfo());
                    if (SKRPGItemStack.getAmount() != 0) {
                        builtItemStack.setAmount(SKRPGItemStack.getAmount());
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
                } else if (quest.getQuest().xpRewards().get(4) != 0) {
                    Text.applyText(player, "&f+ &e" + quest.getQuest().xpRewards().get(4) + " &eFishing XP");
                    playerData.getFishing().setXpTillNext(playerData.getFishing().getXpTillNext() + quest.getQuest().xpRewards().get(4));
                    playerData.getFishing().setTotalXP(playerData.getFishing().getTotalXP() + quest.getQuest().xpRewards().get(4));
                    playerData.getFishing().levelUpSkill(player, playerData, skrpg);
                }
            }

            Text.applyText(player, "&8&m>                                          ");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.4f);
        }
    }
}
