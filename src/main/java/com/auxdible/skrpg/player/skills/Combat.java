package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Combat {
    private Level level;
    private int totalXP;
    private int xpTillNext;
    public Combat(Level level, int totalXP, int xpTillNext) {
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
            if (skrpg.getGuildManager().getPlayerGuild(playerData) != null) {
                skrpg.getGuildManager().getPlayerGuild(playerData)
                        .setPowerLevel(skrpg.getGuildManager().getPlayerGuild(playerData).getPowerLevel() + 1);
            }
            setXpTillNext(getXpTillNext() -
                    getLevel().getXpRequired());
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&6&l           SKILL UP!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You leveled up to Combat &e" + getLevel().toString().replace("_", ""));
            Text.applyText(player, "&7Earned &4+2 Strength ☄ &r&7and &c2% more damage!");
            Text.applyText(player, "&7+&b" + creditsEarned + " C$ Credits");
            Text.applyText(player, "&7Total Extra Damage: &c" + (2 *
                    Integer.parseInt(getLevel().toString()
                            .replace("_", ""))) + "%");
            Text.applyText(player, "&8&m>                                          ");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 0.4f);
            playerData.setBaseStrength(playerData.getBaseStrength() + 2);
            playerData.setCredits(playerData.getCredits() + creditsEarned);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 0.4f);
        }
    }
}
