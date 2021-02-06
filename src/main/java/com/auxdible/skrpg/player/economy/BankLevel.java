package com.auxdible.skrpg.player.economy;

public enum BankLevel {
    NULL(0, 0, 0, "NONE"),
    FREE(1, 500000, 0, "&aFree"),
    BASIC(2, 1000000, 250000, "&9Basic"),
    ADVANCED(3, 2500000, 750000, "&cAdvanced"),
    DELUXE(4, 10000000, 5000000, "&5&lDeluxe"),
    SUPER_DELUXE(5, 50000000, 25000000, "&6&lSuper Deluxe");
    private int level;
    private int maxCredits;
    private int cost;
    private String nameColored;
    BankLevel(int level, int maxCoins, int cost, String nameColored) {
        this.maxCredits = maxCoins;
        this.nameColored = nameColored;
        this.cost = cost;
        this.level = level;
    }
    public int getMaxCredits() { return maxCredits; }
    public int getLevel() { return level; }
    public String getNameColored() { return nameColored; }
    public int getCost() { return cost; }

}
