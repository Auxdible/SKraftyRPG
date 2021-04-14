package com.auxdible.skrpg.player.quests.royaltyquests;

import com.auxdible.skrpg.player.skills.Level;

public enum RoyaltyQuestDifficulty {
    VERY_EASY("VERY_EASY", "&b", "&b&lVery Easy", Level._0, 0),
    EASY("EASY", "&a", "&a&lEASY", Level._6, 1),
    MEDIUM("MEDIUM", "&e", "&e&lMEDIUM", Level._16, 2),
    HARD("HARD", "&c&l", "&c&lHARD", Level._31, 3),
    INSANE("INSANE", "&d&l", "&d&lINSANE", Level._41, 4);

    private String id;
    private String coloredName;
    private Level minLevel;
    private int priority;
    private String color;
    RoyaltyQuestDifficulty(String id, String color, String coloredName, Level minLevel, int priority) {
        this.id = id;
        this.coloredName = coloredName;
        this.minLevel = minLevel;
        this.priority = priority;
        this.color = color;
    }

    public String getColor() { return color; }
    public int getPriority() { return priority; }
    public Level getMinLevel() { return minLevel; }
    public String getColoredName() { return coloredName; }
    public String getId() { return id; }

}
