package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.skills.Level;
import org.bukkit.entity.Player;

import java.util.List;

public interface Quest {
    void setPlayerData(PlayerData playerData);
    PlayerData getPlayerData();

    SKRPG getSKRPG();
    void setSKRPG(SKRPG skrpg);

    String parseData();
    void stringToData(String data);

    void setPhase(int phase);
    int getPhase();

    List<SKRPGItemStack> getItemRewards();

    int getCreditsReward();

    int questPhases();

    Level levelRequired();

    // INDEXES
    // 0 = COMBAT, 1 = MINING, 2 = HERBALISM, 3 = CRAFTING, 4 = FISHING
    List<Double> xpRewards();

    void executePhase(Player player, SKRPG skrpg);

    String name();

    Quests getQuestType();
}
