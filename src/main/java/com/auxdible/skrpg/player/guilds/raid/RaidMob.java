package com.auxdible.skrpg.player.guilds.raid;


import com.auxdible.skrpg.utils.Text;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

public class RaidMob {
    private int currentHp;
    private Creature ent;
    private RaidMobs raidMobType;
    public RaidMob(Creature ent, RaidMobs raidMobType, int currentHp) {
        this.ent = ent;
        this.raidMobType = raidMobType;
        this.currentHp = currentHp;
    }
    public Creature getEnt() { return ent; }
    public int getCurrentHp() { return currentHp; }
    public RaidMobs getRaidMobType() { return raidMobType; }
    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
        ent.setCustomName(Text.color("&4&lRAID &r&c" + raidMobType.getName() + " " + getCurrentHp() + "&c♥"));
        ent.setCustomNameVisible(true);
    }
}
