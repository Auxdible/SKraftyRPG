package com.auxdible.skrpg.mobs;


import com.auxdible.skrpg.utils.Text;
import org.bukkit.entity.Entity;

public class Mob {
    private MobType mobType;
    private int currentHP;
    private Entity ent;
    public Mob(MobType mobType, Entity ent) {
        this.mobType = mobType;
        this.currentHP = mobType.getMaxHP();
        this.ent = ent;
    }

    public Entity getEnt() { return ent; }
    public int getCurrentHP() { return currentHP; }
    public MobType getMobType() { return mobType; }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
        ent.setCustomName(Text.color("&7&l☠&e" + getMobType().getLevel() + " &r&8" + getMobType().getName() + " " + "&c" +
                getCurrentHP() + "♥"));
    }
}
