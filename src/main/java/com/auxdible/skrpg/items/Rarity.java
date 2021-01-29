package com.auxdible.skrpg.items;

public enum Rarity {
    COMMON("&f&lCOMMON", "&f"),
    UNCOMMON("&a&lUNCOMMON", "&a"),
    RARE("&1&lRARE", "&1"),
    EPIC("&5&lEPIC", "&5"),
    LEGENDARY("&e&lLEGENDARY", "&e");
    private String nameColored;
    private String color;
    Rarity(String nameColored, String color) {
        this.nameColored = nameColored;
        this.color = color;
    }

    public String getColor() { return color; }
    public String getNameColored() { return nameColored; }
}
