package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Global {
    private int globalLevel;
    public Global(PlayerData playerData, SKRPG skrpg) {
        calculateGlobalLevel(playerData, skrpg);
    }

    public int getGlobalLevel() { return globalLevel; }

    public void calculateGlobalLevel(PlayerData playerData, SKRPG skrpg) {
        globalLevel = 0;
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getCombat().getLevel().toString());
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getRunics().getLevel().toString());
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getMining().getLevel().toString());
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getFishing().getLevel().toString());
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString());
        globalLevel = globalLevel + SKRPG.levelToInt(playerData.getCrafting().getLevel().toString());
        if (Bukkit.getPlayer(playerData.getUuid()) != null) {
            Player p = Bukkit.getPlayer(playerData.getUuid());
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getScoreboard().getTeam("player").setPrefix(Text.color("&8&l[&r&a" + playerData.getGlobal().getGlobalLevel() + "☼&8&l] &r&7"));
                    p.getScoreboard().getTeam("globalLevel").setSuffix(getGlobalLevel() + " ");
                }
            }.runTaskLater(skrpg, 20);
        }
    }
}
