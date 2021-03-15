package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Herbalism {
    private Level level;
    private int totalXP;
    private int xpTillNext;
    public Herbalism(Level level, int totalXP, int xpTillNext) {
        this.level = level;
        this.totalXP = totalXP;
        this.xpTillNext = xpTillNext;
    }
    public Level getLevel() { return level; }
    public int getTotalXP() { return totalXP; }
    public int getXpTillNext() { return xpTillNext; }
    public void setLevel(int level) { this.level = Level.valueOf("_" + level); }
    public void setTotalXP(int totalXP) { this.totalXP = totalXP; }
    public void setXpTillNext(int xpTillNext) { this.xpTillNext = xpTillNext; }
    public void levelUpSkill(Player player, PlayerData playerData, SKRPG skrpg) {
        if (getLevel() != Level._50 && getXpTillNext() >= Level.valueOf("_" +
                (Integer.parseInt(getLevel().toString()
                        .replace("_", "")) + 1)).getXpRequired()) {
            int creditsEarned = getLevel().getXpRequired() / 2;
            setLevel(Integer.parseInt(getLevel().toString()
                    .replace("_", "")) + 1);
            setXpTillNext(getXpTillNext() -
                    getLevel().getXpRequired());
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&6&l           SKILL UP!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You leveled up to Herbalism &e" + playerData.getHerbalism().getLevel().toString().replace("_", ""));
            Text.applyText(player, "&7Earned &c+2 HP ♥ &r&7and &64% more double drop chance from crops!");
            Text.applyText(player, "&7+ &6" + creditsEarned + " Nuggets");
            Text.applyText(player, "&7Total Double Drop Chance: &6" + (4 *
                    Integer.parseInt(playerData.getHerbalism()
                            .getLevel().toString()
                            .replace("_", ""))) + "%");
            Text.applyText(player, "&8&m>                                         ");
            playerData.setBaseHP(playerData.getBaseHP() + 2);
            playerData.setCredits(playerData.getCredits() + creditsEarned);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        }
    }
}
