package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.player.actions.PlayerAction;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.Trade;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.effects.ActiveEffect;
import com.auxdible.skrpg.player.effects.Effects;
import com.auxdible.skrpg.player.guilds.Guild;
import com.auxdible.skrpg.player.inventory.PlayerInventory;
import com.auxdible.skrpg.player.quests.Quest;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyQuest;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.player.skills.*;
import com.auxdible.skrpg.locations.regions.Region;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PlayerData {
    private SKRPG skrpg;
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
    private double credits;
    private Region region;
    private Combat combat;
    private Mining mining;
    private Crafting crafting;
    private Herbalism herbalism;
    private Fishing fishing;
    private ArrayList<Bank> banks;
    private Date intrestDate;
    private int baseSpeed;
    private Rarity sellAboveRarity;
    private boolean toggleTrade;
    private Trade trade;
    private ArrayList<Collection> collections;
    private ArrayList<Quests> completedQuests;
    private List<Quest> activeQuest;
    private List<NPC> renderedNPCs;
    private Runics runics;
    private HashMap<RunicUpgrades, Integer> runicUpgrades;
    private int fifthRunicPoint;
    private PlayerAction playerAction;
    private ArrayList<ActiveEffect> activeEffects;
    private List<SKRPGItemStack> stash;
    private int royaltyQuestSlots;
    private List<RoyaltyQuest> royaltyQuests;
    private int royaltyPoints;
    private HashMap<RoyaltyUpgrades, Integer> royaltyUpgrades;
    private boolean hasRefreshed;
    private int marineLifeCatchChance;
    private boolean canDrop;
    private Global global;
    private Location locationWhereLeft;
    private PlayerInventory playerInventory;
    public PlayerData(SKRPG skrpg, int maxHP, int maxEnergy, int strength, int defence, int speed, UUID uuid, double credits, Combat combat,
                      Mining mining, Herbalism herbalism, Crafting crafting, Runics runics, Fishing fishing, ArrayList<Bank> banks,
                      Date intrestDate, ArrayList<Collection> collections, Rarity sellAboveRarity, boolean toggleTrade, ArrayList<Quests> quests,
                      int runicPoints, HashMap<RunicUpgrades, Integer> runicUpgrades, List<Quest> currentQuest, int royaltyQuestSlots,
                      int royaltyPoints, HashMap<RoyaltyUpgrades, Integer> royaltyUpgrades, boolean canDrop, Location locationWhereLeft,
                      String inventoryData) {
        this.skrpg = skrpg;
        this.marineLifeCatchChance = 0;
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
        this.credits = (Math.round(credits * 100.0) / 100.0);
        this.baseHP = maxHP;
        this.combat = combat;
        this.mining = mining;
        this.herbalism = herbalism;
        this.fishing = fishing;
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
        this.renderedNPCs = new ArrayList<>();
        this.runicPoints = runicPoints;
        this.runics = runics;
        this.runicUpgrades = runicUpgrades;
        this.fifthRunicPoint = 0;
        this.activeEffects = new ArrayList<>();
        this.stash = new ArrayList<>();
        this.royaltyQuests = new ArrayList<>();
        this.royaltyPoints = royaltyPoints;
        this.royaltyUpgrades = royaltyUpgrades;
        this.royaltyQuestSlots = royaltyQuestSlots;
        this.hasRefreshed = false;
        this.canDrop = canDrop;
        this.global = new Global(this, skrpg);
        this.locationWhereLeft = locationWhereLeft;
        if (inventoryData == null || inventoryData.equals("") || inventoryData.equals(" ")) {
            createInventory();
        } else {
            createInventory(inventoryData);
        }
    }
    public Guild getGuild() {
        return skrpg.getGuildManager().getPlayerGuild(this);
    }
    public PlayerInventory getPlayerInventory() {
        if (playerInventory == null) {
            createInventory();
        }
        return playerInventory; }

    public void createInventory(String data) {
        skrpg.getLogger().info(data);
        List<String> createdData = Arrays.asList(data.split(","));
        List<SKRPGItemStack> items = new ArrayList<>();
        List<SKRPGItemStack> equipment = new ArrayList<>();
        for (String string : createdData) {
            if (!string.equals("0") && !string.equals("0*0")) {
                skrpg.getLogger().info(createdData.indexOf(string) + " " + string);
                if (createdData.indexOf(string) >= 37) {

                    List<String> itemData = Arrays.asList(string.split("\\*"));
                    ItemInfo itemInfo = ItemInfo.parseItemInfo(itemData.get(1));
                    SKRPGItemStack skrpgItemStack = new SKRPGItemStack(itemInfo, Integer.parseInt(itemData.get(0)));
                    equipment.add(skrpgItemStack);
                } else {

                    List<String> itemData = Arrays.asList(string.split("\\*"));

                    ItemInfo itemInfo = ItemInfo.parseItemInfo(itemData.get(1));

                    SKRPGItemStack skrpgItemStack = new SKRPGItemStack(itemInfo, Integer.parseInt(itemData.get(0)));
                    items.add(skrpgItemStack);
                }
            } else {
                items.add(null);
            }
        }
        for (SKRPGItemStack skrpgItemStack : items) {
            if (skrpgItemStack != null) {
                if (skrpgItemStack.getItemInfo() != null) {
                    skrpg.getLogger().info(skrpgItemStack.getItemInfo().getItem().getName());
                }
            }
        }
        playerInventory = new PlayerInventory(skrpg, this, items, equipment);
    }
    public void createInventory() {
        playerInventory = new PlayerInventory(skrpg, this, new ArrayList<>(), new ArrayList<>());
    }
    public Location getLocationWhereLeft() { return locationWhereLeft; }

    public void setLocationWhereLeft(Location locationWhereLeft) { this.locationWhereLeft = locationWhereLeft; }

    public Global getGlobal() {
        if (global == null) {
            global = new Global(this, skrpg);
        }
        return global; }

    public void setCanDrop(boolean canDrop) { this.canDrop = canDrop; }
    public boolean canDrop() {

        return canDrop; }

    public Fishing getFishing() {
        if (fishing == null) {
            fishing = new Fishing(Level._0, 0, 0);
        }
        return fishing; }

    public int getMarineLifeCatchChance() { return marineLifeCatchChance; }
    public void setMarineLifeCatchChance(int marineLifeCatchChance) {
        this.marineLifeCatchChance = marineLifeCatchChance;
    }

    public boolean hasRefreshed() { return hasRefreshed; }

    public HashMap<RoyaltyUpgrades, Integer> getRoyaltyUpgrades() { return royaltyUpgrades; }
    public int getRoyaltyPoints() { return royaltyPoints; }

    public void setRoyaltyPoints(int royaltyPoints) { this.royaltyPoints = royaltyPoints; }

    public List<RoyaltyQuest> getRoyaltyQuests() { return royaltyQuests; }
    public int getRoyaltyQuestSlots() { return royaltyQuestSlots; }

    public void setRoyaltyQuestSlots(int royaltyQuestSlots) { this.royaltyQuestSlots = royaltyQuestSlots; }

    public List<SKRPGItemStack> getStash() {
        if (stash == null) {
            stash = new ArrayList<>();
        }
        return stash; }
    public ArrayList<ActiveEffect> getActiveEffects() {
        if (activeEffects == null) {
            activeEffects = new ArrayList<>();
        }
        return activeEffects; }
    public void addEffect(ActiveEffect activeEffect) {
        Player p = Bukkit.getPlayer(uuid);
        int seconds = activeEffect.getSeconds() % 60;
        int minutes = (activeEffect.getSeconds() / 60) % 60;
        if (getEffect(activeEffect.getEffectType()) != null) {
            if (getEffect(activeEffect.getEffectType()).getLevel() < activeEffect.getLevel()) {
                activeEffects.remove(getEffect(activeEffect.getEffectType()));
                activeEffects.add(activeEffect);
            }
            if (getEffect(activeEffect.getEffectType()).getSeconds() < activeEffect.getSeconds()) {
                activeEffects.remove(getEffect(activeEffect.getEffectType()));
                activeEffects.add(activeEffect);
            }
        } else {
            activeEffects.add(activeEffect);
        }
        Text.applyText(p, "&7You applied " + activeEffect.getEffectType().getName() + " " + activeEffect.getLevel() + " &7for &a" + minutes + ":" + seconds + "&7!");
    }
    public ActiveEffect getEffect(Effects effects) {
        for (ActiveEffect activeEffect : activeEffects) {
            if (activeEffect.getEffectType() == effects) {
                return activeEffect;
            }
        }
        return null;
    }
    public void updateEffects() {
        Player p = Bukkit.getPlayer(uuid);
        List<ActiveEffect> expiredEffects = new ArrayList<>();
        for (ActiveEffect activeEffect : getActiveEffects()) {
            activeEffect.setSeconds(activeEffect.getSeconds() - 1);
            if (activeEffect.getSeconds() <= 0) {
                expiredEffects.add(activeEffect);
            }
        }
        for (ActiveEffect expiredEffect : expiredEffects) {
            activeEffects.remove(expiredEffect);
            Text.applyText(p, "&cYour " + expiredEffect.getEffectType().getName() + " " + expiredEffect.getLevel() + " &cexpired!");
        }
    }

    public void setActiveEffects(ArrayList<ActiveEffect> activeEffects) { this.activeEffects = activeEffects; }

    public void setPlayerAction(PlayerAction playerAction) { this.playerAction = playerAction; }
    public HashMap<RunicUpgrades, Integer> getRunicUpgrades() {
        if (runicUpgrades == null) {
            runicUpgrades = new HashMap<>();
        }
        return runicUpgrades;
    }
    public Runics getRunics() {
        if (runics == null) {
            runics = new Runics(Level._0, 0, 0);
        }
        return runics; }
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
    public List<NPC> getRenderedNPCs() {
        if (renderedNPCs == null) {
            renderedNPCs = new ArrayList<>();
        }
        return renderedNPCs; }
    public PlayerAction getPlayerActionManager() {
        if (playerAction == null) {
            playerAction = new PlayerAction(Bukkit.getPlayer(getUuid()), this, skrpg);
        }
        return playerAction; }
    public List<Quest> getActiveQuest() { return activeQuest; }
    public Quest getQuest(Quests questType) {
        for (Quest quest : activeQuest) {
            if (quest.getQuestType() == questType) {
                return quest;
            }
        }
        return null;
    }
    public boolean hasQuest(Quests questType) {
        for (Quest quest : activeQuest) {
            if (quest.getQuestType() == questType) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Quests> getCompletedQuests() {
        return completedQuests; }
    public ArrayList<Collection> getCollections() {
        if (collections == null) {
            collections = new ArrayList<>();
        }
        return collections; }
    public Rarity getSellAboveRarity() {
        if (sellAboveRarity == null) {
            sellAboveRarity = Rarity.COMMON;
        }
        return sellAboveRarity; }
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

    public Mining getMining() {
        if (mining == null) {
            mining = new Mining(Level._0, 0, 0);
        }
        return mining; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) {
        this.region = region;
        if (region == null) {
            Bukkit.getPlayer(uuid).getScoreboard().getTeam("region").setSuffix(Text.color("&7None"));
        } else {
            Bukkit.getPlayer(uuid).getScoreboard().getTeam("region").setSuffix(Text.color(region.getName()));
        }

    }

    public void setRenderedNPCs(List<NPC> renderedNPCs) { this.renderedNPCs = renderedNPCs; }

    public Date getIntrestDate() {
        if (intrestDate == null) {
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate localDate = LocalDate.now();
            intrestDate = java.sql.Date.from(localDate.atStartOfDay(zoneId).toInstant());
        }
        return intrestDate; }
    public void setIntrestDate(Date intrestDate) { this.intrestDate = intrestDate; }
    public ArrayList<Bank> getBanks() {
        if (collections == null) {
            collections = new ArrayList<>();
        }
        return banks; }
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
    public double getCredits() { return credits; }
    public void setCredits(double credits) { this.credits = credits;
    this.credits = (Math.round(credits * 100.0) / 100.0);
    Bukkit.getPlayer(uuid).getScoreboard().getTeam("credits").setSuffix(credits + "");
    }
    public void setDefence(int defence) { this.defence = defence; }
    public void setEnergy(int energy) { this.energy = energy;
        if (getEnergy() > getMaxEnergy()) {
            setEnergy(getMaxEnergy());
        }
    }
    public Herbalism getHerbalism() {
        if (herbalism == null) {
            herbalism = new Herbalism(Level._0, 0, 0);
        }
        return herbalism; }
    public void setHp(int hp) { this.hp = hp;
    if (Bukkit.getPlayer(uuid) == null) { return; }
        if (getHp() > getMaxHP()) {
            setHp(getMaxHP());
        } else {
            if (Math.round(getHp() * 20
                    / getMaxHP()) != 0 && !(Math.round(getHp() * 20
                    / getMaxHP()) < 0)) {

                Bukkit.getPlayer(uuid).setHealth(Math.round(getHp() * 20
                        / getMaxHP()));
            } else {
                Bukkit.getPlayer(uuid).setHealth(1);
            }
        }
        Bukkit.getPlayer(uuid).getScoreboard().getObjective("health").getScore(Bukkit.getPlayer(uuid).getDisplayName()).setScore(getHp());

    }
    public Combat getCombat() {
        if (combat == null) {
            combat = new Combat(Level._0, 0, 0);
        }
        return combat; }
    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setBaseDefence(int setBaseDefence) {
        this.baseDefence = setBaseDefence; this.defence = baseDefence; }
    public void setBaseEnergy(int setBaseEnergy) { this.baseEnergy = setBaseEnergy; this.maxEnergy = setBaseEnergy; }
    public void setBaseHP(int setBaseHP) { this.baseHP = setBaseHP; this.maxHP = setBaseHP; }
    public void setBaseStrength(int setBaseStrength) { this.baseStrength = setBaseStrength; this.strength = setBaseStrength; }
    public Crafting getCrafting() {
        if (crafting == null) {
            crafting = new Crafting(Level._0, 0, 0);
        }
        return crafting; }




}
