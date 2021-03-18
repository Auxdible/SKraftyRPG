package com.auxdible.skrpg.items.enchantments;

public class Enchantment {
    private Enchantments enchantments;
    private int level;
    public Enchantment(Enchantments enchantmentType, int level) {
        this.enchantments = enchantmentType;
        this.level = level;
    }
    public Enchantments getEnchantmentType() { return enchantments; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}
