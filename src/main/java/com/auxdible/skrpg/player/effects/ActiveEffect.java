package com.auxdible.skrpg.player.effects;

public class ActiveEffect {
    private Effects effectType;
    private int seconds;
    private int level;
    public ActiveEffect(Effects effects, int level, int seconds) {
        this.effectType = effects;
        this.seconds = seconds;
        this.level = level;
    }

    public int getLevel() { return level; }
    public Effects getEffectType() { return effectType; }
    public int getSeconds() { return seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
}
