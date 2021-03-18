package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.player.skills.Level;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AbandonedMinesQuest implements Quest {
    @Override
    public ArrayList<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 0;
    }

    @Override
    public int questPhases() {
        return 0;
    }

    @Override
    public Level levelRequired() {
        return null;
    }

    @Override
    public ArrayList<Integer> xpRewards() {
        return null;
    }

    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {

    }
}
