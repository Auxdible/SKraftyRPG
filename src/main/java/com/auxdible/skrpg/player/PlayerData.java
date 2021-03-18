package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.Trade;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

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
    private int runicPoints;
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
    private Rarity sellAboveRarity;
    private boolean toggleTrade;
    private Trade trade;
    private ArrayList<Collection> collections;
    private ArrayList<Quests> completedQuests;
    private Quests activeQuest;
    private int questPhase;
    private List<NPC> renderedNPCs;
    private Runics runics;
    private HashMap<RunicUpgrades, Integer> runicUpgrades;
    private int fifthRunicPoint;
    public PlayerData(int maxHP, int maxEnergy, int strength, int defence, int speed, UUID uuid, int credits, Combat combat,
                      Mining mining, Herbalism herbalism, Crafting crafting, Runics runics, ArrayList<Bank> banks,
                      Date intrestDate, ArrayList<Collection> collections, Rarity sellAboveRarity, boolean toggleTrade, ArrayList<Quests> quests,
                      int runicPoints, HashMap<RunicUpgrades, Integer> runicUpgrades, int questPhase, Quests currentQuest) {
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
        this.collections = collections;
        this.toggleTrade = toggleTrade;
        this.sellAboveRarity = sellAboveRarity;
        this.completedQuests = quests;
        this.activeQuest = currentQuest;
        this.questPhase = questPhase;
        this.renderedNPCs = new ArrayList<>();
        this.runicPoints = runicPoints;
        this.runics = runics;
        this.runicUpgrades = runicUpgrades;
        this.fifthRunicPoint = 0;
    }

    public HashMap<RunicUpgrades, Integer> getRunicUpgrades() { return runicUpgrades; }
    public Runics getRunics() { return runics; }
    public int getRunicPoints() { return runicPoints; }
    public void removeRunicPoints(int runicPointsRemoved) {
        this.runicPoints = runicPoints - runicPointsRemoved;
    }
    public void addRunicPoints(int runicPointsGained, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(uuid);
        if (Math.random() <= 0.01) {
            runicPointsGained = runicPointsGained + 100;

            Text.applyText(p, "&5&lLUCKY RP! &r&8| &7You got &5" + runicPointsGained +" RP ஐ&7!");
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.2f);
        }
        if (getRunicUpgrades().containsKey(RunicUpgrades.MORE_RP)) {
            fifthRunicPoint++;
        }
        if (fifthRunicPoint == 5) {
            fifthRunicPoint = 0;
            runicPointsGained = runicPointsGained + (1 * getRunicUpgrades().get(RunicUpgrades.MORE_RP));
        }
        getRunics().setXpTillNext(getRunics().getXpTillNext() +
                runicPointsGained);
        getRunics().setTotalXP(getRunics().getTotalXP()
                + runicPointsGained);
        getRunics().levelUpSkill(p, this, skrpg);
        this.runicPoints = runicPoints + runicPointsGained; }
    public List<NPC> getRenderedNPCs() { return renderedNPCs; }
    public void setQuestPhase(int questPhase) { this.questPhase = questPhase; }
    public int getQuestPhase() { return questPhase; }
    public void setActiveQuest(Quests activeQuest) { this.activeQuest = activeQuest; }
    public Quests getActiveQuest() { return activeQuest; }
    public ArrayList<Quests> getCompletedQuests() { return completedQuests; }
    public ArrayList<Collection> getCollections() { return collections; }
    public Rarity getSellAboveRarity() { return sellAboveRarity; }
    public boolean canTrade() { return toggleTrade; }
    public void setToggleTrade(boolean toggleTrade) { this.toggleTrade = toggleTrade; }
    public void setSellAboveRarity(Rarity sellAboveRarity) { this.sellAboveRarity = sellAboveRarity; }
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
    if (Bukkit.getPlayer(uuid) == null) { return; }
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
    public void damage(int damage, SKRPG skrpg) {
        Player player = Bukkit.getPlayer(uuid);
        if (skrpg.getRaidManager().isInRaid(player)) {
            Text.applyText(player, "&cYou deflected a hit because you are in a raid!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            return;
        }
        double damageReduction = (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence() /
                (100.0 + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence()));
        int nerfedDamage = (int) Math.round(damage - (damage *
                damageReduction));
        if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage < 0) {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(0);
        } else {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                    skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage);
        }
        if (getHp() <= 0) {
            kill(player, skrpg);
        }
        ArmorStand damageIndictator = (ArmorStand) player.getWorld()
                .spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        damageIndictator.setInvulnerable(true);
        damageIndictator.setCollidable(false);
        damageIndictator.setInvisible(true);
        damageIndictator.setCustomName(Text.color("&c" + nerfedDamage + " &4&l☄"));
        damageIndictator.setCustomNameVisible(true);
        damageIndictator.setSmall(true);
        damageIndictator.setVelocity(new Vector(0, 0.5, 0));
        new BukkitRunnable() {
            int quarterSecondsAlive = 0;
            @Override
            public void run() {
                quarterSecondsAlive++;
                if (quarterSecondsAlive == 8) {
                    damageIndictator.remove();
                    cancel();
                }
            }
        }.runTaskTimer(skrpg, 0, 5);
    }
    public void damagePlayer(Player damager, int damage, SKRPG skrpg) {
        Player player = Bukkit.getPlayer(getUuid());
        if (player == null) { return; }
        if (skrpg.getRaidManager().isInRaid(player)) {
            Text.applyText(player, "&cYou deflected a hit from a player! (" + damager.getDisplayName() + ") because you are in a raid!");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            return;
        }
        double damageReduction = (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence() /
                (2000.0 + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getDefence()));
        int nerfedDamage = (int) Math.round(damage - (damage *
                damageReduction));

        if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage < 0) {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(0);
        } else {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                    skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getHp() - nerfedDamage);
        }
        Text.applyText(player, "&c" + damager.getDisplayName() + " hit you for " + nerfedDamage + "♥!");
        Text.applyText(damager, "&cHit " + player.getDisplayName() + " for " + nerfedDamage + "♥!");
        if (getDefence() > 250 && getHp() > 100) {
            damager.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 0.4f);
        }
        if (getHp() <= 0) {
            killPlayer(damager, skrpg);
        }
        ArmorStand damageIndictator = (ArmorStand) player.getWorld()
                .spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        damageIndictator.setInvulnerable(true);
        damageIndictator.setCollidable(false);
        damageIndictator.setInvisible(true);
        damageIndictator.setCustomName(Text.color("&c" + nerfedDamage + " &4&l☄"));
        damageIndictator.setCustomNameVisible(true);
        damageIndictator.setSmall(true);
        damageIndictator.setVelocity(new Vector(0, 0.5, 0));
        new BukkitRunnable() {
            int quarterSecondsAlive = 0;
            @Override
            public void run() {
                quarterSecondsAlive++;
                if (quarterSecondsAlive == 8) {
                    damageIndictator.remove();
                    cancel();
                }
            }
        }.runTaskTimer(skrpg, 0, 5);


    }
    public void kill(Player player, SKRPG skrpg) {

            if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion() != null) {
                player.teleport(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion().getSpawnLocation());
            } else {
                player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
            }
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
            player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
            Text.applyText(player, "&cYou lost &6" + skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2 + " Nuggets&c!");
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setCredits(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() / 2);

            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                    skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
    }
    public void killPlayer(Player killer, SKRPG skrpg) {
        Player player = Bukkit.getPlayer(uuid);
        if (skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion() != null) {
            player.teleport(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getRegion().getSpawnLocation());
        } else {
            player.teleport(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation());
        }
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.5f);
        player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
        Text.applyText(player, "&cYou were killed by " + killer.getDisplayName() + "&c!");

        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
    }
}
