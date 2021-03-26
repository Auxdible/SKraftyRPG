package com.auxdible.skrpg.items;

public enum ItemType {
    ITEM(""),
    BOW(" BOW"),
    ARMOR(" ARMOR"),
    MATERIAL(" MATERIAL"),
    TOOL(" TOOL"),
    WEAPON(" WEAPON"),
    RUNIC_STONE(" RUNIC STONE"),
    FOOD_BASE(" FOOD BASE"),
    FOOD_INGREDIENT(" FOOD INGREDIENT"),
    FOOD(" FOOD");
    private String name;
    ItemType(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
