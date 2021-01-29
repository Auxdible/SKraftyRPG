package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.MobType;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MobKill {
    ZOMBIE(MobType.ZOMBIE, 10, Items.ROTTEN_FLESH, Arrays.asList(Drop.ZOMBIE_FRAGMENT));
    private MobType mobType;
    private int xpGiven;
    private Items commonDrop;
    private List<Drop> rareDrops;
    MobKill(MobType mobType, int xpGiven, Items commonDrop, List<Drop> rareDrops) {
        this.mobType = mobType;
        this.xpGiven = xpGiven;
        this.commonDrop = commonDrop;
        this.rareDrops = rareDrops;
    }
    public List<Drop> getRareDrops() { return rareDrops; }
    public int getXpGiven() { return xpGiven; }
    public Items getDrop() { return commonDrop; }
    public MobType getMobType() { return mobType; }
}
