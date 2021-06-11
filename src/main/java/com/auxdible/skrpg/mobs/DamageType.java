package com.auxdible.skrpg.mobs;

public enum DamageType {
    REGULAR("&c{damage} &4&l☄"),
    TRUE("&a{damage} &2&l❂"),
    FIRE("&c{damage} &6&l♨"),
    DROWNING("&9{damage} &b&l❈"),
    NATURAL("&7{damage} &8&l☤"),
    ENERGETIC("&e{damage} &e&l☢");

    private String prefix;
    DamageType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
