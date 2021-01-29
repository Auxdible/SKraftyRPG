package com.auxdible.skrpg.mobs;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.entity.Entity;


import java.util.ArrayList;

public class MobManager {
    private ArrayList<Mob> mobArrayList;
    private SKRPG skrpg;
    public MobManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        mobArrayList = new ArrayList<>();
    }
    public void addMob(Mob mob) { this.mobArrayList.add(mob); }
    public ArrayList<Mob> getMobs() { return mobArrayList; }
    public void removeMob(Mob mob) { this.mobArrayList.remove(mob); }
    public Mob getMobData(Entity entity) {
        for (Mob mob : mobArrayList) {
            if (mob.getEnt().equals(entity)) {
                return mob;
            }
        }
        return null;
    }
}
