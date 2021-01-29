package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;

public enum Drop {
    ZOMBIE_FRAGMENT(0.025, Items.ZOMBIE_FRAGMENT);

    private double chance;
    private Items items;
    Drop(double chance, Items items) {
        this.chance = chance;
        this.items = items;
    }
    public double getChance() { return chance; }
    public Items getItems() { return items; }
}
