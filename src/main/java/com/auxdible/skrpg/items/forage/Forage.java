package com.auxdible.skrpg.items.forage;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.List;


public interface Forage {
    void onSpawn(SKRPG skrpg, Location location);
    void onRemove(SKRPG skrpg, Location location);
    Items itemDrop();
    Entity clickEntity();
    ArmorStand clickStand();
    List<Double> expEarned();
}
