package com.auxdible.skrpg.player;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.player.economy.BankLevel;
import com.auxdible.skrpg.player.skills.*;

import java.io.IOException;
import java.sql.Date;
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

    public void enable() {
        if (skrpg.getPlayerData().getConfigurationSection("playerData") == null) { return;}
        if (skrpg.getPlayerData().getConfigurationSection("playerData").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getPlayerData().getConfigurationSection("playerData").getKeys(false)) {
            int maxHealth = skrpg.getPlayerData().getInt("playerData." + s + ".baseHP");
            int defence = skrpg.getPlayerData().getInt("playerData." + s + ".baseDefence");
            int strength = skrpg.getPlayerData().getInt("playerData." + s + ".baseStrength");
            int maxEnergy = skrpg.getPlayerData().getInt("playerData." + s + ".baseEnergy");
            int baseSpeed = skrpg.getPlayerData().getInt("playerData." + s + ".baseSpeed");
            int coins = skrpg.getPlayerData().getInt("playerData." + s + ".credits");
            int combatLevel = skrpg.getPlayerData().getInt("playerData." + s + ".combat.level");
            int combatXPTotal = skrpg.getPlayerData().getInt("playerData." + s + ".combat.xpTotal");
            int combatXPTill = skrpg.getPlayerData().getInt("playerData." + s + ".combat.xpTill");
            int miningLevel = skrpg.getPlayerData().getInt("playerData." + s + ".mining.level");
            int miningXPTotal = skrpg.getPlayerData().getInt("playerData." + s + ".mining.xpTotal");
            int miningXPTill = skrpg.getPlayerData().getInt("playerData." + s + ".mining.xpTill");
            int herbalismLevel = skrpg.getPlayerData().getInt("playerData." + s + ".herbalism.level");
            int herbalismXPTotal = skrpg.getPlayerData().getInt("playerData." + s + ".herbalism.xpTotal");
            int herbalismXPTill = skrpg.getPlayerData().getInt("playerData." + s + ".herbalism.xpTill");
            int craftingLevel = skrpg.getPlayerData().getInt("playerData." + s + ".crafting.level");
            int craftingXPTotal = skrpg.getPlayerData().getInt("playerData." + s + ".crafting.xpTotal");
            int craftingXPTill = skrpg.getPlayerData().getInt("playerData." + s + ".crafting.xpTill");
            ArrayList<Bank> banks = new ArrayList<>();

            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate currentDate = LocalDate.now();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
            Calendar calendar = Calendar.getInstance();
            for (String bankString :
                    skrpg.getPlayerData().getConfigurationSection("playerData." + s + ".banks").getKeys(false)) {
                int credits = skrpg.getPlayerData().getInt("playerData." + s + ".banks." + bankString + ".credits");
                BankLevel bankLevel = BankLevel.valueOf(
                        skrpg.getPlayerData().getString("playerData." + s + ".banks." + bankString + ".level"));

                banks.add(new Bank(credits, bankLevel));
            }
            if (skrpg.getPlayerData().getString("playerData." + s + ".interest") != null) {
                try {
                    calendar.setTime(sdf.parse(skrpg.getPlayerData().getString("playerData." + s + ".interest")));
                } catch (ParseException x) {
                    x.printStackTrace();
                }
            } else {
                calendar.setTime(Date.from(currentDate.atStartOfDay(zoneId).toInstant()));
            }

            players.add(new PlayerData(maxHealth, maxEnergy, strength, defence, baseSpeed, UUID.fromString(s), coins,
                    new Combat(Level.valueOf("_" + combatLevel), combatXPTotal, combatXPTill),
                    new Mining(Level.valueOf("_" + miningLevel), miningXPTotal, miningXPTill),
                    new Herbalism(Level.valueOf("_" + herbalismLevel), herbalismXPTotal, herbalismXPTill),
                    new Crafting(Level.valueOf("_" + craftingLevel), craftingXPTotal, craftingXPTill), banks, calendar.getTime()));


        }
        HashSet<PlayerData> playerDataDupe = new HashSet<>(players);
        players = new ArrayList<>(playerDataDupe);
    }
    public void disable() {
        ArrayList<UUID> implemented = new ArrayList<>();
        for (PlayerData player : players) {
            if (!implemented.contains(player.getUuid())) {
                implemented.add(player.getUuid());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".baseHP", player.getBaseHP());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".baseDefence", player.getBaseDefence());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".baseStrength", player.getBaseStrength());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".baseEnergy", player.getBaseEnergy());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".baseSpeed", player.getBaseSpeed());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".credits", player.getCredits());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".combat.level", Integer.parseInt(player.getCombat().getLevel()
                        .toString().replace("_", "")));
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".combat.xpTotal", player.getMining().getTotalXP());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".combat.xpTill", player.getMining().getXpTillNext());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".mining.level", Integer.parseInt(player.getMining().getLevel()
                        .toString().replace("_", "")));
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".mining.xpTotal", player.getMining().getTotalXP());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".mining.xpTill", player.getMining().getXpTillNext());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".herbalism.level", Integer.parseInt(player.getHerbalism().getLevel()
                        .toString().replace("_", "")));
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".herbalism.xpTotal", player.getHerbalism().getTotalXP());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".herbalism.xpTill", player.getHerbalism().getXpTillNext());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".crafting.level", Integer.parseInt(player.getCrafting().getLevel()
                        .toString().replace("_", "")));
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".crafting.xpTotal", player.getCrafting().getTotalXP());
                skrpg.getPlayerData().set("playerData." + player.getUuid() + ".crafting.xpTill", player.getCrafting().getXpTillNext());
                for (Bank bank : player.getBanks()) {
                    skrpg.getPlayerData().set("playerData." + player.getUuid() + ".banks." +
                            player.getBanks().lastIndexOf(bank) + ".credits", bank.getCredits());
                    skrpg.getPlayerData().set("playerData." + player.getUuid() + ".banks." +
                            player.getBanks().lastIndexOf(bank) + ".level", bank.getLevel().toString());
                }
                Calendar calendar = Calendar.getInstance();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate currentDate = LocalDate.now();
                Calendar dayCalendar = Calendar.getInstance();
                calendar.setTime(player.getIntrestDate());
                dayCalendar.setTime(Date.from(currentDate.atStartOfDay(zoneId).toInstant()));
                if (calendar.get(Calendar.DAY_OF_MONTH) == dayCalendar.get(Calendar.DAY_OF_MONTH)) {
                    skrpg.getPlayerData().set("playerData." + player.getUuid() + ".interest", calendar.get(Calendar.YEAR) + "-"
                            + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + 1);
                } else {
                    skrpg.getPlayerData().set("playerData." + player.getUuid() + ".interest", calendar.get(Calendar.YEAR) + "-"
                            + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                }

                try {
                    skrpg.getPlayerData().save(skrpg.getPlayerDataFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<PlayerData> getPlayers() { return players; }
    public PlayerData getPlayerData(UUID uuid) {
        for (PlayerData playerData : getPlayers()) {
            if (playerData.getUuid().equals(uuid)) { return playerData; }
        }
        return null;
    }
    public void createPlayer(UUID uuid) {
        ArrayList<Bank> banks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        banks.add(new Bank(0, BankLevel.FREE));
        players.add(new PlayerData(100, 100, 1, 1, 100, uuid, 0,
                new Combat(Level._0,0,0), new Mining(Level._0, 0, 0),
                new Herbalism(Level._0, 0, 0), new Crafting(Level._0, 0, 0), banks, calendar.getTime()));
    }
}
