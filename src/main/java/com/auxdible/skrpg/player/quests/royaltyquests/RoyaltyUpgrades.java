package com.auxdible.skrpg.player.quests.royaltyquests;

public enum RoyaltyUpgrades {
    ALLOWANCE("Nuggets Allowance", 10), REDUCE_PRICE("Price Reduction", 10),
    LONG_LIVE_THE_KING("Long Live The King", 20), INCREASED_SECURITY("Increased Security", 10);

    private String royaltyUpgradeName;
    private int maxTier;
    RoyaltyUpgrades(String royaltyUpgradeName, int maxTier) {
        this.maxTier = maxTier;
        this.royaltyUpgradeName = royaltyUpgradeName;
    }

    public int getMaxTier() { return maxTier; }

    public String getRoyaltyUpgradeName() { return royaltyUpgradeName; }
}
