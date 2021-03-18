package com.auxdible.skrpg.items.enchantments;

import com.auxdible.skrpg.items.ItemType;

import java.util.EnumSet;

public enum Enchantments {
    SHARPNESS(5, ItemType.WEAPON, "Sharpness"),
    POWER(5, ItemType.BOW, "Power"),
    PROTECTION(5, ItemType.ARMOR, "Protection"),
    HEALTHY(5, ItemType.ARMOR, "Healthy"),
    EFFICIENCY(5, ItemType.TOOL, "Efficiency");
    private int normalMaxLevel;
    private ItemType applyType;
    private String name;
    Enchantments(int normalMaxLevel, ItemType applyType, String name) {
        this.normalMaxLevel = normalMaxLevel;
        this.applyType = applyType;
        this.name = name;
    }

    public int getNormalMaxLevel() { return normalMaxLevel; }
    public ItemType getApplyType() { return applyType; }
    public String getName() { return name; }
    public static Enchantments getEnchantment(String name) {
        for (Enchantments enchantments : EnumSet.allOf(Enchantments.class)) {
            if (enchantments.getName().equalsIgnoreCase(name)) {
                return enchantments;
            }
        }
        return null;
    }
}
