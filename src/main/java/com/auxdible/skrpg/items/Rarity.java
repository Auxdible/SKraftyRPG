package com.auxdible.skrpg.items;

public enum Rarity {
    COMMON("&f&lCOMMON", "&f⬥ ", "&f", 1),
    UNCOMMON("&a&lUNCOMMON", "&a❇ ", "&a",  2),
    RARE("&1&lRARE", "&1≈ ", "&1",  3),
    EPIC("&5&lEPIC", "&5፨ ", "&5",  4),
    LEGENDARY("&6&lLEGENDARY", "&6፠ ", "&6", 5),
    ANCIENT("&e&lANCIENT", "&e۞ ", "&e",  6);
    private String nameColored;
    private String color;
    private String rawColor;
    private int priority;
    Rarity(String nameColored, String color, String rawColor, int priority) {
        this.nameColored = nameColored;
        this.color = color;
        this.priority = priority;
        this.rawColor = rawColor;
    }

    public String getRawColor() { return rawColor; }
    public String getColor() { return color; }
    public String getNameColored() { return nameColored; }
    public int getPriority() { return priority; }
}
