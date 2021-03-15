package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;

public enum Drop {
    ZOMBIE_FRAGMENT(0.05, Items.ZOMBIE_FRAGMENT),
    CRAB_FRAGMENT(0.01, Items.CRAB_FRAGMENT),
    GEMSTONE(0.1, Items.GEMSTONE_ORE),
    PREMIUM_GOLD_EXTRACT(0.05, Items.PREMIUM_GOLD_EXTRACT);


    private double chance;
    private Items items;
    Drop(double chance, Items items) {
        this.chance = chance;
        this.items = items;
    }
    public double getChance() { return chance; }
    public Items getItems() { return items; }
}
