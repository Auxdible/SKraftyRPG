package com.auxdible.skrpg.items.food.foodactions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.effects.ActiveEffect;
import com.auxdible.skrpg.player.effects.Effects;
import org.bukkit.entity.Player;

public class BerryfruitAction implements FoodAction {
    @Override
    public void onEat(Player p, SKRPG skrpg, PlayerData playerData) {
        playerData.addEffect(new ActiveEffect(Effects.REGENERATION, 1, 30));
    }
}
