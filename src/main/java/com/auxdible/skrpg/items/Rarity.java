package com.auxdible.skrpg.items;

public enum Rarity {
    COMMON("&f&lCOMMON", "&f", 1),
    UNCOMMON("&a&lUNCOMMON", "&a", 2),
    RARE("&1&lRARE", "&1", 3),
    EPIC("&5&lEPIC", "&5", 4),
    LEGENDARY("&6&lLEGENDARY", "&6", 5),
    MYTHICAL("&e&lMYTHICAL", "&e", 6);
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
