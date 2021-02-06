package com.auxdible.skrpg.player.guilds;

public enum GuildRank {
    MEMBER(0, "&a", "&aMember"),
    ELDER(1, "&e", "&6Elder"),
    MODERATOR(2, "&9", "&9Moderator"),
    ADMIN(3, "&c", "&cAdmin"),
    OWNER(4, "&6", "&4Owner");

    private int priority;
    private String color;
    private String nameColored;
    GuildRank(int priority, String color, String nameColored) {
        this.color = color;
        this.nameColored = nameColored;
        this.priority = priority;
    }
    public int getPriority() { return priority; }
    public String getColor() { return color; }
    public String getNameColored() { return nameColored; }
}
