package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.player.PlayerData;
import org.bukkit.entity.Player;

public interface Ability {

    String getName();

    void ability(PlayerData playerData, SKRPG skrpg);

    int getCost();

    boolean isAttack();

    Items getItem();
}
