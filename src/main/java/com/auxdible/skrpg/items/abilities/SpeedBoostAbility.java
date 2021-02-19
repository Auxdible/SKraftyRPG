package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpeedBoostAbility implements Ability {

    @Override
    public String getName() {
        return "Speed Boost";
    }

    @Override
    public void ability(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);

        playerData.setBaseSpeed(playerData.getBaseSpeed() + 25);
        new BukkitRunnable() {
            @Override
            public void run() {

                playerData.setBaseSpeed(playerData.getBaseSpeed() - 25);
            }
        }.runTaskLater(skrpg, 200);
    }

    @Override
    public int getCost() {
        return 50;
    }

    @Override
    public boolean isAttack() {
        return false;
    }

    @Override
    public Items getItem() {
        return Items.STARTER_SWORD;
    }
}
