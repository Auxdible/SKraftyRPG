package com.auxdible.skrpg.player.skills;

public enum DropRarity {

    NORMAL(null, 0),
    LUCKY("&a&lLUCKY", 1),
    SUPER_LUCKY("&b&lSUPER LUCKY", 2),
    CRAZY("&e&lCRAZY", 3),
    SUPER_CRAZY("&d&lSUPER CRAZY", 4),
    LITERALLY_INSANE("&6&k&l;i;i;i; &r&6&lLITERALLY INSANE &r&6&k&l;i;i;i;", 5);
    private String name;
    private int priority;
    DropRarity(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public int getPriority() { return priority; }

    public String getName() { return name; }

}
