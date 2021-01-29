package com.auxdible.skrpg.player;

import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.Trade;
import com.auxdible.skrpg.player.skills.Combat;
import com.auxdible.skrpg.player.skills.Crafting;
import com.auxdible.skrpg.player.skills.Herbalism;
import com.auxdible.skrpg.player.skills.Mining;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PlayerData {
    private int speed;
    private int hp;
    private int maxHP;
    private int baseHP;
    private int energy;
    private int maxEnergy;
    private int baseEnergy;
    private int strength;
    private int baseStrength;
    private int defence;
    private int baseDefence;
    private UUID uuid;
    private int credits;
    private Region region;
    private Combat combat;
    private Mining mining;
    private Crafting crafting;
    private Herbalism herbalism;
    private ArrayList<Bank> banks;
    private Date intrestDate;
    private int baseSpeed;
    private Trade trade;
    public PlayerData(int maxHP, int maxEnergy, int strength, int defence, int speed, UUID uuid, int credits, Combat combat,
                      Mining mining, Herbalism herbalism, Crafting crafting, ArrayList<Bank> banks, Date intrestDate) {
        this.crafting = crafting;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.strength = strength;
        this.defence = defence;
        this.uuid = uuid;
        this.baseDefence = defence;
        this.baseEnergy = maxEnergy;
        this.baseStrength = strength;
        this.credits = credits;
        this.baseHP = maxHP;
        this.combat = combat;
        this.mining = mining;
        this.herbalism = herbalism;
        this.region = null;
        this.banks = banks;
        this.intrestDate = intrestDate;
        this.baseSpeed = speed;
        this.speed = speed;
        this.trade = null;
    }
    public Trade getTrade() { return trade; }
    public void setTrade(Trade trade) { this.trade = trade; }
    public int getBaseSpeed() { return baseSpeed; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) {
        if (speed > 500) {
            this.speed = 500;
        } else {
            this.speed = speed;
        }
    Bukkit.getPlayer(getUuid()).setWalkSpeed((speed / 5) * 0.01f);
    }

    public Mining getMining() { return mining; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) {
        this.region = region;
        if (region == null) {
            Bukkit.getPlayer(uuid).getScoreboard().getTeam("region").setSuffix(Text.color("&7None"));
        } else {
            Bukkit.getPlayer(uuid).getScoreboard().getTeam("region").setSuffix(Text.color(region.getName()));
        }

    }
    public Date getIntrestDate() { return intrestDate; }
    public void setIntrestDate(Date intrestDate) { this.intrestDate = intrestDate; }
    public ArrayList<Bank> getBanks() { return banks; }
    public int getDefence() { return defence; }
    public int getEnergy() { return energy; }
    public int getHp() { return hp; }
    public void setBaseSpeed(int baseSpeed) { this.baseSpeed = baseSpeed; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getMaxHP() { return maxHP; }
    public int getStrength() { return strength; }
    public UUID getUuid() { return uuid; }
    public int getBaseDefence() { return baseDefence; }
    public int getBaseEnergy() { return baseEnergy; }
    public int getBaseHP() { return baseHP; }
    public int getBaseStrength() { return baseStrength; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits;
    Bukkit.getPlayer(uuid).getScoreboard().getTeam("credits").setSuffix(credits + "");
    }
    public void setDefence(int defence) { this.defence = defence; }
    public void setEnergy(int energy) { this.energy = energy;
        if (getEnergy() > getMaxEnergy()) {
            setEnergy(getMaxEnergy());
        }
    }
    public Herbalism getHerbalism() { return herbalism; }
    public void setHp(int hp) { this.hp = hp;
        if (getHp() > getMaxHP()) {
            setHp(getMaxHP());
        } else {
            if (Math.round(getHp() * 20
                    / getMaxHP()) != 0) {
                Bukkit.getPlayer(uuid).setHealth(Math.round(getHp() * 20
                        / getMaxHP()));
            } else {
                Bukkit.getPlayer(uuid).setHealth(1);
            }
        }

    }
    public Combat getCombat() { return combat; }
    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setBaseDefence(int setBaseDefence) {
        this.baseDefence = setBaseDefence; this.defence = baseDefence; }
    public void setBaseEnergy(int setBaseEnergy) { this.baseEnergy = setBaseEnergy; this.maxEnergy = setBaseEnergy; }
    public void setBaseHP(int setBaseHP) { this.baseHP = setBaseHP; this.maxHP = setBaseHP; }
    public void setBaseStrength(int setBaseStrength) { this.baseStrength = setBaseStrength; this.strength = setBaseStrength; }
    public Crafting getCrafting() { return crafting; }
}
