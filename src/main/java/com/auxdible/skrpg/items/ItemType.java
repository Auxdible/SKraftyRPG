package com.auxdible.skrpg.items;

public enum ItemType {
    ITEM(""),
    BOW(" BOW"),
    ARMOR(" ARMOR"),
    MATERIAL(" CRAFTING MATERIAL"),
    TOOL(" TOOL"),
    WEAPON(" WEAPON"),
    RUNIC_STONE(" RUNIC STONE");
    private String name;
    ItemType(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
