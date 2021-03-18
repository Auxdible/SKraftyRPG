package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Runics {
    private Level level;
    private int totalXP;
    private int xpTillNext;
    public Runics(Level level, int totalXP, int xpTillNext) {
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
            Text.applyText(player, "&5&l           SKILL UP!");
            Text.applyText(player, " ");
            Text.applyText(player, "&7You leveled up to &5Runics " + getLevel().toString().replace("_", ""));
            if (getLevel() == Level._1) {
                Text.applyText(player, "&5&lUNLOCKED! &r&7Level &51-2 &r&7Enchantments");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._3) {
                Text.applyText(player, "&5&lUNLOCKED! &r&5Destroying Items");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._5) {
                Text.applyText(player, "&5&lUNLOCKED! &r&7Level &53-4 &r&7Enchantments");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._7) {
                Text.applyText(player, "&5&lUNLOCKED! &r&f&lCOMMON &r&5Runic Stones");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._8) {
                Text.applyText(player, "&5&lUNLOCKED! &r&a&lUNCOMMON &r&5Runic Stones");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._10) {
                Text.applyText(player, "&5&lUNLOCKED! &r&5Runic Shop");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._11) {
                Text.applyText(player, "&5&lUNLOCKED! &r&7Level &55 &7Enchantments");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._12) {
                Text.applyText(player, "&5&lUNLOCKED! &r&1&lRARE &r&5Runic Stones");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._13) {
                Text.applyText(player, "&5&lUNLOCKED! &r&5&lEPIC &r&5Runic Stones");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._14) {
                Text.applyText(player, "&5&lUNLOCKED! &r&e&lLEGENDARY &r&5Runic Stones");
                Text.applyText(player, "&7Go to the &5Runic Table &7to access it!");
            } else if (getLevel() == Level._25) {
                Text.applyText(player, "&5&lUNLOCKED! &r&5Personal Runic Table");
                Text.applyText(player, "&7Go to the &emenu (/menu) &7to access it!");
            }
            Text.applyText(player, "&7+ &6" + creditsEarned + " Nuggets");
            Text.applyText(player, "&8&m>                                          ");
            playerData.setBaseStrength(playerData.getBaseStrength() + 2);
            playerData.setCredits(playerData.getCredits() + creditsEarned);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        }
    }
}
