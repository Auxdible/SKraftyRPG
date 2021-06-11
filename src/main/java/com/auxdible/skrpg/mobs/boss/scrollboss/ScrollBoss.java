package com.auxdible.skrpg.mobs.boss.scrollboss;

import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.mobs.MobType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface ScrollBoss {
    HashMap<Player, Integer> damageTotal();
    void spawnBoss();
    MobType bossMobType();
    Mob getMob();
    void damage(int amount, Player player);
    String name();
    Player getScrollSpawner();
    Location getSpawnLocation();
    Items getCommonItem();
    List<Items> getRareItem();
}
