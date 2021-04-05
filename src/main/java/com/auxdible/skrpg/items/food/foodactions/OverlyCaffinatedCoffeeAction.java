package com.auxdible.skrpg.items.food.foodactions;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.effects.ActiveEffect;
import com.auxdible.skrpg.player.effects.Effects;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.entity.Player;

public class OverlyCaffinatedCoffeeAction implements FoodAction {
    @Override
    public void onEat(Player p, SKRPG skrpg, PlayerData playerData) {

        playerData.addEffect(new ActiveEffect(Effects.SPEED, 2, 30));
    }
}
