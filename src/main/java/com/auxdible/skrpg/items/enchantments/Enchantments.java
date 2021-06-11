package com.auxdible.skrpg.items.enchantments;

import com.auxdible.skrpg.items.ItemCatagory;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Rarity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public enum Enchantments {
    /* weapon enchantments */
    SHARPNESS(5, Arrays.asList(ItemCatagory.WEAPON), "Sharpness", true, null, "&5⚔", 50, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    FIRE_ASPECT(2, Arrays.asList(ItemCatagory.WEAPON), "Fire Aspect", true, null, "&5♨", 100, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    POISON(3, Arrays.asList(ItemCatagory.WEAPON, ItemCatagory.BOW), "&2Poison", false, null, "&2✚", 250, Rarity.RARE, Rarity.EPIC, Rarity.LEGENDARY),
    SHIFT_ATTACK(3, Arrays.asList(ItemCatagory.WEAPON), "Crouch Attack", true, null, "&5▼", 100, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    KNOCKBACK(2, Arrays.asList(ItemCatagory.WEAPON), "Knockback", true, null, "&5≈", 25, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    WATERLORD(3, Arrays.asList(ItemCatagory.WEAPON), "&1Water&blord", false, null, "&b≈", 250, Rarity.RARE, Rarity.RARE, Rarity.RARE),
    /* bow enchantments */
    POWER(5, Arrays.asList(ItemCatagory.BOW), "Power",true, null, "&5↑", 50, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    FLAME(2, Arrays.asList(ItemCatagory.BOW), "Flame", true, null, "&5♨", 100, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),

    /* armor enchantments */
    PROTECTION(5, Arrays.asList(ItemCatagory.ARMOR), "Protection", true, null, null, 100, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    HEALTHY(5, Arrays.asList(ItemCatagory.ARMOR), "Healthy", true, null, null, 100, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON),
    /* tool enchantments */
    EFFICIENCY(5, Arrays.asList(ItemCatagory.TOOL), "Efficiency", true, null, null, 50, Rarity.COMMON, Rarity.COMMON, Rarity.UNCOMMON);
    private int normalMaxLevel;
    private List<ItemCatagory> catagory;
    private String name;
    private boolean isTableEnchantment;
    private List<ItemType> exclusiveItemType;
    private String enchantmentDamageSymbol;
    private int scrollCostPerLevel;
    private Rarity underHalfLevelRarity;
    private Rarity overHalfLevelRarity;
    private Rarity maxRarity;
    Enchantments(int normalMaxLevel, List<ItemCatagory> catagory, String name, boolean isTableEnchantment,
                 List<ItemType> exclusiveItemType, String enchantmentDamageSymbol, int scrollCostPerLevel,
                 Rarity underHalfLevelRarity, Rarity overHalfLevelRarity, Rarity maxRarity) {
        this.enchantmentDamageSymbol = enchantmentDamageSymbol;
        this.normalMaxLevel = normalMaxLevel;
        this.catagory = catagory;
        this.name = name;
        this.exclusiveItemType = exclusiveItemType;
        this.isTableEnchantment = isTableEnchantment;
        this.scrollCostPerLevel = scrollCostPerLevel;
        this.underHalfLevelRarity = underHalfLevelRarity;
        this.overHalfLevelRarity = overHalfLevelRarity;
        this.maxRarity = maxRarity;
    }

    public Rarity getMaxRarity() { return maxRarity; }

    public Rarity getOverHalfLevelRarity() { return overHalfLevelRarity; }

    public Rarity getUnderHalfLevelRarity() { return underHalfLevelRarity; }

    public int getScrollCostPerLevel() { return scrollCostPerLevel; }

    public String getEnchantmentDamageSymbol() { return enchantmentDamageSymbol; }

    public boolean isTableEnchantment() { return isTableEnchantment; }

    public int getNormalMaxLevel() { return normalMaxLevel; }

    public List<ItemCatagory> getCatagory() { return catagory; }

    public List<ItemType> getExclusiveItemType() { return exclusiveItemType; }

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
