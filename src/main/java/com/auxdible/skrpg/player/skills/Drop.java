package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;

public enum Drop {
    ZOMBIE_FRAGMENT(0.05, Items.ZOMBIE_FRAGMENT, DropRarity.LUCKY),
    CRAB_FRAGMENT(0.01, Items.CRAB_FRAGMENT, DropRarity.SUPER_LUCKY),
    GEMSTONE(0.1, Items.GEMSTONE_ORE, DropRarity.NORMAL),
    PREMIUM_GOLD_EXTRACT(0.05, Items.PREMIUM_GOLD_EXTRACT, DropRarity.LUCKY);


    private double chance;
    private Items items;
    private DropRarity dropRarity;
    Drop(double chance, Items items, DropRarity dropRarity) {
        this.chance = chance;
        this.items = items;
        this.dropRarity = dropRarity;
    }

    public DropRarity getDropRarity() { return dropRarity; }
    public double getChance() { return chance; }
    public Items getItems() { return items; }
}
