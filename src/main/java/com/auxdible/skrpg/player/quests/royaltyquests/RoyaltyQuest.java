package com.auxdible.skrpg.player.quests.royaltyquests;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.entity.Player;

public class RoyaltyQuest {
    private int progressInteger;
    private int amountNeeded;
    private RoyaltyQuestType royaltyQuestType;
    private RoyaltyQuestDifficulty difficulty;
    public RoyaltyQuest(int progressInteger, int amountNeeded, RoyaltyQuestType royaltyQuestType, RoyaltyQuestDifficulty difficulty) {
        this.progressInteger = progressInteger;
        this.amountNeeded = amountNeeded;
        this.royaltyQuestType = royaltyQuestType;
        this.difficulty = difficulty;
    }

    public int getAmountNeeded() { return amountNeeded; }
    public void progress(int progressAmount, Player p, SKRPG skrpg) {
        this.progressInteger = progressInteger + progressAmount;
        if (progressInteger >= amountNeeded) {
            skrpg.getPlayerManager().getPlayerData(p.getUniqueId()).getPlayerActionManager().completeRoyaltyQuest(this);
        }
    }
    public int getProgressInteger() { return progressInteger; }
    public RoyaltyQuestType getRoyaltyQuestType() { return royaltyQuestType; }
    public RoyaltyQuestDifficulty getDifficulty() { return difficulty; }
}
