package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.quests.Quest;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.player.skills.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PlayerManager {
    private ArrayList<PlayerData> players;
    private SKRPG skrpg;
    public PlayerManager(SKRPG skrpg) {
        players = new ArrayList<>();
        this.skrpg = skrpg;
    }
    public PlayerData loadMySQLPlayerData(Player player) {
        PlayerData playerData = null;
        try {
            ResultSet rs = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM stat_table WHERE UUID = '" + player.getUniqueId() + "';").executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                createPlayer(player.getUniqueId());
            } else {
                ResultSet rsStats = SKRPG.prepareStatement("SELECT * FROM stat_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsStats.next();
                ResultSet rsSkills = SKRPG.prepareStatement("SELECT * FROM skills_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsSkills.next();
                ResultSet rsBank = SKRPG.prepareStatement("SELECT * FROM banks_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsBank.next();
                ResultSet rsCollection = SKRPG.prepareStatement("SELECT * FROM collection_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                ArrayList<Bank> playerBanks = new ArrayList<>();
                for (int i = 1; i <= rsBank.getInt("bankAmount"); i++) {
                    playerBanks.add(new Bank(Double.parseDouble(rsBank.getString("bank" + i + "Credits")), BankLevel.valueOf(rsBank.getString("bank" + i + "Level"))));
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(sdf.parse(rsStats.getString("interest")));
                calendar.add(Calendar.MONTH, 1);
                LocalDate localDate = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                if (calendar.getTime().before(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                    calendar.setTime(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                    skrpg.getLogger().info(calendar.getTime() + "");
                }
                ArrayList<Collection> collections = new ArrayList<>();
                boolean collectionsFound = false;
                if (rsCollection.next()) {
                    skrpg.getLogger().info("Can see Accumulations.");
                    collectionsFound = true;
                    List<String> tiers = Arrays.asList(rsCollection.getString("collectionsTier").split(","));
                    List<String> amounts = Arrays.asList(rsCollection.getString("collectionsAmount").split(","));

                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        try {
                            collections.add(new Collection(Integer.parseInt(amounts.get(i)), Tiers.valueOf("_" + tiers.get(i)), collectionTypes.get(i)));
                            skrpg.getLogger().info("Accumulation added!" + collectionTypes.get(i));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            skrpg.getLogger().info("Out of bounds.");
                            collections.add(new Collection(0, Tiers._0, collectionTypes.get(i)));
                        }

                    }
                }


                if (!collectionsFound) {
                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        collections.add(new Collection(0, Tiers._0, collectionTypes.get(i)));
                    }
                }
                String rarityValue = rsStats.getString("raritySell");
                if (rsStats.getString("raritySell").isEmpty()) {
                    rarityValue = "LEGENDARY";
                }
                List<String> completedQuests;
                List<String> allQuests = new ArrayList<>();
                ArrayList<Quests> processedQuests = new ArrayList<>();
                if (rsStats.getString("questsCompleted") != null && !rsStats.getString("questsCompleted").isEmpty()) {
                    completedQuests = Arrays.asList(rsStats.getString("questsCompleted").split(","));
                    for (Quests quests : EnumSet.allOf(Quests.class)) {
                        allQuests.add(quests.toString());
                    }
                    for (String quests : completedQuests) {
                        if (allQuests.contains(quests)) {
                            processedQuests.add(Quests.valueOf(quests));
                        }
                    }
                }
                List<String> runicUpgrades = Arrays.asList(rsStats.getString("runicUpgrades").split(","));
                HashMap<RunicUpgrades, Integer> runicUpgradesProcessed = new HashMap<>();
                if (!rsStats.getString("runicUpgrades").equals("")) {
                    for (String string : runicUpgrades) {
                        List<String> splitUpgrade = Arrays.asList(string.split(":"));
                        runicUpgradesProcessed.put(RunicUpgrades.valueOf(splitUpgrade.get(0)), Integer.parseInt(splitUpgrade.get(1)));
                    }
                }
                List<String> royaltyUpgrades = Arrays.asList(rsStats.getString("royaltyUpgrades").split(","));
                HashMap<RoyaltyUpgrades, Integer> royaltyUpgradesProcessed = new HashMap<>();
                if (!rsStats.getString("royaltyUpgrades").equals("")) {
                    for (String string : royaltyUpgrades) {
                        List<String> splitUpgrade = Arrays.asList(string.split(":"));
                        royaltyUpgradesProcessed.put(RoyaltyUpgrades.valueOf(splitUpgrade.get(0)), Integer.parseInt(splitUpgrade.get(1)));
                    }
                }
                List<String> quests = Arrays.asList(rsStats.getString("currentQuest").split(","));
                List<String> questProgress = Arrays.asList(rsStats.getString("currentQuestState").split(","));
                List<Quest> quest = new ArrayList<>();
                for (String questProcess : quests) {
                    try {
                        Quest questCreated = Quests.valueOf(questProcess).getQuest();
                        questCreated.stringToData(questProgress.get(quests.indexOf(questProcess)));
                        questCreated.setSKRPG(skrpg);
                        quest.add(questCreated);
                    } catch (IllegalArgumentException x) {

                    }

                }
                List<String> locations = Arrays.asList(rsStats.getString("location").split("#"));
                Location playerLocation = Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation();
                try {
                    playerLocation = new Location(Bukkit.getWorld(locations.get(0)), Integer.parseInt(locations.get(1)), Integer.parseInt(locations.get(2)),
                            Integer.parseInt(locations.get(3)), Integer.parseInt(locations.get(4)), Integer.parseInt(locations.get(5)));
                } catch (Exception x) {

                }

                playerData = new PlayerData(skrpg, rsStats.getInt("baseHP"), rsStats.getInt("baseEnergy"),
                        rsStats.getInt("baseStrength"), rsStats.getInt("baseDefence"), rsStats.getInt("baseSpeed"),
                        player.getUniqueId(), Double.parseDouble(rsStats.getString("credits")),
                        new Combat(Level.valueOf("_" + rsSkills.getInt("combatLevel")), Double.parseDouble(rsSkills.getString("combatXpTotal")), Double.parseDouble(rsSkills.getString("combatXpTill"))),
                        new Mining(Level.valueOf("_" + rsSkills.getInt("miningLevel")), Double.parseDouble(rsSkills.getString("miningXpTotal")), Double.parseDouble(rsSkills.getString("miningXpTill"))),
                        new Herbalism(Level.valueOf("_" + rsSkills.getInt("herbalismLevel")), Double.parseDouble(rsSkills.getString("herbalismXpTotal")), Double.parseDouble(rsSkills.getString("herbalismXpTill"))),
                        new Crafting(Level.valueOf("_" + rsSkills.getInt("craftingLevel")), Double.parseDouble(rsSkills.getString("craftingXpTotal")), Double.parseDouble(rsSkills.getString("craftingXpTill"))),
                        new Runics(Level.valueOf("_" + rsSkills.getInt("runicLevel")), Double.parseDouble(rsSkills.getString("runicXpTotal")), Double.parseDouble(rsSkills.getString("runicXpTill"))),
                        new Fishing(Level.valueOf("_" + rsSkills.getInt("fishingLevel")), Double.parseDouble(rsSkills.getString("fishingXpTotal")), Double.parseDouble(rsSkills.getString("fishingXpTill"))),
                        playerBanks, calendar.getTime(), collections, Rarity.valueOf(rarityValue), rsStats.getBoolean("canTrade"), processedQuests, rsStats.getInt("runicPoints"), runicUpgradesProcessed,
                        quest, rsStats.getInt("royaltyQuestSlots"), rsStats.getInt("royaltyPoints"), royaltyUpgradesProcessed,rsStats.getBoolean("canDrop"), playerLocation, rsStats.getString("inventoryData"));
                for (Quest quest1 : playerData.getActiveQuest()) {
                    quest1.setPlayerData(playerData);
                }
                if (getPlayerData(playerData.getUuid()) == null) {
                    players.add(playerData);
                } else {
                    getPlayerData(player.getUniqueId()).createInventory(rsStats.getString("inventoryData"));
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }
        return playerData;
    }
    public PlayerData loadOfflineMySQLData(OfflinePlayer player) {
        PlayerData playerData = null;
        try {
            ResultSet rs = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM stat_table WHERE UUID = '" + player.getUniqueId() + "';").executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                createPlayer(player.getUniqueId());
            } else {
                ResultSet rsStats = SKRPG.prepareStatement("SELECT * FROM stat_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsStats.next();
                ResultSet rsSkills = SKRPG.prepareStatement("SELECT * FROM skills_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsSkills.next();
                ResultSet rsBank = SKRPG.prepareStatement("SELECT * FROM banks_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();
                rsBank.next();
                ResultSet rsCollection = SKRPG.prepareStatement("SELECT * FROM collection_table WHERE " +
                        "UUID = '" + player.getUniqueId() + "';").executeQuery();

                ArrayList<Bank> playerBanks = new ArrayList<>();
                for (int i = 1; i <= rsBank.getInt("bankAmount"); i++) {
                    playerBanks.add(new Bank(Double.parseDouble(rsBank.getString("bank" + i + "Credits")), BankLevel.valueOf(rsBank.getString("bank" + i + "Level"))));
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(sdf.parse(rsStats.getString("interest")));
                calendar.add(Calendar.MONTH, 1);
                LocalDate localDate = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                if (calendar.getTime().before(Date.from(localDate.atStartOfDay(zoneId).toInstant()))) {
                    calendar.setTime(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                }
                ArrayList<Collection> collections = new ArrayList<>();

                boolean collectionsFound = false;
                if (rsCollection.next()) {
                    skrpg.getLogger().info("Can see Accumulations.");
                    collectionsFound = true;
                    List<String> tiers = Arrays.asList(rsCollection.getString("collectionsTier").split(","));
                    List<String> amounts = Arrays.asList(rsCollection.getString("collectionsAmount").split(","));

                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        try {
                            collections.add(new Collection(Integer.parseInt(amounts.get(i)), Tiers.valueOf("_" + tiers.get(i)), collectionTypes.get(i)));
                            skrpg.getLogger().info("Accumulation added!" + collectionTypes.get(i));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            skrpg.getLogger().info("Out of bounds.");
                            collections.add(new Collection(0, Tiers._0, collectionTypes.get(i)));
                        }

                    }
                }


                if (!collectionsFound) {
                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        collections.add(new Collection(0, Tiers._0, collectionTypes.get(i)));
                    }
                }
                String rarityValue = rsStats.getString("raritySell");
                if (rsStats.getString("raritySell").isEmpty()) {
                    rarityValue = "LEGENDARY";
                }
                List<String> completedQuests = Arrays.asList(rsStats.getString("questsCompleted").split(","));
                List<String> allQuests = new ArrayList<>();
                ArrayList<Quests> processedQuests = new ArrayList<>();
                for (Quests quests : EnumSet.allOf(Quests.class)) {
                    allQuests.add(quests.toString());
                }
                for (String quests : completedQuests) {
                    if (allQuests.contains(quests)) {
                        processedQuests.add(Quests.valueOf(quests));
                    }
                }
                List<String> runicUpgrades = Arrays.asList(rsStats.getString("runicUpgrades").split(","));
                HashMap<RunicUpgrades, Integer> runicUpgradesProcessed = new HashMap<>();
                if (!rsStats.getString("runicUpgrades").equals("")) {
                    for (String string : runicUpgrades) {
                        List<String> splitUpgrade = Arrays.asList(string.split(":"));
                        runicUpgradesProcessed.put(RunicUpgrades.valueOf(splitUpgrade.get(0)), Integer.parseInt(splitUpgrade.get(1)));
                    }
                }
                List<String> royaltyUpgrades = Arrays.asList(rsStats.getString("royaltyUpgrades").split(","));
                HashMap<RoyaltyUpgrades, Integer> royaltyUpgradesProcessed = new HashMap<>();
                if (!rsStats.getString("royaltyUpgrades").equals("")) {
                    for (String string : royaltyUpgrades) {
                        List<String> splitUpgrade = Arrays.asList(string.split(":"));
                        royaltyUpgradesProcessed.put(RoyaltyUpgrades.valueOf(splitUpgrade.get(0)), Integer.parseInt(splitUpgrade.get(1)));
                    }
                }
                List<String> quests = Arrays.asList(rsStats.getString("currentQuest").split(","));
                List<String> questProgress = Arrays.asList(rsStats.getString("currentQuestState").split(","));
                List<Quest> quest = new ArrayList<>();
                for (String questProcess : quests) {
                    try {
                        Quest questCreated = Quests.valueOf(questProcess).getQuest();
                        questCreated.stringToData(questProgress.get(quests.indexOf(questProcess)));
                        questCreated.setSKRPG(skrpg);
                        quest.add(questCreated);
                    } catch (IllegalArgumentException x) {

                    }

                }
                List<String> locations = Arrays.asList(rsStats.getString("location").split("#"));
                Location playerLocation = Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).getSpawnLocation();
                try {
                    playerLocation = new Location(Bukkit.getWorld(locations.get(0)), Integer.parseInt(locations.get(1)), Integer.parseInt(locations.get(2)),
                            Integer.parseInt(locations.get(3)), Integer.parseInt(locations.get(4)), Integer.parseInt(locations.get(5)));
                } catch (Exception x) {

                }

                playerData = new PlayerData(skrpg, rsStats.getInt("baseHP"), rsStats.getInt("baseEnergy"),
                        rsStats.getInt("baseStrength"), rsStats.getInt("baseDefence"), rsStats.getInt("baseSpeed"),
                        player.getUniqueId(), Double.parseDouble(rsStats.getString("credits")),
                        new Combat(Level.valueOf("_" + rsSkills.getInt("combatLevel")), Double.parseDouble(rsSkills.getString("combatXpTotal")), Double.parseDouble(rsSkills.getString("combatXpTill"))),
                        new Mining(Level.valueOf("_" + rsSkills.getInt("miningLevel")), Double.parseDouble(rsSkills.getString("miningXpTotal")), Double.parseDouble(rsSkills.getString("miningXpTill"))),
                        new Herbalism(Level.valueOf("_" + rsSkills.getInt("herbalismLevel")), Double.parseDouble(rsSkills.getString("herbalismXpTotal")), Double.parseDouble(rsSkills.getString("herbalismXpTill"))),
                        new Crafting(Level.valueOf("_" + rsSkills.getInt("craftingLevel")), Double.parseDouble(rsSkills.getString("craftingXpTotal")), Double.parseDouble(rsSkills.getString("craftingXpTill"))),
                        new Runics(Level.valueOf("_" + rsSkills.getInt("runicLevel")), Double.parseDouble(rsSkills.getString("runicXpTotal")), Double.parseDouble(rsSkills.getString("runicXpTill"))),
                        new Fishing(Level.valueOf("_" + rsSkills.getInt("fishingLevel")), Double.parseDouble(rsSkills.getString("fishingXpTotal")), Double.parseDouble(rsSkills.getString("fishingXpTill"))),
                        playerBanks, calendar.getTime(), collections, Rarity.valueOf(rarityValue), rsStats.getBoolean("canTrade"), processedQuests, rsStats.getInt("runicPoints"), runicUpgradesProcessed,
                        quest, rsStats.getInt("royaltyQuestSlots"), rsStats.getInt("royaltyPoints"), royaltyUpgradesProcessed,rsStats.getBoolean("canDrop"), playerLocation, rsStats.getString("inventoryData"));
                if (getPlayerData(playerData.getUuid()) == null) {
                    players.add(playerData);
                }
            }
        } catch (SQLException | ParseException x) {
            x.printStackTrace();
        }
        return playerData;
    }
    public ArrayList<PlayerData> getPlayers() { return players; }
    public PlayerData getPlayerData(UUID uuid) {
        for (PlayerData playerData : getPlayers()) {
            if (playerData.getUuid().equals(uuid)) { return playerData; }
        }
        return null;
    }
    public void removePlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            Bukkit.getPlayer(uuid).getInventory().clear();
        }
        this.players.remove(getPlayerData(uuid));
        try {
            SKRPG.prepareStatement("DELETE FROM stat_table WHERE UUID = '" + uuid + "';").executeUpdate();
            SKRPG.prepareStatement("DELETE FROM banks_table WHERE UUID = '" + uuid + "';").executeUpdate();
            SKRPG.prepareStatement("DELETE FROM skills_table WHERE UUID = '" + uuid + "';").executeUpdate();
            SKRPG.prepareStatement("DELETE FROM collection_table WHERE UUID = '" + uuid + "';").executeUpdate();
        } catch (SQLException x) {
            x.printStackTrace();
        }

    }
    public void createPlayer(UUID uuid) {
        ArrayList<Bank> banks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        ArrayList<Collection> collectionsGenerated = new ArrayList<>();
        for (CollectionType collectionType : EnumSet.allOf(CollectionType.class)) {
            collectionsGenerated.add(new Collection(0, Tiers._0, collectionType));
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        banks.add(new Bank(0, BankLevel.FREE));
        PlayerData playerData = new PlayerData(skrpg, 100, 100, 1, 1, 100, uuid, 0,
                new Combat(Level._0, 0, 0), new Mining(Level._0, 0, 0),
                new Herbalism(Level._0, 0, 0), new Crafting(Level._0, 0, 0),
                new Runics(Level._0, 0, 0), new Fishing(Level._0, 0, 0), banks, calendar.getTime(), collectionsGenerated, Rarity.LEGENDARY, true, new ArrayList<>(), 0, new HashMap<>(), new ArrayList<>(), 0, 0, new HashMap<>(), false, skrpg.getLocationManager().getMonolithSpawnLocation(), "");

        players.add(playerData);

        // I'll add this code back if the method starts glitching out < TODO
        // try {
        //
        //            SKRPG.prepareStatement("INSERT INTO stat_table(UUID, baseHP, baseDefence, baseStrength, baseEnergy, baseSpeed, credits, interest, canTrade, raritySell, questsCompleted, runicPoints, runicUpgrades, currentQuestState, currentQuest, royaltyQuestSlots)" +
        //                    " VALUES ('" + playerData.getUuid().toString() + "','" + playerData.getBaseHP() + "','" +
        //                    playerData.getBaseDefence() + "','" + playerData.getBaseStrength() + "','" + playerData.getBaseEnergy() + "','" + playerData.getBaseSpeed() + "','" +
        //                    playerData.getCredits() + "','" + calendar.get(Calendar.YEAR) + "-"
        //                    + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "','" + canTrade + "','" + playerData.getSellAboveRarity().toString() + "','','" + 0 + "','','" + playerData.getQuestPhase() + "','" +
        //                    "','" + playerData.getRoyaltyQuestSlots() + "');").executeUpdate();
        //            SKRPG.prepareStatement("INSERT INTO skills_table(UUID, miningLevel, miningXpTill, miningXpTotal, " +
        //                    "herbalismLevel, herbalismXpTill, herbalismXpTotal, craftingLevel, craftingXpTill, craftingXpTotal, " +
        //                    "combatLevel, combatXpTill, combatXpTotal, runicLevel, runicXpTill, runicXpTotal)" +
        //                    " VALUES ('" + playerData.getUuid().toString() + "','" + SKRPG.levelToInt(playerData.getMining().getLevel().toString()) + "','" +
        //                    playerData.getMining().getXpTillNext() + "','" + playerData.getMining().getTotalXP() + "','" +
        //                    SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) + "','" + playerData.getHerbalism().getXpTillNext() + "','" + playerData.getHerbalism().getTotalXP() + "','" +
        //                    SKRPG.levelToInt(playerData.getCrafting().getLevel().toString()) + "','" + playerData.getCrafting().getXpTillNext() + "','" + playerData.getCrafting().getTotalXP() + "','" +
        //                    SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) + "','" + playerData.getCombat().getXpTillNext() + "','" + playerData.getCombat().getTotalXP() + "','" +
        //                    + SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) + "','" + playerData.getRunics().getXpTillNext() + "','" + playerData.getRunics().getTotalXP() +
        //                    "');").executeUpdate();
        //            SKRPG.prepareStatement("INSERT INTO banks_table(bank1Level, bank1Credits, bank2Level, " +
        //                    "bank2Credits, bank3Level, bank3Credits, bank4Level, bank4Credits, bank5Level, " +
        //                    "bank5Credits, bankAmount, UUID)" +
        //                    " VALUES ('" + bank1Object.getLevel().toString() + "','" +
        //                    bank1Object.getCredits() + "','" + bank2Object.getLevel().toString() + "','" +
        //                    bank2Object.getCredits() + "','" + bank3Object.getLevel().toString() + "','" + bank3Object.getCredits() + "','" +
        //                    bank4Object.getLevel().toString() + "','" + bank4Object.getCredits() + "','" + bank5Object.getLevel().toString() + "','" +
        //                    bank5Object.getCredits() + "','" + bankAmount + "','" + playerData.getUuid().toString() + "');").executeUpdate();
        //            SKRPG.prepareStatement("INSERT INTO collection_table(UUID, collectionsTier, collectionsAmount)" +
        //                    " VALUES ('" + playerData.getUuid().toString() + "','" +
        //                    stringJoinerTiers.toString() + "','" + stringJoinerCollections.toString() + "');").executeUpdate();
        //        } catch (SQLException x) {
        //            x.printStackTrace();
        //        }

    }

    public void disableMySQL() {
        HashSet<PlayerData> playerDataDupe = new HashSet<>(players);
        players = new ArrayList<>(playerDataDupe);

        for (PlayerData playerData : players) {
            try {

                ResultSet rs = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM stat_table WHERE UUID = '" + playerData.getUuid().toString() + "';").executeQuery();
                rs.next();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(playerData.getIntrestDate());
                int canTrade = 0;
                if (playerData.canTrade()) {
                    canTrade = 1;
                }
                int canDrop = 0;
                if (playerData.canDrop()) {
                    canDrop = 1;
                }
                StringJoiner questsCompletedJoiner = new StringJoiner(",");
                for (Quests quests : playerData.getCompletedQuests()) {
                    questsCompletedJoiner.add(quests.toString());
                }
                StringJoiner runicUpgradesJoiner = new StringJoiner(",");
                if (!playerData.getRunicUpgrades().keySet().isEmpty()) {
                    for (RunicUpgrades runicUpgrades : playerData.getRunicUpgrades().keySet()) {
                        runicUpgradesJoiner.add(runicUpgrades.toString() + ":" + playerData.getRunicUpgrades().get(runicUpgrades));
                    }
                }
                StringJoiner royaltyUpgradesJoiner = new StringJoiner(",");
                if (!playerData.getRoyaltyUpgrades().keySet().isEmpty()) {
                    for (RoyaltyUpgrades royaltyUpgrades : playerData.getRoyaltyUpgrades().keySet()) {
                        royaltyUpgradesJoiner.add(royaltyUpgrades.toString() + ":" + playerData.getRoyaltyUpgrades().get(royaltyUpgrades));
                    }
                }
                StringJoiner quest = new StringJoiner(",");
                StringJoiner questProgress = new StringJoiner(",");
                if (!playerData.getActiveQuest().isEmpty()) {
                    for (Quest quests : playerData.getActiveQuest()) {
                        quest.add(quests.getQuestType().toString());
                        questProgress.add(quests.parseData() + "");
                    }
                }

                if (rs.getInt(1) == 0) {
                    SKRPG.prepareStatement("INSERT INTO stat_table(UUID, baseHP, baseDefence, baseStrength, baseEnergy, baseSpeed, credits, interest, canTrade, raritySell, questsCompleted, runicPoints, runicUpgrades, currentQuestState, currentQuest, royaltyQuestSlots, royaltyPoints, royaltyUpgrades, canDrop, inventoryData, location)" +
                            " VALUES ('" + playerData.getUuid().toString() + "','" + playerData.getBaseHP() + "','" +
                            playerData.getBaseDefence() + "','" + playerData.getBaseStrength() + "','" + playerData.getBaseEnergy() + "','" + playerData.getBaseSpeed() + "','" +
                            playerData.getCredits() + "','" + calendar.get(Calendar.YEAR) + "-"
                            + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "','" + canTrade + "','" +
                            playerData.getSellAboveRarity().toString()  + "','" + questsCompletedJoiner.toString() + "','" + playerData.getRunicPoints() +  "','" + runicUpgradesJoiner.toString() + "','" + questProgress.toString() + "','" + quest.toString() + "','"
                            + playerData.getRoyaltyQuestSlots() + "','" + playerData.getRoyaltyPoints() + "','" + royaltyUpgradesJoiner.toString() + "','" +
                            canDrop + "','" + playerData.getPlayerInventory().generateInventorySave() + "','" +
                            playerData.getLocationWhereLeft().getWorld().getName() + "#" +
                            playerData.getLocationWhereLeft().getX() + "#" +
                            playerData.getLocationWhereLeft().getY() + "#" +
                            playerData.getLocationWhereLeft().getZ() + "#" +
                            playerData.getLocationWhereLeft().getYaw() + "#" +
                            playerData.getLocationWhereLeft().getPitch() + "');").executeUpdate();
                } else {
                    SKRPG.prepareStatement("UPDATE stat_table SET baseHP = '" + playerData.getBaseHP() + "'," +
                            " baseDefence = '" + playerData.getBaseDefence() + "'," +
                            " baseStrength = '" + playerData.getBaseStrength() + "'," +
                            " baseEnergy = '" + playerData.getBaseEnergy() + "'," +
                            " baseSpeed = '" + playerData.getBaseSpeed() + "'," +
                            " credits = '" + playerData.getCredits() + "'," +
                            " interest = '" + calendar.get(Calendar.YEAR) + "-"
                            + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) +  "'," +
                            " canTrade = '" + canTrade + "'," +
                            " raritySell = '" + playerData.getSellAboveRarity().toString() + "'," +
                            " questsCompleted = '" + questsCompletedJoiner.toString() + "'," +
                            " runicPoints = '" + playerData.getRunicPoints() + "'," +
                            " runicUpgrades = '" + runicUpgradesJoiner.toString() + "'," +
                            " currentQuestState = '" + questProgress.toString() + "'," +
                            " currentQuest = '" + quest.toString() + "'," +
                            " royaltyQuestSlots = '" + playerData.getRoyaltyQuestSlots() + "'," +
                            " royaltyPoints = '" + playerData.getRoyaltyPoints() + "'," +
                            " royaltyUpgrades = '" + royaltyUpgradesJoiner.toString() + "'," +
                            " canDrop = '" + canDrop + "',"  +
                            " inventoryData = '" + playerData.getPlayerInventory().generateInventorySave() + "'," +
                            " location = '" + playerData.getLocationWhereLeft().getWorld().getName() + "#" +
                            playerData.getLocationWhereLeft().getX() + "#" +
                            playerData.getLocationWhereLeft().getY() + "#" +
                            playerData.getLocationWhereLeft().getZ() + "#" +
                            playerData.getLocationWhereLeft().getYaw() + "#" +
                            playerData.getLocationWhereLeft().getPitch() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();

                }
                ResultSet rsSkills = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM skills_table WHERE UUID = '" + playerData.getUuid().toString() + "';").executeQuery();
                rsSkills.next();
                if (rsSkills.getInt(1) == 0) {
                    SKRPG.prepareStatement("INSERT INTO skills_table(UUID, miningLevel, miningXpTill, miningXpTotal, " +
                            "herbalismLevel, herbalismXpTill, herbalismXpTotal, craftingLevel, craftingXpTill, craftingXpTotal, " +
                            "combatLevel, combatXpTill, combatXpTotal, runicLevel, runicXpTill, runicXpTotal, fishingLevel, fishingXpTill, fishingXpTotal)" +
                            " VALUES ('" + playerData.getUuid().toString() + "','" + SKRPG.levelToInt(playerData.getMining().getLevel().toString()) + "','" +
                            playerData.getMining().getXpTillNext() + "','" + playerData.getMining().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) + "','" + playerData.getHerbalism().getXpTillNext() + "','" + playerData.getHerbalism().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getCrafting().getLevel().toString()) + "','" + playerData.getCrafting().getXpTillNext() + "','" + playerData.getCrafting().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) + "','" + playerData.getCombat().getXpTillNext() + "','" + playerData.getCombat().getTotalXP()
                            + "','" +
                            SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) + "','" + playerData.getRunics().getXpTillNext() + "','" + playerData.getRunics().getTotalXP()
                            + "','" + SKRPG.levelToInt(playerData.getFishing().getLevel().toString()) + "','" + playerData.getFishing().getXpTillNext() + "','" + playerData.getFishing().getTotalXP() + "');").executeUpdate();
                } else {
                    SKRPG.prepareStatement("UPDATE skills_table SET miningLevel = '" + SKRPG.levelToInt(playerData.getMining().getLevel().toString()) + "'," +
                            " miningXpTill = '" + playerData.getMining().getXpTillNext() + "'," +
                            " miningXpTotal = '" + playerData.getMining().getTotalXP() + "'," +
                            " herbalismLevel = '" + SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) + "'," +
                            " herbalismXpTill = '" + playerData.getHerbalism().getXpTillNext() + "'," +
                            " herbalismXpTotal = '" + playerData.getHerbalism().getTotalXP() + "'," +
                            " craftingLevel = '" + SKRPG.levelToInt(playerData.getCrafting().getLevel().toString())+ "'," +
                            " craftingXpTill = '" + playerData.getCrafting().getXpTillNext() + "'," +
                            " craftingXpTotal = '" + playerData.getCrafting().getTotalXP() + "'," +
                            " combatLevel = '" + SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) + "'," +
                            " combatXpTill = '" + playerData.getCombat().getXpTillNext() + "'," +
                            " combatXpTotal = '" + playerData.getCombat().getTotalXP() + "'," +
                            " runicLevel = '" + SKRPG.levelToInt(playerData.getRunics().getLevel().toString()) + "'," +
                            " runicXpTill = '" + playerData.getRunics().getXpTillNext() + "'," +
                            " runicXpTotal = '" + playerData.getRunics().getTotalXP() + "'," +
                            " fishingLevel = '" + SKRPG.levelToInt(playerData.getFishing().getLevel().toString()) + "'," +
                            " fishingXpTill = '" + playerData.getFishing().getXpTillNext() + "'," +
                            " fishingXpTotal = '" + playerData.getFishing().getTotalXP() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();
                }
                ResultSet rsBanks = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM banks_table WHERE UUID = '" + playerData.getUuid().toString() + "';").executeQuery();
                rsBanks.next();
                int bankAmount = playerData.getBanks().size();

                Bank bank1Object = new Bank(-1, BankLevel.NULL), bank2Object = new Bank(-1, BankLevel.NULL), bank3Object = new Bank(-1, BankLevel.NULL), bank4Object = new Bank(-1, BankLevel.NULL), bank5Object = new Bank(-1, BankLevel.NULL);
                for (int i = 0; i < playerData.getBanks().size(); i++) {
                    if (i + 1 == 1) {
                        bank1Object = playerData.getBanks().get(i);
                    } else if (i + 1 == 2) {
                        bank2Object = playerData.getBanks().get(i);
                    } else if (i + 1 == 3) {
                        bank3Object = playerData.getBanks().get(i);
                    } else if (i + 1 == 4) {
                        bank4Object = playerData.getBanks().get(i);
                    } else if (i + 1 == 5) {
                        bank5Object = playerData.getBanks().get(i);
                    }
                }
                if (rsBanks.getInt(1) == 0) {

                    SKRPG.prepareStatement("INSERT INTO banks_table(bank1Level, bank1Credits, bank2Level, " +
                            "bank2Credits, bank3Level, bank3Credits, bank4Level, bank4Credits, bank5Level, " +
                            "bank5Credits, bankAmount, UUID)" +
                            " VALUES ('" + bank1Object.getLevel().toString() + "','" +
                            bank1Object.getCredits() + "','" + bank2Object.getLevel().toString()  + "','" +
                            bank2Object.getCredits() + "','" + bank3Object.getLevel().toString()  + "','" + bank3Object.getCredits() + "','" +
                            bank4Object.getLevel().toString()  + "','" + bank4Object.getCredits() + "','" + bank5Object.getLevel().toString()  + "','" +
                            bank5Object.getCredits() + "','"  + bankAmount + "','" + playerData.getUuid().toString() + "');").executeUpdate();
                } else {
                    SKRPG.prepareStatement("UPDATE banks_table SET bankAmount = '" + bankAmount + "', bank1Level = '" + bank1Object.getLevel().toString() + "'," +
                            " bank1Credits = '" + bank1Object.getCredits() + "'," +
                            " bank2Level = '" + bank2Object.getLevel().toString() + "'," +
                            " bank2Credits = '" + bank2Object.getCredits() + "'," +
                            " bank3Level = '" + bank3Object.getLevel().toString() + "'," +
                            " bank3Credits = '" + bank3Object.getCredits() + "'," +
                            " bank4Level = '" + bank4Object.getLevel().toString() + "'," +
                            " bank4Credits = '" + bank4Object.getCredits() + "'," +
                            " bank5Level = '" + bank5Object.getLevel().toString() + "'," +
                            " bank5Credits = '" + bank5Object.getCredits() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();
                }
                ResultSet rsCollections = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM collection_table WHERE UUID = '" + playerData.getUuid().toString() + "';").executeQuery();
                rsCollections.next();
                List<String> tiers = new ArrayList<>();
                List<String> amounts = new ArrayList<>();
                StringJoiner stringJoinerTiers = new StringJoiner(",");
                StringJoiner stringJoinerCollections = new StringJoiner(",");
                for (Collection collection : playerData.getCollections()) {
                    tiers.add(collection.getTier().toString().replaceAll("_", ""));
                    amounts.add(collection.getCollectionAmount() + "");
                    skrpg.getLogger().info(collection.getTier().toString().replaceAll("_", "") + " = " + collection.getCollectionAmount());
                }
                for (String tier : tiers) {
                    stringJoinerTiers.add(tier);
                }
                for (String count : amounts) {
                    stringJoinerCollections.add(count);
                }
                if (rsCollections.getInt(1) == 0) {
                    SKRPG.prepareStatement("INSERT INTO collection_table(UUID, collectionsTier, collectionsAmount)" +
                            " VALUES ('" + playerData.getUuid().toString() + "','" +
                            stringJoinerTiers.toString() + "','" + stringJoinerCollections.toString()  + "');").executeUpdate();
                } else {
                    SKRPG.prepareStatement("UPDATE collection_table SET collectionsTier = '" + stringJoinerTiers.toString() + "'," +
                            " collectionsAmount = '" + stringJoinerCollections.toString() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();
                }
            } catch (SQLException x) {
                x.printStackTrace();
            }

        }
    }
}
