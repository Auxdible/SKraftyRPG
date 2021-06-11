package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;

import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class LordBryanQuest implements Quest {
    private int phase;
    private SKRPG skrpg;
    private PlayerData playerData;
    @Override
    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public PlayerData getPlayerData() {
        return playerData;
    }
    @Override
    public void setSKRPG(SKRPG skrpg) { this.skrpg = skrpg; }
    @Override
    public SKRPG getSKRPG() { return skrpg; }
    @Override
    public String parseData() {
        return phase + "";
    }

    @Override
    public void stringToData(String data) {
        phase = Integer.parseInt(data);
    }
    @Override
    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public List<SKRPGItemStack> getItemRewards() {
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
        return Arrays.asList(200.0, 0.0, 0.0, 0.0, 0.0);
    }

    public void giveItems(Player player, PlayerData playerData, Items itemType, SKRPG skrpg) {
        if (!playerData.hasQuest(getQuestType())) { return; }
        int amount = 0;
        for (ItemStack itemStack : Arrays.asList(player.getInventory().getContents())) {
            ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
            if (itemInfo != null) {
                if (itemInfo.getItem() == itemType) {
                    amount = amount + itemStack.getAmount();
                }
            }
        }
        if (itemType.equals(Items.ROTTEN_FLESH) && amount >= 32 && phase == 1) {
            setPhase(2);
            executePhase(player, skrpg);
        }
    }
    @Override
    public void executePhase(Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            setPhase(1);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&6&lLord Bryan &r&8| &7Oh dread! Horror! I hear that the undead forces of the corrupt have overwhelmed the plains. I have heard much about you, " + player.getDisplayName() + ". Please, save our wonderful city. Bring me back 32 Rotten Flesh and I will give you great rewards.");
        } else if (phase == 2) {
            setPhase(2);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&6&lLord Bryan &r&8| &7Thank you, adventurer! Our city is safe to stand until another day. Take one of our swords from the smith. It will help you on your journey. &7If you want to do more to help our kingdom, click me again to open the &6Royalty Quests&7!");
            playerData.setRoyaltyQuestSlots(playerData.getRoyaltyQuestSlots() + 1);
            ItemInfo itemInfo = Items.DIAMOND_SWORD.generateItemInfo();
            itemInfo.addEnchantment(new Enchantment(Enchantments.SHARPNESS, 2));
            playerData.getPlayerActionManager().addExistingItem(new SKRPGItemStack(itemInfo, 1));
            Quests.completeQuest(Quests.LORD_BRYAN, player, playerData, skrpg);
        }
    }

    @Override
    public String name() {
        return "First Royalty";
    }

    @Override
    public Quests getQuestType() {
        return Quests.LORD_BRYAN;
    }
}

