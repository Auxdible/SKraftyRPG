package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.skills.Level;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface Quest {
    List<CraftingIngrediant> getItemRewards();

    int getCreditsReward();

    int questPhases();

    Level levelRequired();

    // INDEXES
    // 0 = COMBAT, 1 = MINING, 2 = HERBALISM, 3 = CRAFTING
    List<Double> xpRewards();

    void executePhase(int phase, Player player, SKRPG skrpg);

    String name();
}
