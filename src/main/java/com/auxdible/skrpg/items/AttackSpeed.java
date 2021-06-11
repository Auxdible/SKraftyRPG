package com.auxdible.skrpg.items;

public enum AttackSpeed {
    RAPID(4, 1, 0, "Rapid"),
    SWIFT(10, 0, 0, "Swift"),
    NORMAL(20, 0, 3,"Normal"),
    SLOW(30, 0, 6, "Slow"),
    HEAVY(40, 0, 10, "Heavy"),
    NONE(2, 0, 0,null);
    private int ticksPerHit;
    private int hasteAmount;
    private int miningFatigueAmount;
    private String name;
    AttackSpeed(int ticksPerHit, int hasteAmount, int miningFatigueAmount, String name) {
        this.ticksPerHit = ticksPerHit;
        this.name = name;
        this.hasteAmount = hasteAmount;
        this.miningFatigueAmount = miningFatigueAmount;
    }

    public int getHasteAmount() { return hasteAmount; }

    public int getMiningFatigueAmount() { return miningFatigueAmount; }

    public String getName() { return name; }
    public int getTicksPerHit() { return ticksPerHit; }
}
