package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.player.collections.Collection;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.quests.Quest;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.skills.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

import javax.annotation.Nullable;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Array;
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
                    playerBanks.add(new Bank(rsBank.getInt("bank" + i + "Credits"), BankLevel.valueOf(rsBank.getString("bank" + i + "Level"))));
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
                    skrpg.getLogger().info("Can see collections.");
                    collectionsFound = true;
                    List<String> tiers = Arrays.asList(rsCollection.getString("collectionsTier").split(","));
                    List<String> amounts = Arrays.asList(rsCollection.getString("collectionsAmount").split(","));

                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        try {
                            collections.add(new Collection(Integer.parseInt(amounts.get(i)), Tiers.valueOf("_" + tiers.get(i)), collectionTypes.get(i)));
                            skrpg.getLogger().info("Collection added!" + collectionTypes.get(i));
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
                List<String> completedQuests = new ArrayList<>();
                List<String> allQuests = new ArrayList<>();
                ArrayList<Quests> processedQuests = new ArrayList<>();
                if (!rsStats.getString("questsCompleted").isEmpty()) {
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


                playerData = new PlayerData(rsStats.getInt("baseHP"), rsStats.getInt("baseEnergy"),
                        rsStats.getInt("baseStrength"), rsStats.getInt("baseDefence"), rsStats.getInt("baseSpeed"),
                        player.getUniqueId(), rsStats.getInt("credits"),
                        new Combat(Level.valueOf("_" + rsSkills.getInt("combatLevel")), rsSkills.getInt("combatXpTotal"), rsSkills.getInt("combatXpTill")),
                        new Mining(Level.valueOf("_" + rsSkills.getInt("miningLevel")), rsSkills.getInt("miningXpTotal"), rsSkills.getInt("miningXpTill")),
                        new Herbalism(Level.valueOf("_" + rsSkills.getInt("herbalismLevel")), rsSkills.getInt("herbalismXpTotal"), rsSkills.getInt("herbalismXpTill")),
                        new Crafting(Level.valueOf("_" + rsSkills.getInt("craftingLevel")), rsSkills.getInt("craftingXpTotal"), rsSkills.getInt("craftingXpTill")),
                        playerBanks, calendar.getTime(), collections, Rarity.valueOf(rarityValue), rsStats.getBoolean("canTrade"), processedQuests);
                if (getPlayerData(playerData.getUuid()) == null) {
                    players.add(playerData);
                }
            }
        } catch (SQLException | ParseException x) {
            x.printStackTrace();
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
                    playerBanks.add(new Bank(rsBank.getInt("bank" + i + "Credits"), BankLevel.valueOf(rsBank.getString("bank" + i + "Level"))));
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
                    skrpg.getLogger().info("Can see collections.");
                    collectionsFound = true;
                    List<String> tiers = Arrays.asList(rsCollection.getString("collectionsTier").split(","));
                    List<String> amounts = Arrays.asList(rsCollection.getString("collectionsAmount").split(","));

                    List<CollectionType> collectionTypes = Arrays.asList(CollectionType.values());
                    for (int i = 0; i < collectionTypes.size(); i++) {
                        try {
                            collections.add(new Collection(Integer.parseInt(amounts.get(i)), Tiers.valueOf("_" + tiers.get(i)), collectionTypes.get(i)));
                            skrpg.getLogger().info("Collection added!" + collectionTypes.get(i));
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
                playerData = new PlayerData(rsStats.getInt("baseHP"), rsStats.getInt("baseEnergy"),
                        rsStats.getInt("baseStrength"), rsStats.getInt("baseDefence"), rsStats.getInt("baseSpeed"),
                        player.getUniqueId(), rsStats.getInt("credits"),
                        new Combat(Level.valueOf("_" + rsSkills.getInt("combatLevel")), rsSkills.getInt("combatXpTotal"), rsSkills.getInt("combatXpTill")),
                        new Mining(Level.valueOf("_" + rsSkills.getInt("miningLevel")), rsSkills.getInt("miningXpTotal"), rsSkills.getInt("miningXpTill")),
                        new Herbalism(Level.valueOf("_" + rsSkills.getInt("herbalismLevel")), rsSkills.getInt("herbalismXpTotal"), rsSkills.getInt("herbalismXpTill")),
                        new Crafting(Level.valueOf("_" + rsSkills.getInt("craftingLevel")), rsSkills.getInt("craftingXpTotal"), rsSkills.getInt("craftingXpTill")),
                        playerBanks, calendar.getTime(), collections, Rarity.valueOf(rarityValue), rsStats.getBoolean("canTrade"), processedQuests);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        Calendar calendar = Calendar.getInstance();
        ArrayList<Collection> collectionsGenerated = new ArrayList<>();
        for (CollectionType collectionType : EnumSet.allOf(CollectionType.class)) {
            collectionsGenerated.add(new Collection(0, Tiers._0, collectionType));
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        banks.add(new Bank(0, BankLevel.FREE));
        PlayerData playerData = new PlayerData(100, 100, 1, 1, 100, uuid, 0,
                new Combat(Level._0, 0, 0), new Mining(Level._0, 0, 0),
                new Herbalism(Level._0, 0, 0), new Crafting(Level._0, 0, 0), banks, calendar.getTime(), collectionsGenerated, Rarity.LEGENDARY, true, new ArrayList<>());
        players.add(playerData);
        int canTrade = 0;
        if (playerData.canTrade()) {
            canTrade = 1;
        }
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
        List<String> tiers = new ArrayList<>();
        List<String> amounts = new ArrayList<>();
        StringJoiner stringJoinerTiers = new StringJoiner(",");
        StringJoiner stringJoinerCollections = new StringJoiner(",");
        for (Collection collection : playerData.getCollections()) {
            tiers.add(collection.getTier().toString().replaceAll("_", ""));
            amounts.add(collection.getCollectionAmount() + "");

        }
        for (String tier : tiers) {
            stringJoinerTiers.add(tier);
        }
        for (String count : amounts) {
            stringJoinerCollections.add(count);
        }
        try {
            SKRPG.prepareStatement("INSERT INTO stat_table(UUID, baseHP, baseDefence, baseStrength, baseEnergy, baseSpeed, credits, interest, canTrade, raritySell)" +
                    " VALUES ('" + playerData.getUuid().toString() + "','" + playerData.getBaseHP() + "','" +
                    playerData.getBaseDefence() + "','" + playerData.getBaseStrength() + "','" + playerData.getBaseEnergy() + "','" + playerData.getBaseSpeed() + "','" +
                    playerData.getCredits() + "','" + calendar.get(Calendar.YEAR) + "-"
                    + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "','" + canTrade + "','" + playerData.getSellAboveRarity().toString() + "');").executeUpdate();
            SKRPG.prepareStatement("INSERT INTO skills_table(UUID, miningLevel, miningXpTill, miningXpTotal, " +
                    "herbalismLevel, herbalismXpTill, herbalismXpTotal, craftingLevel, craftingXpTill, craftingXpTotal, " +
                    "combatLevel, combatXpTill, combatXpTotal)" +
                    " VALUES ('" + playerData.getUuid().toString() + "','" + SKRPG.levelToInt(playerData.getMining().getLevel().toString()) + "','" +
                    playerData.getMining().getXpTillNext() + "','" + playerData.getMining().getTotalXP() + "','" +
                    SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) + "','" + playerData.getHerbalism().getXpTillNext() + "','" + playerData.getHerbalism().getTotalXP() + "','" +
                    SKRPG.levelToInt(playerData.getCrafting().getLevel().toString()) + "','" + playerData.getCrafting().getXpTillNext() + "','" + playerData.getCrafting().getTotalXP() + "','" +
                    SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) + "','" + playerData.getCombat().getXpTillNext() + "','" + playerData.getCombat().getTotalXP()
                    + "');").executeUpdate();
            SKRPG.prepareStatement("INSERT INTO banks_table(bank1Level, bank1Credits, bank2Level, " +
                    "bank2Credits, bank3Level, bank3Credits, bank4Level, bank4Credits, bank5Level, " +
                    "bank5Credits, bankAmount, UUID)" +
                    " VALUES ('" + bank1Object.getLevel().toString() + "','" +
                    bank1Object.getCredits() + "','" + bank2Object.getLevel().toString() + "','" +
                    bank2Object.getCredits() + "','" + bank3Object.getLevel().toString() + "','" + bank3Object.getCredits() + "','" +
                    bank4Object.getLevel().toString() + "','" + bank4Object.getCredits() + "','" + bank5Object.getLevel().toString() + "','" +
                    bank5Object.getCredits() + "','" + bankAmount + "','" + playerData.getUuid().toString() + "');").executeUpdate();
            SKRPG.prepareStatement("INSERT INTO collection_table(UUID, collectionsTier, collectionsAmount)" +
                    " VALUES ('" + playerData.getUuid().toString() + "','" +
                    stringJoinerTiers.toString() + "','" + stringJoinerCollections.toString() + "');").executeUpdate();
        } catch (SQLException x) {
            x.printStackTrace();
        }

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
                StringJoiner questsCompletedJoiner = new StringJoiner(",");
                for (Quests quests : playerData.getCompletedQuests()) {
                    questsCompletedJoiner.add(quests.toString());
                }
                if (rs.getInt(1) == 0) {
                    SKRPG.prepareStatement("INSERT INTO stat_table(UUID, baseHP, baseDefence, baseStrength, baseEnergy, baseSpeed, credits, interest, canTrade, raritySell, questsCompleted)" +
                            " VALUES ('" + playerData.getUuid().toString() + "','" + playerData.getBaseHP() + "','" +
                            playerData.getBaseDefence() + "','" + playerData.getBaseStrength() + "','" + playerData.getBaseEnergy() + "','" + playerData.getBaseSpeed() + "','" +
                            playerData.getCredits() + "','" + calendar.get(Calendar.YEAR) + "-"
                            + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "','" + canTrade + "','" + playerData.getSellAboveRarity().toString()  + "','" + questsCompletedJoiner.toString() + "');").executeUpdate();
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
                            " questsCompleted = '" + questsCompletedJoiner.toString() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();

                }
                ResultSet rsSkills = SKRPG.prepareStatement("SELECT COUNT(UUID) FROM skills_table WHERE UUID = '" + playerData.getUuid().toString() + "';").executeQuery();
                rsSkills.next();
                if (rsSkills.getInt(1) == 0) {
                    SKRPG.prepareStatement("INSERT INTO skills_table(UUID, miningLevel, miningXpTill, miningXpTotal, " +
                            "herbalismLevel, herbalismXpTill, herbalismXpTotal, craftingLevel, craftingXpTill, craftingXpTotal, " +
                            "combatLevel, combatXpTill, combatXpTotal)" +
                            " VALUES ('" + playerData.getUuid().toString() + "','" + SKRPG.levelToInt(playerData.getMining().getLevel().toString()) + "','" +
                            playerData.getMining().getXpTillNext() + "','" + playerData.getMining().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getHerbalism().getLevel().toString()) + "','" + playerData.getHerbalism().getXpTillNext() + "','" + playerData.getHerbalism().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getCrafting().getLevel().toString()) + "','" + playerData.getCrafting().getXpTillNext() + "','" + playerData.getCrafting().getTotalXP() + "','" +
                            SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) + "','" + playerData.getCombat().getXpTillNext() + "','" + playerData.getCombat().getTotalXP()
                            + "');").executeUpdate();
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
                            " combatXpTotal = '" + playerData.getCombat().getTotalXP() + "' WHERE UUID = '" + playerData.getUuid().toString() + "';").executeUpdate();
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
