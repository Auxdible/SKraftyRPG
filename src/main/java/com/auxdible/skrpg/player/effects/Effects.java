package com.auxdible.skrpg.player.effects;

public enum Effects {
    REGENERATION("&cRegeneration"),
    STRENGTH("&4Strength"),
    SPEED("&fSpeed"),
    PLATED("&aPlated"),
    GREEDY("&6Greedy"),
    EXPERIENCED("&bExperienced"),
    ENERGETIC("&eEnergetic");
    private String name;
    Effects(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}
