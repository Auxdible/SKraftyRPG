package com.auxdible.skrpg.items;

import java.util.EnumSet;

public enum RunicStones {
    ENCRESTED("Encrested", 0, 0, 0, 8, 0, 8,
            Items.ENCRESTED_OBSIDIAN, 125),
    SPEEDY("Speedy", 0, 0, 10, 0, 5, 0, Items.SWEET_CANE, 125),
    FEROCIOUS("Ferocious", 5, 10, 2, 0, 0, 0, Items.CRAB_SCALE, 125),
    CIRCUITED("Circuited", 0, 0, 5, 0, 15, 0, Items.REDSTONE_CIRCUIT, 125);

    private int bonusDamage;
    private int bonusStrength;
    private int bonusSpeed;
    private int bonusHealth;
    private int bonusEnergy;
    private int bonusDefence;
    private Items item;
    private String name;
    private int cost;
    RunicStones(String name, int bonusDamage, int bonusStrength, int bonusSpeed, int bonusHealth, int bonusEnergy, int bonusDefence,
                Items items, int cost) {
        this.bonusDamage = bonusDamage;
        this.bonusStrength = bonusStrength;
        this.bonusSpeed = bonusSpeed;
        this.bonusHealth = bonusHealth;
        this.bonusEnergy = bonusEnergy;
        this.bonusDefence = bonusDefence;
        this.item = items;
        this.name = name;
        this.cost = cost;
    }
    public int getCost() { return cost; }
    public String getName() { return name; }
    public int getBonusDamage() { return bonusDamage; }
    public int getBonusDefence() { return bonusDefence; }
    public int getBonusStrength() { return bonusStrength; }
    public int getBonusHealth() { return bonusHealth; }
    public int getBonusSpeed() { return bonusSpeed; }
    public int getBonusEnergy() { return bonusEnergy; }
    public Items getItem() { return item; }
    public static RunicStones getRunicStone(Items item) {
        for (RunicStones runicStones : EnumSet.allOf(RunicStones.class)) {
            if (runicStones.getItem() == item) {
                return runicStones;
            }
        }
        return null;
    }
}
