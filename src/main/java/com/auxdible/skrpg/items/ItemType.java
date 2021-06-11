package com.auxdible.skrpg.items;

public enum ItemType {
    ITEM("", null),
    BOW(" BOW", ItemCatagory.BOW),
    HELMET(" HELMET", ItemCatagory.ARMOR),
    CHESTPLATE(" CHESTPLATE", ItemCatagory.ARMOR),
    LEGGINGS(" LEGGINGS", ItemCatagory.ARMOR),
    BOOTS(" BOOTS", ItemCatagory.ARMOR),
    MATERIAL(" MATERIAL", null),
    HOE(" HOE", ItemCatagory.TOOL),
    PICKAXE(" PICKAXE", ItemCatagory.TOOL),
    AXE(" AXE", ItemCatagory.TOOL),
    SWORD(" SWORD", ItemCatagory.WEAPON),
    WEAPON(" WEAPON", ItemCatagory.WEAPON),
    FISHING_ROD_WEAPON(" FISHING ROD WEAPON", ItemCatagory.WEAPON),
    RUNIC_STONE(" RUNIC STONE", null),
    FOOD_BASE(" FOOD BASE", null),
    FOOD_INGREDIENT(" FOOD INGREDIENT", null),
    FOOD(" FOOD", null),
    FISHING_ROD(" FISHING ROD", ItemCatagory.FISHING_ROD),
    ENCHANTMENT_SCROLL(" ENCHANTMENT SCROLL", null),
    RING(" RING", ItemCatagory.ACCESSORY),
    ARTIFACT(" ARTIFACT", ItemCatagory.ACCESSORY),
    HEADBAND(" HEADBAND", ItemCatagory.ACCESSORY),
    NECKLACE(" NECKLACE", ItemCatagory.ACCESSORY);
    private String name;
    private ItemCatagory itemCatagory;
    ItemType(String name, ItemCatagory itemCatagory) {
        this.name = name; this.itemCatagory = itemCatagory;
    }
    public ItemCatagory getItemCatagory() { return itemCatagory; }

    public String getName() { return name; }
}
