package com.auxdible.skrpg.items;

public enum ItemType {
    ITEM(""),
    SWORD(" SWORD"),
    BOW(" BOW"),
    ARMOR(" ARMOR"),
    MATERIAL(" CRAFTING MATERIAL"),
    PICKAXE(" PICKAXE"),
    AXE(" AXE"),
    HOE(" HOE");
    private String name;
    ItemType(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
