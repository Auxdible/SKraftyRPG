package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;

public enum Drop {
    ZOMBIE_FRAGMENT(0.05, Items.ZOMBIE_FRAGMENT, DropRarity.LUCKY),
    CRAB_FRAGMENT(0.01, Items.CRAB_FRAGMENT, DropRarity.SUPER_LUCKY),
    GEMSTONE(0.1, Items.GEMSTONE_ORE, DropRarity.NORMAL),
    PREMIUM_GOLD_EXTRACT(0.05, Items.PREMIUM_GOLD_EXTRACT, DropRarity.LUCKY),
    WARRIOR_BLADE(0.002, Items.WARRIOR_BLADE, DropRarity.CRAZY),
    ARCHER_LONGBOW(0.002, Items.ARCHER_LONGBOW, DropRarity.CRAZY),
    HEALER_BOOK(0.002, Items.HEALERS_BOOK, DropRarity.CRAZY),
    TANK_PLATE(0.002, Items.TANK_PLATE, DropRarity.CRAZY),
    TRICKSTER_WAND(0.002, Items.TRICKSTER_WAND, DropRarity.CRAZY),
    EXCALIBUR(0.00001, Items.EXCALIBUR, DropRarity.LITERALLY_INSANE),
    KINGS_STAFF(0.00001, Items.KING_STAFF, DropRarity.LITERALLY_INSANE),
    KING_CROWN(0.005, Items.KING_CROWN, DropRarity.LUCKY),
    KING_BREASTPLATE(0.005, Items.KING_ROBE_BREASTPLATE, DropRarity.LUCKY),
    KING_TROUSERS(0.005, Items.KING_ROBE_TROUSERS, DropRarity.LUCKY),
    KING_BOOTS(0.005, Items.KING_BOOTS, DropRarity.LUCKY);


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
