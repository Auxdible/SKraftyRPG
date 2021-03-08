package com.auxdible.skrpg;

import com.auxdible.skrpg.commands.*;
import com.auxdible.skrpg.commands.admin.*;
import com.auxdible.skrpg.commands.inventory.*;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.*;
import com.auxdible.skrpg.mobs.npcs.NPCManager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.PlayerJoinLeaveListener;
import com.auxdible.skrpg.player.PlayerListener;
import com.auxdible.skrpg.player.PlayerManager;
import com.auxdible.skrpg.player.economy.TradeManager;
import com.auxdible.skrpg.player.guilds.GuildInviteManager;
import com.auxdible.skrpg.player.guilds.GuildManager;
import com.auxdible.skrpg.player.guilds.raid.RaidManager;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.regions.RegionManager;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SKRPG extends JavaPlugin {

    public PlayerManager playerManager;
    public PlayerManager getPlayerManager() { return playerManager; }

    public MobManager mobManager;
    public MobManager getMobManager() { return mobManager; }

    public RegionManager regionManager;
    public RegionManager getRegionManager() { return regionManager; }

    public NPCManager npcManager;
    public NPCManager getNpcManager() { return npcManager; }

    public MobSpawnManager mobSpawnManager;
    public MobSpawnManager getMobSpawnManager() { return mobSpawnManager; }

    public TradeManager tradeManager;
    public TradeManager getTradeManager() { return tradeManager; }

    public GuildManager guildManager;
    public GuildManager getGuildManager() { return guildManager; }

    public GuildInviteManager guildInviteManager;
    public GuildInviteManager getGuildInviteManager() { return guildInviteManager; }

    public RaidManager raidManager;
    public RaidManager getRaidManager() { return raidManager; }

    private String host, database, username, password;
    private int port;
    private static Connection connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SKRPG core plugin...");
        saveDefaultConfig();
        host = getConfig().getString("mySQL.host");
        database = getConfig().getString("mySQL.database");
        username = getConfig().getString("mySQL.username");
        password = getConfig().getString("mySQL.password");
        port = getConfig().getInt("mySQL.port");

        try {
            openConnection();
            getLogger().info("Connected to database.");
        } catch (SQLException x) {
            x.printStackTrace();
        }

        try {
            prepareStatement("CREATE TABLE IF NOT EXISTS stat_table(" +
                    "UUID varchar(36), baseHP int(11), baseDefence int(11), baseStrength int(11), " +
                    "baseEnergy int(11), baseSpeed int(11), credits int(11), interest varchar(36), canTrade tinyint(1), raritySell varchar(36), questsCompleted longtext);").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS skills_table(UUID varchar(36), miningLevel int(11), miningXpTill int(11), miningXpTotal int(11), herbalismLevel int(11), herbalismXpTill int(11), " +
                    "herbalismXpTotal int(11), craftingLevel int(11), craftingXpTill int(11), craftingXpTotal int(11), combatLevel int(11), combatXpTill int(11), combatXpTotal int(11));").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS banks_table(bank1Level varchar(36), bank1Credits int(11), bank2Level varchar(36)," +
                    "bank2Credits int(11), bank3Level varchar(36), bank3Credits int(11), bank4Level varchar(36), bank4Credits int(11), bank5Level varchar(36)," +
                    "bank5Credits int(11), bankAmount int(11), UUID varchar(36));").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS collection_table(UUID varchar(36), collectionsTier longtext, collectionsAmount longtext);").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS guilds_table(ID int(11), NAME varchar(16), MEMBERS longtext, RANKS longtext, PERMISSIONS longtext, REGIONS longtext);").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerManager = new PlayerManager(this);
        mobManager = new MobManager(this);
        regionManager = new RegionManager(this);
        npcManager = new NPCManager(this);
        mobSpawnManager = new MobSpawnManager(this);
        tradeManager = new TradeManager(this);
        guildManager = new GuildManager(this);
        guildInviteManager = new GuildInviteManager(this);
        raidManager = new RaidManager(this);
        mobSpawnManager.enable();
        npcManager.enable();
        guildManager.enableGuilds();
        regionManager.enable();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.DO_MOB_SPAWNING, false);
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        getCommand("menu").setExecutor(new MenuCommand(this));
        getCommand("createregion").setExecutor(new CreateRegionCommand(this));
        getCommand("ct").setExecutor(new CraftingTableCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("skills").setExecutor(new SkillsCommand(this));
        getCommand("createnpc").setExecutor(new CreateNPCCommand(this));
        getCommand("createMobSpawn").setExecutor(new CreateMobSpawnCommand(this));
        getCommand("addCredits").setExecutor(new AddCreditsCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("spawnEntity").setExecutor(new SpawnEntityCommand(this));
        getCommand("collections").setExecutor(new CollectionsCommand(this));
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getCommand("guild").setExecutor(new GuildCommand(this));
        getCommand("skrpg").setExecutor(new SKRPGCommand(this));
        getCommand("recipe").setExecutor(new RecipeCommand(this));
        getLogger().info("Loaded SKRPG core plugin!");
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            Text.color(
                                    "&c" + playerData.getHp() + "/" + playerData.getMaxHP() + " ♥ " +
                                            "&a" + playerData.getDefence() + " ✿ Defence " +
                                            "&e" + playerData.getEnergy() + "/" + playerData.getMaxEnergy() + " ☢ Energy"
                            )
                    ));
                    if (playerData.getHp() <= 0) {
                        if (getPlayerManager().getPlayerData(player.getUniqueId()).getHp() <= 0) {
                            player.teleport(Bukkit.getWorld(getConfig().getString("rpgWorld")).getSpawnLocation());
                            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                            player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
                            getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                                    getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
                        }
                    }
                    boolean regionFound = false;
                    for (Region region : getRegionManager().getRegions()) {
                       if (locationIsInCuboid(player.getLocation(),
                               new Location(region.getSpawnLocation().getWorld(),
                                       region.getX(), 0, region.getZ()),
                               new Location(region.getSpawnLocation().getWorld(), region.getX2(), 256, region.getZ2()))) {
                            if (playerData.getRegion() != region) {
                                player.sendTitle(Text.color("&e&lYou are now entering"),
                                        Text.color(region.getName()), 20, 40, 20);
                            }
                            if (region.getControllingGuild() != null) {
                                player.getScoreboard().getTeam("regionOwner")
                                        .setSuffix(Text.color(region.getControllingGuild().getName()));
                            } else {
                                player.getScoreboard().getTeam("regionOwner")
                                        .setSuffix(Text.color("&8NONE"));
                            }
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(region);
                            regionFound = true;
                            break;
                        }
                    }
                    if (!regionFound) {
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(null);
                        player.getScoreboard().getTeam("regionOwner")
                                .setSuffix(Text.color("&8NONE"));
                    }
                    int strengthIncrease = 0;
                    int defenceIncrease = 0;
                    int energyIncrease = 0;
                    int hpIncrease = 0;
                    int speedIncrease = 0;
                    for (Items item : Items.values()) {
                        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName()))) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getHelmet() != null) {
                            if (player.getInventory().getHelmet().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getChestplate() != null) {
                            if (player.getInventory().getChestplate().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getLeggings() != null) {
                            if (player.getInventory().getLeggings().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getBoots() != null) {
                            if (player.getInventory().getBoots().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }

                    }
                    playerData.setStrength(playerData.getBaseStrength() + strengthIncrease);
                    playerData.setDefence(playerData.getBaseDefence() + defenceIncrease);
                    playerData.setMaxEnergy(playerData.getBaseEnergy() + energyIncrease);
                    playerData.setMaxHP(playerData.getBaseHP() + hpIncrease);
                    playerData.setSpeed(playerData.getBaseSpeed() + speedIncrease);
                }
            }
        }.runTaskTimer(this, 0, 20);
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                    if (playerData.getHp() < playerData.getMaxHP()) {
                        if ((int) Math.round(playerData.getMaxHP() * 0.1) > playerData.getMaxHP()) {
                            playerData.setHp(playerData.getMaxHP());
                        } else {
                            playerData.setHp(playerData.getHp() + (int) Math.round(playerData.getMaxHP() * 0.1));
                        }


                    }
                    if (playerData.getEnergy() < playerData.getMaxEnergy()) {
                        if ((int) Math.round(playerData.getMaxEnergy() * 0.1) > playerData.getMaxEnergy()) {
                            playerData.setEnergy(playerData.getMaxEnergy());
                        } else {
                            playerData.setEnergy(playerData.getEnergy() + (int) Math.round(playerData.getMaxEnergy() * 0.1));
                        }


                    }
                }
            }
        }.runTaskTimer(this, 0, 80);
        SKRPG skrpg = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (MobSpawn mobSpawn : mobSpawnManager.getMobSpawns()) {
                    if (mobSpawn.getCurrentlySpawnedMobs() == null || mobSpawn.getCurrentlySpawnedMobs().isEmpty()) {
                        MobType.buildMob(mobSpawn.getMob().getId(), skrpg, mobSpawn);
                    }
                }
            }
        }.runTaskTimer(this, 0, 300);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playerManager.disableMySQL();
        regionManager.disable();
        npcManager.disable();
        mobSpawnManager.disable();
        guildManager.disableGuilds();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("The server is reloading!");
        }
        for (Entity entity : Bukkit.getWorld(getConfig().getString("rpgWorld")).getEntities()) {
            entity.remove();
        }
    }
    public static int levelToInt(String level) {
        return Integer.parseInt(level.replaceAll("_", ""));
    }
    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }
    public static PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException x) {
            x.printStackTrace();
        }
        return ps;
    }
    public boolean locationIsInCuboid(Location playerLocation, Location min, Location max) {
        boolean trueOrNot = false;
        if (playerLocation.getWorld() == min.getWorld() && playerLocation.getWorld() == max.getWorld()) {
            if (playerLocation.getX() >= min.getX() && playerLocation.getX() <= max.getX()) {
                if (playerLocation.getY() >= min.getY() && playerLocation.getY() <= max.getY()) {
                    if (playerLocation.getZ() >= min.getZ()
                            && playerLocation.getZ() <= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
            if (playerLocation.getX() <= min.getX() && playerLocation.getX() >= max.getX()) {
                if (playerLocation.getY() <= min.getY() && playerLocation.getY() >= max.getY()) {
                    if (playerLocation.getZ() <= min.getZ()
                            && playerLocation.getZ() >= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
        }
        return trueOrNot;
    }
}
