package com.auxdible.skrpg.items.food.foodactions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import org.bukkit.entity.Player;

public interface FoodAction {
    void onEat(Player p, SKRPG skrpg, PlayerData playerData);
}
