package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.npcs.TutorialNPCVillager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.TradeItem;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LordBryanQuest implements Quest {
    @Override
    public ArrayList<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 5000;
    }

    @Override
    public int questPhases() {
        return 2;
    }

    @Override
    public Level levelRequired() {
        return Level._1;
    }

    @Override
    public List<Double> xpRewards() {
        return Arrays.asList(200.0, 0.0, 0.0, 0.0);
    }

    public void giveItems(Player player, PlayerData playerData, Items itemType, SKRPG skrpg) {
        if (playerData.getActiveQuest() != Quests.LORD_BRYAN) { return; }
        int amount = 0;
        for (ItemStack itemStack : Arrays.asList(player.getInventory().getContents())) {
            ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
            if (itemInfo != null) {
                if (itemInfo.getItem() == itemType) {
                    amount = amount + itemStack.getAmount();
                }
            }
        }
        if (itemType.equals(Items.ROTTEN_FLESH) && amount >= 32 && playerData.getQuestPhase() == 1) {
            executePhase(2, player, skrpg);
        }
    }
    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            playerData.setQuestPhase(1);
            player.teleport(player.getWorld().getSpawnLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&6&lLord Bryan &r&8| &7Oh dread! Horror! I hear that the undead forces of the corrupt have overwhelmed the plains. I have heard much about you, " + player.getDisplayName() + ". Please, save our wonderful city. Bring me back 32 Rotten Flesh and I will give you great rewards.");
        } else if (phase == 2) {
            playerData.setQuestPhase(2);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&e&lNPC &r&8| &7Thank you, adventurer! Our city is safe to stand until another day. Take one of our swords from the smith. It will help you on your journey. &7If you want to do more to help our kingdom, click me again to open the &6Royalty Quests&7!");
            playerData.setRoyaltyQuestSlots(playerData.getRoyaltyQuestSlots() + 1);
            ItemInfo itemInfo = Items.DIAMOND_SWORD.generateItemInfo();
            itemInfo.addEnchantment(new Enchantment(Enchantments.SHARPNESS, 2));
            playerData.getPlayerActionManager().addExistingItem(new TradeItem(itemInfo, 1));
            Quests.completeQuest(Quests.LORD_BRYAN, player, playerData, skrpg);
        }
    }

    @Override
    public String name() {
        return "First Royalty";
    }
}

