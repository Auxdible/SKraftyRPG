package com.auxdible.skrpg.items;

public enum Rarity {
    COMMON("&f&lCOMMON", "&f", 0),
    UNCOMMON("&a&lUNCOMMON", "&a", 1),
    RARE("&1&lRARE", "&1", 2),
    EPIC("&5&lEPIC", "&5", 3),
    LEGENDARY("&e&lLEGENDARY", "&e", 4);
    private String nameColored;
    private String color;
    private int priority;
    Rarity(String nameColored, String color, int priority) {
        this.nameColored = nameColored;
        this.color = color;
        this.priority = priority;
    }

    public String getColor() { return color; }
    public String getNameColored() { return nameColored; }
    public int getPriority() { return priority; }
}
